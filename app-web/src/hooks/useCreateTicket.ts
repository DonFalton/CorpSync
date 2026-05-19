import { useMutation, useQueryClient } from '@tanstack/react-query';
import { supabase } from '../shared/api/supabase/client';
import type { Database } from '../types/supabase';

// Definimos el tipo de entrada directamente del tipo 'Insert' de Supabase
export type CreateTicketInput = Database['public']['Tables']['tickets']['Insert'];

export const useCreateTicket = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (newTicket: CreateTicketInput) => {
      const { data, error } = await supabase
        .from('tickets')
        .insert(newTicket)
        .select()
        .single();
        
      if (error) {
        throw new Error(error.message);
      }
      return data;
    },
    onSuccess: async (data) => {
      // 1. Enviar notificación a los Jefes de Técnicos (admin de Informática)
      try {
        console.log('[NOTIF] 1. Ticket creado con éxito. Iniciando envío de notificaciones...', data.id);
        console.log('[NOTIF] 2. Buscando admins de Informática...');
        const { data: admins, error: errorAdmins } = await supabase
          .from('perfiles')
          .select('id')
          .eq('rol', 'admin')
          .eq('departamento', 'Informática');

        console.log('[NOTIF] 3. Resultado de admins encontrados:', admins, errorAdmins);

        if (admins && admins.length > 0) {
          const notificaciones = admins.map(admin => ({
            perfil_id: admin.id,
            ticket_id: data.id,
            titulo: 'Nuevo Ticket Creado',
            mensaje: `Se ha reportado un nuevo ticket: ${data.titulo}`,
            tipo: 'creacion_ticket'
          }));
          console.log('[NOTIF] 4. Payload construido para el RPC:', notificaciones);
          const { data: rpcData, error: rpcError } = await supabase.rpc('insertar_notificaciones_seguras', { payload: notificaciones });
          console.log('[NOTIF] 5. Respuesta del RPC:', { rpcData, rpcError });
        }
      } catch (err) {
        console.error('[NOTIF] EXCEPCIÓN FATAL en useCreateTicket:', err);
      }

      // 2. Forzar la actualización del listado de tickets y el dashboard
      queryClient.invalidateQueries({ queryKey: ['tickets'] });
    },
  });
};
