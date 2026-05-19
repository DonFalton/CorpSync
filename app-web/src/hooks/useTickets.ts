import { useQuery } from '@tanstack/react-query';
import { QueryData } from '@supabase/supabase-js';
import { supabase } from '../shared/api/supabase/client';

// Query builder con resolución explícita de llaves foráneas para perfil del creador
const ticketsQuery = supabase
  .from('tickets')
  .select('*, creador:perfiles!tickets_empleado_id_fkey(*)');

// Inferencia estática de tipos derivados de la consulta SQL (Supabase Types)
export type TicketWithRelations = QueryData<typeof ticketsQuery>[0];

// Hook de TanStack Query para fetching, caché e invalidación en tiempo real
export const useTickets = () => {
  return useQuery({
    queryKey: ['tickets'],
    queryFn: async () => {
      const { data, error } = await ticketsQuery;
      
      if (error) {
        // React Query interceptará este error y activará isError = true
        throw new Error(error.message);
      }
      
      return data as TicketWithRelations[];
    },
  });
};
