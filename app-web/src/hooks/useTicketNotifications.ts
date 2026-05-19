import { useEffect } from 'react';
import { supabase } from '../shared/api/supabase/client';
import toast from 'react-hot-toast';
import { useNotificationStore, NotificationItem } from '../store/useNotificationStore';

export const useTicketNotifications = (usuarioId?: string) => {
  const addNotification = useNotificationStore(state => state.addNotification);

  useEffect(() => {
    if (!usuarioId) return;

    // Función de carga inicial para la campana de notificaciones (Hydration)
    const fetchInitialNotifications = async () => {
      try {
        const { data, error } = await supabase
          .from('notificaciones')
          .select('*')
          .eq('perfil_id', usuarioId)
          .eq('leida', false)
          .order('fecha', { ascending: false });

        if (!error && data) {
          // Importante: No usamos toast() aquí para la carga inicial
          data.forEach(notif => {
            addNotification(notif as NotificationItem);
          });
        }
      } catch (err) {
        console.error('Error fetching initial notifications:', err);
      }
    };

    // Función para evaluar SLAs e insertar eventos en notificaciones
    const evaluateSLAs = async () => {
      try {
        const { data: tickets } = await supabase
          .from('tickets')
          .select('*')
          .neq('estado', 'resuelto');

        if (!tickets) return;

        const now = new Date().getTime();
        const alertsToInsert: any[] = [];

        const checkExists = async (ticketId: number, tipo: string) => {
          const { data } = await supabase
            .from('notificaciones')
            .select('id')
            .eq('ticket_id', ticketId)
            .eq('tipo', tipo)
            .limit(1);
          return data && data.length > 0;
        };

        let admins: any[] | null = null;
        const getAdmins = async () => {
          if (!admins) {
            const { data } = await supabase
              .from('perfiles')
              .select('id')
              .eq('rol', 'admin')
              .eq('departamento', 'Informática');
            admins = data;
          }
          return admins || [];
        };

        for (const ticket of tickets) {
          const createdAt = new Date(ticket.creado_en).getTime();
          const elapsedMs = now - createdAt;
          const elapsedHours = elapsedMs / (1000 * 60 * 60);

          // 1. Ticket Huérfano
          if (!ticket.tecnico_id && elapsedHours > 2) {
            if (!(await checkExists(ticket.id, 'huerfano'))) {
              const adminList = await getAdmins();
              adminList.forEach(admin => {
                alertsToInsert.push({
                  perfil_id: admin.id,
                  ticket_id: ticket.id,
                  titulo: 'Ticket Huérfano',
                  mensaje: `El ticket "${ticket.titulo}" lleva más de 2h sin asignar.`,
                  tipo: 'huerfano'
                });
              });
            }
          }

          // 2. Evaluación de SLA
          let slaHours = 0;
          switch (ticket.prioridad) {
            case 'critica': slaHours = 4; break;
            case 'alta': slaHours = 24; break;
            case 'media': slaHours = 72; break;
            case 'baja': slaHours = 168; break;
            default: break;
          }

          if (slaHours > 0) {
            const percentage = (elapsedHours / slaHours) * 100;

            if (percentage >= 100) {
              if (!(await checkExists(ticket.id, 'sla_vencido'))) {
                const adminList = await getAdmins();
                adminList.forEach(admin => {
                  alertsToInsert.push({
                    perfil_id: admin.id,
                    ticket_id: ticket.id,
                    titulo: 'SLA Vencido',
                    mensaje: `El ticket "${ticket.titulo}" ha vencido su SLA de ${slaHours}h.`,
                    tipo: 'sla_vencido'
                  });
                });
              }
            } else if (percentage >= 80 && ticket.tecnico_id) {
              if (!(await checkExists(ticket.id, 'sla_riesgo'))) {
                alertsToInsert.push({
                  perfil_id: ticket.tecnico_id,
                  ticket_id: ticket.id,
                  titulo: 'Riesgo de SLA',
                  mensaje: `El ticket "${ticket.titulo}" está al ${percentage.toFixed(0)}% de vencer su SLA.`,
                  tipo: 'sla_riesgo'
                });
              }
            }
          }
        }

        if (alertsToInsert.length > 0) {
          await supabase.rpc('insertar_notificaciones_seguras', { payload: alertsToInsert });
        }
      } catch (err) {
        console.error('Error al evaluar SLAs:', err);
      }
    };

    // Suscripción en tiempo real a la tabla notificaciones para este usuario
    const channel = supabase.channel(`notificaciones-${usuarioId}`)
      .on(
        'postgres_changes',
        { 
          event: 'INSERT', 
          schema: 'public', 
          table: 'notificaciones', 
          filter: `perfil_id=eq.${usuarioId}` 
        },
        (payload) => {
          const newNotif = payload.new as any;
          toast.success(newNotif.titulo);
          addNotification(newNotif as NotificationItem);
        }
      )
      .subscribe();

    // Evaluación inicial y luego cada 5 minutos
    fetchInitialNotifications();
    evaluateSLAs();
    const intervalId = setInterval(evaluateSLAs, 300000);

    return () => {
      supabase.removeChannel(channel);
      clearInterval(intervalId);
    };
  }, [usuarioId, addNotification]);
};
