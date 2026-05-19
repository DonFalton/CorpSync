import { useMutation, useQueryClient } from '@tanstack/react-query';
import { supabase } from '../shared/api/supabase/client';
import { Database } from '../types/supabase';

type TicketUpdate = Database['public']['Tables']['tickets']['Update'];

export const useUpdateTicket = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({ id, ...updateData }: TicketUpdate & { id: number }) => {
      // Extracción de estado previo para motor de comparación de eventos
      const { data: oldTicket } = await supabase
        .from('tickets')
        .select('*')
        .eq('id', id)
        .single();

      // Mutación de la entidad principal (Ticket)
      const { data, error } = await supabase
        .from('tickets')
        .update(updateData)
        .eq('id', id)
        .select()
        .single();

      if (error) {
        throw new Error(error.message);
      }

      // Motor de reglas de negocio para generación de notificaciones transaccionales
      if (oldTicket && data) {
        try {
          console.log('[NOTIF-UPDATE] 1. Ticket actualizado con éxito. Evaluando cambios de estado y asignación para ticket:', data.id);
          const newNotifications = [];
          
          // Regla 1: Alerta a creador por transición de ciclo de vida
          if (oldTicket.estado !== data.estado) {
            newNotifications.push({
              perfil_id: data.empleado_id,
              ticket_id: data.id,
              titulo: 'Cambio de Estado',
              mensaje: `Tu ticket "${data.titulo}" ha cambiado a estado: ${data.estado}`,
              tipo: 'cambio_estado'
            });
          }
          
          // Regla 2: Alerta a técnico por transferencia de propiedad
          if (data.tecnico_id && oldTicket.tecnico_id !== data.tecnico_id) {
            newNotifications.push({
              perfil_id: data.tecnico_id,
              ticket_id: data.id,
              titulo: 'Nueva Asignación',
              mensaje: `Se te ha asignado el ticket: ${data.titulo}`,
              tipo: 'nueva_asignacion'
            });
          }
          
          // Regla 3: Alerta a gerencia IT por resolución final
          if (data.estado === 'resuelto' && oldTicket.estado !== 'resuelto') {
            console.log('[NOTIF-UPDATE] 2. Ticket resuelto, buscando admins de IT...');
            const { data: admins, error: errorAdmins } = await supabase
              .from('perfiles')
              .select('id')
              .eq('rol', 'admin')
              .eq('departamento', 'IT');
              
            console.log('[NOTIF-UPDATE] 3. Resultado de admins encontrados:', admins, errorAdmins);
            
            if (admins) {
              admins.forEach(admin => {
                newNotifications.push({
                  perfil_id: admin.id,
                  ticket_id: data.id,
                  titulo: 'Ticket Resuelto',
                  mensaje: `El ticket "${data.titulo}" ha sido marcado como resuelto.`,
                  tipo: 'ticket_resuelto'
                });
              });
            }
          }
          
          // Transmisión RPC por lotes para evasión de RLS e inserción masiva
          if (newNotifications.length > 0) {
            console.log('[NOTIF-UPDATE] 4. Payload construido para el RPC:', newNotifications);
            const { data: rpcData, error: rpcError } = await supabase.rpc('insertar_notificaciones_seguras', { payload: newNotifications });
            console.log('[NOTIF-UPDATE] 5. Respuesta del RPC:', { rpcData, rpcError });
          } else {
            console.log('[NOTIF-UPDATE] 4. No hay notificaciones nuevas generadas en esta actualización.');
          }
        } catch (err) {
          console.error('[NOTIF-UPDATE] EXCEPCIÓN FATAL en useUpdateTicket:', err);
        }
      }

      return data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['tickets'] });
    },
  });
};
