import { Link } from 'react-router-dom';
import { useTickets, TicketWithRelations } from '../hooks/useTickets';

const getPriorityColor = (prioridad: string | null) => {
  switch (prioridad) {
    case '1':
      return 'bg-red-100 text-red-800 border-red-200';
    case '2':
      return 'bg-orange-100 text-orange-800 border-orange-200';
    case '3':
      return 'bg-blue-100 text-blue-800 border-blue-200';
    default:
      return 'bg-gray-100 text-gray-800 border-gray-200';
  }
};

const getStatusBadge = (estado: string) => {
  const baseClasses = "px-2.5 py-0.5 rounded-full text-xs font-medium border";
  switch (estado.toLowerCase()) {
    case 'nuevo':
      return `${baseClasses} bg-purple-100 text-purple-800 border-purple-200`;
    case 'en_proceso':
      return `${baseClasses} bg-yellow-100 text-yellow-800 border-yellow-200`;
    case 'resuelto':
      return `${baseClasses} bg-green-100 text-green-800 border-green-200`;
    case 'cerrado':
      return `${baseClasses} bg-gray-100 text-gray-800 border-gray-200`;
    default:
      return `${baseClasses} bg-blue-100 text-blue-800 border-blue-200`;
  }
};

export const Tickets = () => {
  const { data: tickets, isLoading, isError, error } = useTickets();

  if (isLoading) {
    return (
      <div className="p-8">
        <h1 className="text-3xl font-bold text-gray-800 mb-6">Gestión de Tickets</h1>
        <div className="space-y-4">
          {[1, 2, 3].map((skeleton) => (
            <div key={skeleton} className="animate-pulse bg-white p-6 rounded-lg border border-gray-200 shadow-sm">
              <div className="h-4 bg-gray-200 rounded w-1/4 mb-4"></div>
              <div className="h-4 bg-gray-200 rounded w-1/2 mb-2"></div>
              <div className="h-4 bg-gray-200 rounded w-1/3"></div>
            </div>
          ))}
        </div>
      </div>
    );
  }

  if (isError) {
    return (
      <div className="p-8">
        <h1 className="text-3xl font-bold text-gray-800 mb-6">Gestión de Tickets</h1>
        <div className="bg-red-50 border-l-4 border-red-500 p-4 rounded shadow-sm" role="alert">
          <div className="flex">
            <div className="flex-shrink-0">
              <svg className="h-5 w-5 text-red-400" viewBox="0 0 20 20" fill="currentColor">
                <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clipRule="evenodd" />
              </svg>
            </div>
            <div className="ml-3">
              <p className="text-sm text-red-700">
                Ocurrió un error al cargar los tickets: {error instanceof Error ? error.message : 'Error desconocido'}
              </p>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="p-8 max-w-7xl mx-auto">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Gestión de Tickets</h1>
        <Link 
          to="/tickets/new"
          className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors shadow-sm inline-block"
        >
          Nuevo Ticket
        </Link>
      </div>

      {!tickets || tickets.length === 0 ? (
        <div className="text-center py-16 bg-white rounded-xl border border-gray-200 shadow-sm">
          <svg className="mx-auto h-12 w-12 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
          </svg>
          <h3 className="mt-2 text-sm font-medium text-gray-900">No hay tickets</h3>
          <p className="mt-1 text-sm text-gray-500">Comienza creando un nuevo ticket.</p>
        </div>
      ) : (
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
          {tickets.map((ticket: TicketWithRelations) => (
            <div key={ticket.id} className="bg-white rounded-xl shadow-sm border border-gray-200 p-5 hover:shadow-md transition-shadow">
              <div className="flex justify-between items-start mb-4">
                <span className={getStatusBadge(ticket.estado)}>
                  {ticket.estado.replace('_', ' ').toUpperCase()}
                </span>
                <span className={`px-2 py-0.5 rounded text-xs font-semibold border ${getPriorityColor(ticket.prioridad)}`}>
                  P{ticket.prioridad}
                </span>
              </div>
              
              {/* Prevención de inyección XSS: Solo renderizamos variables de texto plano dentro de llaves */}
              <h2 className="text-lg font-bold text-gray-900 mb-2 line-clamp-1">{ticket.titulo}</h2>
              <p className="text-gray-600 text-sm mb-4 line-clamp-3">{ticket.descripcion}</p>
              
              <div className="flex items-center justify-between mt-auto pt-4 border-t border-gray-100">
                <div className="flex items-center">
                  <div className="h-8 w-8 rounded-full bg-blue-100 flex items-center justify-center text-blue-700 font-bold text-xs">
                    {ticket.creador?.nombre?.charAt(0).toUpperCase() || '?'}
                  </div>
                  <span className="ml-2 text-sm font-medium text-gray-700 line-clamp-1">
                    {ticket.creador?.nombre || 'Usuario Desconocido'}
                  </span>
                </div>
                <span className="text-xs text-gray-500">
                  {new Date(ticket.creado_en).toLocaleDateString()}
                </span>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
