import { useTickets } from '../hooks/useTickets';

export const Dashboard = () => {
  const { data: tickets, isLoading, isError } = useTickets();

  // Estados derivados
  const totalTickets = tickets?.length || 0;
  const ticketsNuevos = tickets?.filter((t) => t.estado.toLowerCase() === 'nuevo').length || 0;
  const ticketsEnProceso = tickets?.filter((t) => t.estado.toLowerCase() === 'en_proceso').length || 0;
  const prioridadAlta = tickets?.filter((t) => String(t.prioridad) === '1').length || 0;

  if (isLoading) {
    return (
      <div className="p-8">
        <h1 className="text-3xl font-bold text-gray-800 mb-6">Dashboard</h1>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          {[1, 2, 3, 4].map((i) => (
            <div key={i} className="bg-white rounded-xl p-6 shadow-sm border border-gray-200 animate-pulse">
              <div className="h-4 bg-gray-200 rounded w-1/2 mb-4"></div>
              <div className="h-8 bg-gray-300 rounded w-1/4"></div>
            </div>
          ))}
        </div>
      </div>
    );
  }

  if (isError) {
    return (
      <div className="p-8">
        <h1 className="text-3xl font-bold text-gray-800 mb-6">Dashboard</h1>
        <div className="bg-yellow-50 border-l-4 border-yellow-400 p-4 rounded text-yellow-700">
          No se pudieron cargar las métricas en este momento.
        </div>
      </div>
    );
  }

  return (
    <div className="p-8 max-w-7xl mx-auto">
      <h1 className="text-3xl font-bold text-gray-900 mb-8">Dashboard General</h1>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        {/* Metric 1 */}
        <div className="bg-white rounded-2xl p-6 shadow-sm border border-gray-100 hover:shadow-md transition-shadow">
          <div className="flex items-center">
            <div className="p-3 rounded-full bg-blue-100 text-blue-600">
              <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"></path>
              </svg>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-500">Total Tickets</p>
              <h3 className="text-2xl font-bold text-gray-900">{totalTickets}</h3>
            </div>
          </div>
        </div>

        {/* Metric 2 */}
        <div className="bg-white rounded-2xl p-6 shadow-sm border border-gray-100 hover:shadow-md transition-shadow">
          <div className="flex items-center">
            <div className="p-3 rounded-full bg-purple-100 text-purple-600">
              <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"></path>
              </svg>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-500">Nuevos</p>
              <h3 className="text-2xl font-bold text-gray-900">{ticketsNuevos}</h3>
            </div>
          </div>
        </div>

        {/* Metric 3 */}
        <div className="bg-white rounded-2xl p-6 shadow-sm border border-gray-100 hover:shadow-md transition-shadow">
          <div className="flex items-center">
            <div className="p-3 rounded-full bg-yellow-100 text-yellow-600">
              <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"></path>
              </svg>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-500">En Proceso</p>
              <h3 className="text-2xl font-bold text-gray-900">{ticketsEnProceso}</h3>
            </div>
          </div>
        </div>

        {/* Metric 4 */}
        <div className="bg-white rounded-2xl p-6 shadow-sm border border-gray-100 hover:shadow-md transition-shadow">
          <div className="flex items-center">
            <div className="p-3 rounded-full bg-red-100 text-red-600">
              <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M13 10V3L4 14h7v7l9-11h-7z"></path>
              </svg>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-500">Prioridad Alta</p>
              <h3 className="text-2xl font-bold text-gray-900">{prioridadAlta}</h3>
            </div>
          </div>
        </div>
      </div>
      
      {/* Últimos Tickets Previsualización */}
      <div className="mt-8 bg-white rounded-xl shadow-sm border border-gray-200 p-6">
        <h2 className="text-lg font-semibold text-gray-800 mb-4">Actividad Reciente</h2>
        {!tickets || tickets.length === 0 ? (
          <p className="text-gray-500 text-sm">No hay actividad reciente para mostrar.</p>
        ) : (
          <div className="space-y-4">
            {tickets.slice(0, 3).map(ticket => (
              <div key={ticket.id} className="flex items-center justify-between pb-4 border-b border-gray-100 last:border-0 last:pb-0">
                <div>
                  <p className="text-sm font-medium text-gray-900 line-clamp-1">{ticket.titulo}</p>
                  <p className="text-xs text-gray-500 mt-1">{new Date(ticket.creado_en).toLocaleString()}</p>
                </div>
                <span className="px-2 py-1 bg-gray-100 text-gray-700 text-xs rounded border border-gray-200">
                  {ticket.estado.replace('_', ' ').toUpperCase()}
                </span>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};
