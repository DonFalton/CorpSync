import { useQuery } from '@tanstack/react-query';
import { QueryData } from '@supabase/supabase-js';
import { supabase } from '../shared/api/supabase/client';

// 1. Construir la consulta con desambiguación de llave foránea.
const ticketsQuery = supabase
  .from('tickets')
  .select('*, creador:perfiles!tickets_empleado_id_fkey(*)');

// 2. Inferir automáticamente el tipo derivado usando el utilitario de Supabase
export type TicketWithRelations = QueryData<typeof ticketsQuery>[0];

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
