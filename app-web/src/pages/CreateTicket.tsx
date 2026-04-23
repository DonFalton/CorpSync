import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { useSupabase } from '../app/providers/SupabaseProvider';
import { useCreateTicket, CreateTicketInput } from '../hooks/useCreateTicket';

// Interface para el formulario omitiendo campos gestionados internamente
type TicketFormData = Omit<CreateTicketInput, 'empleado_id' | 'creado_en' | 'actualizado_en' | 'estado' | 'id' | 'tecnico_id' | 'imagen_url'>;

export const CreateTicket = () => {
  const navigate = useNavigate();
  const { session } = useSupabase();
  const { mutateAsync, isPending, isError, error } = useCreateTicket();

  const { register, handleSubmit, formState: { errors } } = useForm<TicketFormData>({
    defaultValues: {
      prioridad: 'por_asignar', // Modificado para cumplir el CHECK constraint
      categoria: 'Hardware',
    }
  });

  const onSubmit = async (data: TicketFormData) => {
    if (!session?.user?.id) return;

    try {
      await mutateAsync({
        ...data,
        empleado_id: session.user.id,
      });
      navigate('/tickets');
    } catch (err) {
      // El error visual lo maneja isError / error de useCreateTicket en el JSX
      console.error('Error creating ticket:', err);
    }
  };

  return (
    <div className="p-8 max-w-3xl mx-auto">
      <div className="flex items-center mb-8">
        <button
          onClick={() => navigate('/tickets')}
          disabled={isPending}
          className="mr-4 p-2 text-gray-500 hover:text-gray-700 hover:bg-gray-100 rounded-full transition-colors disabled:opacity-50"
          aria-label="Volver atrás"
        >
          <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
          </svg>
        </button>
        <h1 className="text-3xl font-bold text-gray-900">Crear Nuevo Ticket</h1>
      </div>

      <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
        <div className="p-6 border-b border-gray-200">
          <p className="text-sm text-gray-500">
            Completa los detalles a continuación para registrar una nueva incidencia o solicitud.
          </p>
        </div>

        {isError && (
          <div className="m-6 p-4 bg-red-50 border-l-4 border-red-500 rounded text-red-700 text-sm">
            Ocurrió un error al guardar el ticket: {error instanceof Error ? error.message : 'Error desconocido'}
          </div>
        )}

        <form onSubmit={handleSubmit(onSubmit)} className="p-6 space-y-6">
          <div>
            <label htmlFor="titulo" className="block text-sm font-medium text-gray-700 mb-1">
              Título del Ticket <span className="text-red-500">*</span>
            </label>
            <input
              id="titulo"
              type="text"
              {...register('titulo', { required: 'El título es obligatorio' })}
              className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition-colors ${errors.titulo ? 'border-red-300 focus:border-red-500 bg-red-50' : 'border-gray-300 focus:border-blue-500'
                }`}
              placeholder="Ej. Problemas de conexión a la VPN"
            />
            {errors.titulo && <p className="mt-1 text-sm text-red-600">{errors.titulo.message}</p>}
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label htmlFor="categoria" className="block text-sm font-medium text-gray-700 mb-1">
                Categoría
              </label>
              <input
                id="categoria"
                type="text"
                {...register('categoria')}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-colors"
                placeholder="Ej. Software, Hardware, Redes"
              />
            </div>

            <div>
              <label htmlFor="prioridad" className="block text-sm font-medium text-gray-700 mb-1">
                Prioridad
              </label>
              <select
                id="prioridad"
                {...register('prioridad')}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-colors bg-white"
              >
                <option value="por_asignar">Por asignar (Defecto)</option>
                <option value="baja">Baja (P3)</option>
                <option value="media">Media (P2)</option>
                <option value="alta">Alta (P1)</option>
                <option value="critica">Crítica (P0)</option>
              </select>
            </div>
          </div>

          <div>
            <label htmlFor="descripcion" className="block text-sm font-medium text-gray-700 mb-1">
              Descripción <span className="text-red-500">*</span>
            </label>
            <textarea
              id="descripcion"
              rows={5}
              {...register('descripcion', { required: 'La descripción es obligatoria' })}
              className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition-colors resize-y ${errors.descripcion ? 'border-red-300 focus:border-red-500 bg-red-50' : 'border-gray-300 focus:border-blue-500'
                }`}
              placeholder="Describe detalladamente el problema o solicitud..."
            ></textarea>
            {errors.descripcion && <p className="mt-1 text-sm text-red-600">{errors.descripcion.message}</p>}
          </div>

          <div className="pt-4 flex justify-end">
            <button
              type="button"
              onClick={() => navigate('/tickets')}
              disabled={isPending}
              className="px-6 py-2 border border-gray-300 text-gray-700 rounded-lg font-medium mr-3 hover:bg-gray-50 focus:outline-none transition-colors disabled:opacity-50"
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={isPending}
              className={`px-6 py-2 bg-blue-600 text-white rounded-lg font-medium hover:bg-blue-700 focus:ring-4 focus:ring-blue-300 transition-colors ${isPending ? 'opacity-50 cursor-not-allowed' : ''
                }`}
            >
              {isPending ? 'Guardando...' : 'Crear Ticket'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};