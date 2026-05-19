import { useMutation, useQueryClient } from '@tanstack/react-query';
import { supabase } from '../shared/api/supabase/client';
import type { Database } from '../types/supabase';

// Inferencia de tipo estructurado para payloads de inserción según esquema DDL
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
      // Emisión de alertas asíncronas para el departamento de jefatura IT
      try {
        console.log('[NOTIF] 1. Ticket creado con éxito. Iniciando envío de notificaciones...', data.id);
        console.log('[NOTIF] 2. Buscando admins de IT...');
        const { data: admins, error: errorAdmins } = await supabase
          .from('perfiles')
          .select('id')
          .eq('rol', 'admin')
          .eq('departamento', 'IT');

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

      // Inserción de invalidación de caché global para resincronización de UI
      queryClient.invalidateQueries({ queryKey: ['tickets'] });
    },
  });
};
