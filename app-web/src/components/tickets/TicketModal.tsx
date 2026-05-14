import { useState } from 'react';
import { TicketWithRelations } from '../../hooks/useTickets';
import { useUpdateTicket } from '../../hooks/useUpdateTicket';
import { useTecnicos } from '../../hooks/useTecnicos';
import toast from 'react-hot-toast';

interface TicketModalProps {
  ticket: TicketWithRelations;
  onClose: () => void;
}

export const TicketModal = ({ ticket, onClose }: TicketModalProps) => {
  const updateTicketMutation = useUpdateTicket();
  const { data: tecnicos } = useTecnicos();
  
  const [estado, setEstado] = useState(ticket.estado);
  const [prioridad, setPrioridad] = useState(ticket.prioridad || 'por_asignar');
  const [categoria, setCategoria] = useState(ticket.categoria || '');
  const [tecnicoId, setTecnicoId] = useState(ticket.tecnico_id || '');

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
                className="w-full rounded-md border border-gray-300 p-2 shadow-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500"
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
                className="w-full rounded-md border border-gray-300 p-2 shadow-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500"
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
              <input 
                type="text"
                value={categoria} 
                onChange={(e) => setCategoria(e.target.value)}
                className="w-full rounded-md border border-gray-300 p-2 shadow-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500"
                placeholder="Ej. Hardware, Software..."
              />
            </div>
          </div>

          <div className="flex justify-between items-center pt-4 border-t border-gray-200">
            <div className="flex items-center gap-3">
              <label className="text-sm font-medium text-gray-700">Técnico Asignado:</label>
              <select 
                value={tecnicoId}
                onChange={(e) => setTecnicoId(e.target.value)}
                className="rounded-md border border-gray-300 p-2 shadow-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500 text-sm"
              >
                <option value="">Sin asignar</option>
                {tecnicos?.map(t => (
                  <option key={t.id} value={t.id}>{t.nombre}</option>
                ))}
              </select>
            </div>
            <div className="flex space-x-3">
              <button 
                onClick={onClose}
                className="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 cursor-pointer"
              >
                Cancelar
              </button>
              <button 
                onClick={handleSave}
                disabled={updateTicketMutation.isPending}
                className="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-lg hover:bg-blue-700 disabled:opacity-50 cursor-pointer"
              >
                {updateTicketMutation.isPending ? 'Guardando...' : 'Guardar Cambios'}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
