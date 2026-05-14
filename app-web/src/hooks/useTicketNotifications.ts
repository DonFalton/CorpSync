import { useEffect } from 'react';
import { supabase } from '../shared/api/supabase/client';
import toast from 'react-hot-toast';
import { useNotificationStore } from '../store/useNotificationStore';

export const useTicketNotifications = (usuarioId?: string) => {
  const addNotification = useNotificationStore(state => state.addNotification);

  useEffect(() => {
    if (!usuarioId) return;

    const channel = supabase.channel('custom-all-channel')
      .on(
        'postgres_changes',
        { event: '*', schema: 'public', table: 'tickets' },
        (payload) => {
          const { eventType, new: newRecord, old: oldRecord } = payload;

          if (eventType === 'INSERT') {
            const msj = `🔔 Nuevo ticket: ${newRecord.titulo}`;
            toast.success(msj);
            addNotification({ titulo: 'Nuevo Ticket', mensaje: msj, ticket_id: newRecord.id });
          } else if (eventType === 'UPDATE') {
            // Te han asignado el ticket
            if (newRecord.tecnico_id === usuarioId && oldRecord.tecnico_id !== usuarioId) {
              const msj = `🎯 Te han asignado el ticket: ${newRecord.titulo}`;
              toast.success(msj, { duration: 5000 });
              addNotification({ titulo: 'Asignación de Ticket', mensaje: msj, ticket_id: newRecord.id });
            } 
            // Cambio de estado en un ticket que te pertenece
            else if (newRecord.tecnico_id === usuarioId && newRecord.estado !== oldRecord.estado) {
              const msj = `ℹ️ El estado de tu ticket "${newRecord.titulo}" cambió a ${newRecord.estado.replace('_', ' ').toUpperCase()}`;
              toast.success(msj);
              addNotification({ titulo: 'Cambio de Estado', mensaje: msj, ticket_id: newRecord.id });
            }
          }
        }
      )
      .subscribe();

    return () => {
      supabase.removeChannel(channel);
    };
  }, [usuarioId, addNotification]);
};
