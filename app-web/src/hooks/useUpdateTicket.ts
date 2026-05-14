import { useMutation, useQueryClient } from '@tanstack/react-query';
import { supabase } from '../shared/api/supabase/client';
import { Database } from '../types/supabase';

type TicketUpdate = Database['public']['Tables']['tickets']['Update'];

export const useUpdateTicket = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({ id, ...updateData }: TicketUpdate & { id: number }) => {
      const { data, error } = await supabase
        .from('tickets')
        .update(updateData)
        .eq('id', id)
        .select()
        .single();

      if (error) {
        throw new Error(error.message);
      }
      return data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['tickets'] });
    },
  });
};
