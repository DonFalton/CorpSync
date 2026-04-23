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
    onSuccess: () => {
      // Forzar la actualización del listado de tickets y el dashboard
      queryClient.invalidateQueries({ queryKey: ['tickets'] });
    },
  });
};
