import { useEffect } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { supabase } from '../shared/api/supabase/client';

export const useTicketsRealtime = (onUpdate?: (payload: any) => void) => {
    const queryClient = useQueryClient();

    useEffect(() => {
        // Nos suscribimos a cualquier UPDATE en la tabla tickets
        const channel = supabase
            .channel('schema-db-changes')
            .on(
                'postgres_changes',
                {
                    event: 'UPDATE',
                    schema: 'public',
                    table: 'tickets',
                },
                (payload) => {
                    console.info('[Realtime] Ticket actualizado por IA/Técnico:', payload.new.id);

                    // Invalida TODAS las queries que contengan 'tickets' en su key.
                    // Esto forzará a useTickets.ts a hacer un re-fetch silencioso.
                    queryClient.invalidateQueries({ queryKey: ['tickets'] });

                    // Si se proporciona un callback, se ejecuta para refrescar estados locales
                    if (onUpdate) {
                        onUpdate(payload);
                    }
                }
            )
            .subscribe();

        // Cleanup phase: Desconectar al desmontar para evitar memory leaks
        return () => {
            supabase.removeChannel(channel);
        };
    }, [queryClient, onUpdate]);
};