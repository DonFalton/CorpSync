import { useQuery } from '@tanstack/react-query';
import { supabase } from '../shared/api/supabase/client';

export interface Tecnico {
  id: string;
  nombre: string;
  rol: string;
}

// Extracción y cacheo del censo de personal operativo para reasignaciones
export const useTecnicos = () => {
  return useQuery<Tecnico[], Error>({
    queryKey: ['tecnicos'],
    queryFn: async () => {
        const { data, error } = await supabase
          .from('perfiles')
          .select('id, nombre, rol')
          .eq('rol', 'tecnico')
          .eq('departamento', 'IT');
      
      if (error) throw error;
      return data as Tecnico[];
    },
  });
};
