import { useEffect } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { supabase } from '../shared/api/supabase/client';

export const useTicketsRealtime = (onUpdate?: (payload: any) => void) => {
    const queryClient = useQueryClient();

    useEffect(() => {
        // Suscripción al canal de replicación PostgreSQL (CDC) para eventos de UPDATE
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

                    // Inserción de invalidación de caché L1 (TanStack) para forzar re-hidratación transparente
                    queryClient.invalidateQueries({ queryKey: ['tickets'] });

                    // Delegación de callback para flujos con gestión de estado local (Ej. EmployeeDashboard)
                    if (onUpdate) {
                        onUpdate(payload);
                    }
                }
            )
            .subscribe();

        // Cierre de la conexión de socket para prevenir fugas de memoria en desmontaje
        return () => {
            supabase.removeChannel(channel);
        };
    }, [queryClient, onUpdate]);
};