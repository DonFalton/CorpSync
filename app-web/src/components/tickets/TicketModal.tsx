import { useState } from 'react';
import { TicketWithRelations } from '../../hooks/useTickets';
import { useUpdateTicket } from '../../hooks/useUpdateTicket';
import { useTecnicos } from '../../hooks/useTecnicos';
import { useSupabase } from '../../app/providers/SupabaseProvider';
import toast from 'react-hot-toast';

interface TicketModalProps {
    ticket: TicketWithRelations;
    onClose: () => void;
}

export const TicketModal = ({ ticket, onClose }: TicketModalProps) => {
    const { session, perfil } = useSupabase();
    const updateTicketMutation = useUpdateTicket();
    const { data: tecnicos } = useTecnicos();

    const [estado, setEstado] = useState(ticket.estado);
    const [prioridad, setPrioridad] = useState(ticket.prioridad || 'por_asignar');
    const [categoria, setCategoria] = useState(ticket.categoria || '');
    const [tecnicoId, setTecnicoId] = useState(ticket.tecnico_id || '');

    // Motor de Control de Acceso Basado en Roles (RBAC) para componentes UI
    const isDireccion = perfil?.rol === 'admin' && perfil?.departamento === 'Dirección';
    const isSoporteNivel1 = perfil?.rol === 'tecnico' && perfil?.departamento === 'IT';
    const isJefeTecnicos = perfil?.rol === 'admin' && perfil?.departamento === 'IT';

    const isReadOnly = isDireccion;
    // Regla de negocio: Bloqueo de reasignación cruzada para Nivel 1
    const isReassignBlocked = !!(isSoporteNivel1 && ticket.tecnico_id && ticket.tecnico_id !== session?.user?.id);
    const disableTecnicoSelect = isReadOnly || isReassignBlocked;

    // Inyección dinámica de opciones de personal basada en nivel de privilegios
    let assignableTecnicos = tecnicos || [];
    if (isSoporteNivel1) {
        assignableTecnicos = assignableTecnicos.filter(t => t.id === session?.user?.id);
    } else if (isJefeTecnicos && perfil) {
        // El Jefe es admin, por lo que no viene en 'tecnicos' (que filtra rol='tecnico'). Lo añadimos.
        assignableTecnicos = [
            { id: perfil.id, nombre: perfil.nombre, rol: perfil.rol },
            ...assignableTecnicos
        ];
    }

    const handleSave = () => {
        updateTicketMutation.mutate(
            {
                id: ticket.id,
                estado,
                prioridad,
                categoria: categoria || null,
                tecnico_id: tecnicoId || null,
            },
            {
                onSuccess: () => {
                    toast.success('Ticket actualizado correctamente');
                    onClose();
                },
                onError: (err) => {
                    toast.error(`Error al actualizar: ${err.message}`);
                }
            }
        );
    };

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4">
            <div className="bg-white rounded-xl shadow-xl w-full max-w-2xl max-h-[90vh] overflow-y-auto">
                <div className="p-6">
                    <div className="flex justify-between items-start mb-6">
                        <h2 className="text-2xl font-bold text-gray-900">{ticket.titulo}</h2>
                        <button onClick={onClose} className="text-gray-400 hover:text-gray-600">
                            <svg className="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                            </svg>
                        </button>
                    </div>

                    <div className="mb-6">
                        <p className="text-gray-700 whitespace-pre-wrap">{ticket.descripcion}</p>
                    </div>

                    {ticket.imagen_url && (
                        <div className="mb-6">
                            <img src={ticket.imagen_url} alt="Adjunto del ticket" className="max-w-full h-auto rounded-lg border border-gray-200" />
                        </div>
                    )}

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">Estado</label>
                            <select
                                value={estado}
                                onChange={(e) => setEstado(e.target.value)}
                                disabled={isReadOnly}
                                className="w-full rounded-md border border-gray-300 p-2 shadow-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500 disabled:opacity-60 disabled:bg-gray-100 bg-white text-gray-900 [color-scheme:light]"
                            >
                                <option value="pendiente">Pendiente</option>
                                <option value="en_proceso">En proceso</option>
                                <option value="resuelto">Resuelto</option>
                            </select>
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">Prioridad</label>
                            <select
                                value={prioridad}
                                onChange={(e) => setPrioridad(e.target.value)}
                                disabled={isReadOnly}
                                className="w-full rounded-md border border-gray-300 p-2 shadow-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500 disabled:opacity-60 disabled:bg-gray-100 bg-white text-gray-900 [color-scheme:light]"
                            >
                                <option value="por_asignar">Sin prioridad</option>
                                <option value="baja">Baja</option>
                                <option value="media">Media</option>
                                <option value="alta">Alta</option>
                                <option value="critica">Crítica</option>
                            </select>
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">Categoría</label>
                            <select
                                value={categoria}
                                onChange={(e) => setCategoria(e.target.value)}
                                disabled={isReadOnly}
                                className="w-full rounded-md border border-gray-300 p-2 shadow-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500 disabled:opacity-60 disabled:bg-gray-100 text-sm bg-white text-gray-900 [color-scheme:light]"
                            >
                                <option value="Sin_categorizar">Sin categorizar</option>
                                <option value="Hardware">Hardware</option>
                                <option value="Software">Software</option>
                                <option value="Redes">Redes</option>
                                <option value="Otros">Otros</option>
                            </select>
                        </div>
                    </div>

                    <div className="flex justify-between items-center pt-4 border-t border-gray-200">
                        <div className="flex items-center gap-3">
                            <label className="text-sm font-medium text-gray-700">Técnico Asignado:</label>
                            <select
                                value={tecnicoId}
                                onChange={(e) => setTecnicoId(e.target.value)}
                                disabled={disableTecnicoSelect}
                                className="rounded-md border border-gray-300 p-2 shadow-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500 disabled:opacity-60 disabled:bg-gray-100 text-sm bg-white text-gray-900 [color-scheme:light]"
                                title={isReassignBlocked ? "No tienes permisos para reasignar tickets de otro técnico" : undefined}
                            >
                                <option value="">Sin asignar</option>
                                {assignableTecnicos.map(t => (
                                    <option key={t.id} value={t.id}>{t.nombre}</option>
                                ))}
                            </select>
                        </div>
                        <div className="flex space-x-3">
                            <button
                                onClick={onClose}
                                className="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 cursor-pointer"
                            >
                                {isReadOnly ? 'Cerrar' : 'Cancelar'}
                            </button>
                            {!isReadOnly && (
                                <button
                                    onClick={handleSave}
                                    disabled={updateTicketMutation.isPending}
                                    className="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-lg hover:bg-blue-700 disabled:opacity-50 cursor-pointer"
                                >
                                    {updateTicketMutation.isPending ? 'Guardando...' : 'Guardar Cambios'}
                                </button>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};