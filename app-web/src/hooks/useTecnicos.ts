import { useQuery } from '@tanstack/react-query';
import { supabase } from '../shared/api/supabase/client';

export interface Tecnico {
  id: string;
  nombre: string;
  rol: string;
}

export const useTecnicos = () => {
  return useQuery<Tecnico[], Error>({
    queryKey: ['tecnicos'],
    queryFn: async () => {
      const { data, error } = await supabase
        .from('perfiles')
        .select('id, nombre, rol')
        .eq('rol', 'tecnico');
      
      if (error) throw error;
      return data as Tecnico[];
    },
  });
};
