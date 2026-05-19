// src/app/providers/SupabaseProvider.tsx
import { createContext, useContext, useEffect, useState } from 'react';
import type { Session } from '@supabase/supabase-js';
import { supabase } from '../../shared/api/supabase/client';

export interface UserProfile {
  id: string;
  nombre: string;
  rol: string;
  departamento: string | null;
  email: string;
}

interface SupabaseContextType {
  session: Session | null;
  perfil: UserProfile | null;
  isLoading: boolean;
}

const SupabaseContext = createContext<SupabaseContextType | undefined>(undefined);

export const SupabaseProvider = ({ children }: { children: React.ReactNode }) => {
  const [session, setSession] = useState<Session | null>(null);
  const [perfil, setPerfil] = useState<UserProfile | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchProfile = async (currentSession: Session | null) => {
      if (!currentSession) {
        setPerfil(null);
        setIsLoading(false);
        return;
      }

      const { data, error } = await supabase
        .from('perfiles')
        .select('*')
        .eq('id', currentSession.user.id)
        .single();

      if (!error && data) {
        setPerfil(data);
      } else {
        setPerfil(null);
      }
      setIsLoading(false);
    };

    // Obtener sesión inicial
    supabase.auth.getSession().then(({ data: { session: initialSession } }) => {
      setSession(initialSession);
      fetchProfile(initialSession);
    });

    // Escuchar cambios de estado (login, logout, token refresh)
    const { data: { subscription } } = supabase.auth.onAuthStateChange((_event: string, currentSession: Session | null) => {
      setIsLoading(true);
      setSession(currentSession);
      fetchProfile(currentSession);
    });

    return () => subscription.unsubscribe();
  }, []);

  return (
    <SupabaseContext.Provider value={{ session, perfil, isLoading }}>
      {children}
    </SupabaseContext.Provider>
  );
};

// Hook de consumo seguro
export const useSupabase = () => {
  const context = useContext(SupabaseContext);
  if (context === undefined) {
    throw new Error('useSupabase debe usarse dentro de un SupabaseProvider');
  }
  return context;
};