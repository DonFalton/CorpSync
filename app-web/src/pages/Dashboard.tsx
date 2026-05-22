import { useTickets } from '../hooks/useTickets';
import { PieChart, Pie, Cell, BarChart, Bar, XAxis, YAxis, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { useNavigate } from 'react-router-dom';

export const Dashboard = () => {
  // Suscripción a la fuente de verdad de tickets
  const { data: tickets, isLoading, isError } = useTickets();
  const navigate = useNavigate();

  // Cálculo de KPIs globales para indicadores de cabecera
  const totalTickets = tickets?.length || 0;
  const ticketsNuevos = tickets?.filter((t) => t.estado.toLowerCase() === 'pendiente').length || 0;
  const ticketsEnProceso = tickets?.filter((t) => t.estado.toLowerCase() === 'en_proceso').length || 0;
  const prioridadAlta = tickets?.filter((t) => String(t.prioridad) === 'alta' || String(t.prioridad) === 'critica').length || 0;

  // Transformación de la colección de tickets para Gráfica de Tarta (Estados)
  const countPorEstado = tickets?.reduce((acc: Record<string, number>, ticket) => {
    const estado = ticket.estado;
    acc[estado] = (acc[estado] || 0) + 1;
    return acc;
  }, {});

  const ticketsPorEstado = countPorEstado ? Object.entries(countPorEstado).map(([name, value]) => ({
    name: name.replace('_', ' ').toUpperCase(),
    originalName: name,
    value
  })) : [];

  const COLORS: Record<string, string> = {
    'PENDIENTE': '#9333ea',
    'EN PROCESO': '#eab308',
    'RESUELTO': '#16a34a',
  };

  // Transformación de la colección de tickets para Gráfica de Barras (Categorías)
  const countPorCategoria = tickets?.reduce((acc: Record<string, number>, ticket) => {
    const cat = ticket.categoria || 'Sin Categorizar';
    acc[cat] = (acc[cat] || 0) + 1;
    return acc;
  }, {});

  const ticketsPorCategoria = countPorCategoria ? Object.entries(countPorCategoria).map(([name, total]) => ({
    name,
    total
  })) : [];

  // Generación y descarga del reporte de tickets en formato CSV (UTF-8)
  const handleExportCSV = () => {
    if (!tickets || tickets.length === 0) return;

    const escapeCsvField = (field: string | null | undefined) => {
      if (!field) return '""';
      const stringField = String(field);
      const escaped = stringField.replace(/"/g, '""');
      return `"${escaped}"`;
    };

    const headers = ['ID', 'Título', 'Estado', 'Prioridad', 'Categoría', 'Creado En', 'Creador', 'Técnico ID'].join(',');

    const rows = tickets.map((ticket) => {
      return [
        ticket.id,
        escapeCsvField(ticket.titulo),
        escapeCsvField(ticket.estado),
        ticket.prioridad,
        escapeCsvField(ticket.categoria),
        escapeCsvField(new Date(ticket.creado_en).toLocaleString()),
        escapeCsvField(ticket.creador?.nombre),
        escapeCsvField(ticket.tecnico_id)
      ].join(',');
    });

    const csvContent = [headers, ...rows].join('\n');
    // Añadimos BOM para que Excel detecte UTF-8 correctamente
    const blob = new Blob(['\uFEFF' + csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);

    const link = document.createElement('a');
    const dateStr = new Date().toISOString().split('T')[0];
    link.href = url;
    link.setAttribute('download', `reporte_tickets_${dateStr}.csv`);
    document.body.appendChild(link);
    link.click();

    document.body.removeChild(link);
    URL.revokeObjectURL(url);
  };

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
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold text-gray-900 dark:text-white">Dashboard General</h1>

        {tickets && tickets.length > 0 && (
          <button
            onClick={handleExportCSV}
            className="flex items-center gap-2 px-4 py-2 bg-white text-gray-700 font-medium text-sm border border-gray-300 rounded-lg shadow-sm hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors"
          >
            <svg className="w-4 h-4 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"></path>
            </svg>
            Exportar Reporte
          </button>
        )}
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        {/* Metric 1 */}
        <div
          onClick={() => navigate('/tickets', { state: { activeTab: 'todos', filterEstado: 'todos' } })}
          className="bg-white rounded-2xl p-6 shadow-sm border border-gray-100 cursor-pointer hover:bg-gray-50 transform hover:-translate-y-1 transition-all"
        >
          <div className="flex items-center">
            <div className="p-3 rounded-full bg-blue-100 text-blue-600">
              <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"></path>
              </svg>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-500">Total Tickets</p>
              <h3 className="text-2xl font-bold text-gray-900 dark:text-white">{totalTickets}</h3>
            </div>
          </div>
        </div>

        {/* Metric 2 */}
        <div
          onClick={() => navigate('/tickets', { state: { activeTab: 'todos', filterEstado: 'pendiente' } })}
          className="bg-white rounded-2xl p-6 shadow-sm border border-gray-100 cursor-pointer hover:bg-gray-50 transform hover:-translate-y-1 transition-all"
        >
          <div className="flex items-center">
            <div className="p-3 rounded-full bg-purple-100 text-purple-600">
              <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"></path>
              </svg>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-500">Nuevos</p>
              <h3 className="text-2xl font-bold text-gray-900 dark:text-white">{ticketsNuevos}</h3>
            </div>
          </div>
        </div>

        {/* Metric 3 */}
        <div
          onClick={() => navigate('/tickets', { state: { activeTab: 'todos', filterEstado: 'en_proceso' } })}
          className="bg-white rounded-2xl p-6 shadow-sm border border-gray-100 cursor-pointer hover:bg-gray-50 transform hover:-translate-y-1 transition-all"
        >
          <div className="flex items-center">
            <div className="p-3 rounded-full bg-yellow-100 text-yellow-600">
              <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"></path>
              </svg>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-500">En Proceso</p>
              <h3 className="text-2xl font-bold text-gray-900 dark:text-white">{ticketsEnProceso}</h3>
            </div>
          </div>
        </div>

        {/* Metric 4 */}
        <div
          onClick={() => navigate('/tickets', { state: { activeTab: 'urgentes', filterEstado: 'todos' } })}
          className="bg-white rounded-2xl p-6 shadow-sm border border-gray-100 cursor-pointer hover:bg-gray-50 transform hover:-translate-y-1 transition-all"
        >
          <div className="flex items-center">
            <div className="p-3 rounded-full bg-red-100 text-red-600">
              <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M13 10V3L4 14h7v7l9-11h-7z"></path>
              </svg>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-500">Prioridad Alta</p>
              <h3 className="text-2xl font-bold text-gray-900 dark:text-white">{prioridadAlta}</h3>
            </div>
          </div>
        </div>
      </div>

      {/* Gráficos Recharts */}
      {(!tickets || tickets.length === 0) ? (
        <div className="mt-8 bg-white rounded-xl shadow-sm border border-gray-200 p-8 text-center">
          <p className="text-gray-500 font-medium">Datos insuficientes para generar gráficos</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 mt-8">
          {/* Columna 1: Distribución por Estado */}
          <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
            <h2 className="text-lg font-semibold text-gray-800 mb-6">Distribución por Estado</h2>
            <div className="h-72">
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie
                    data={ticketsPorEstado}
                    innerRadius={60}
                    outerRadius={100}
                    paddingAngle={5}
                    dataKey="value"
                  >
                    {ticketsPorEstado.map((entry, index) => (
                      <Cell
                        key={`cell-${index}`}
                        fill={COLORS[entry.name] || '#3b82f6'}
                        onClick={() => navigate('/tickets', { state: { activeTab: 'todos', filterEstado: entry.originalName } })}
                        className="cursor-pointer hover:opacity-80 transition-opacity"
                        style={{ outline: 'none' }}
                      />
                    ))}
                  </Pie>
                  <Tooltip
                    contentStyle={{ borderRadius: '0.5rem', border: 'none', boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)' }}
                  />
                  <Legend />
                </PieChart>
              </ResponsiveContainer>
            </div>
          </div>

          {/* Columna 2: Carga por Categoría */}
          <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
            <h2 className="text-lg font-semibold text-gray-800 mb-6">Carga por Categoría</h2>
            <div className="h-72">
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={ticketsPorCategoria} margin={{ top: 20, right: 30, left: 0, bottom: 5 }}>
                  <XAxis dataKey="name" stroke="#6b7280" fontSize={12} tickLine={false} axisLine={false} />
                  <YAxis stroke="#6b7280" fontSize={12} tickLine={false} axisLine={false} allowDecimals={false} />
                  <Tooltip
                    cursor={{ fill: '#f3f4f6' }}
                    contentStyle={{ borderRadius: '0.5rem', border: 'none', boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)' }}
                  />
                  <Bar
                    dataKey="total"
                    fill="#3b82f6"
                    radius={[4, 4, 0, 0]}
                    onClick={(data) => navigate('/tickets', { state: { activeTab: 'todos', searchTerm: data.name === 'Sin Categorizar' ? '' : data.name } })}
                    className="cursor-pointer hover:opacity-80 transition-opacity"
                  />
                </BarChart>
              </ResponsiveContainer>
            </div>
          </div>
        </div>
      )}

      {/* Últimos Tickets Previsualización */}
      <div className="mt-8 bg-white rounded-xl shadow-sm border border-gray-200 p-6">
        <h2 className="text-lg font-semibold text-gray-800 mb-4">Actividad Reciente</h2>
        {!tickets || tickets.length === 0 ? (
          <p className="text-gray-500 text-sm">No hay actividad reciente para mostrar.</p>
        ) : (
          <div className="space-y-2">
            {tickets.slice(0, 3).map(ticket => (
              <div
                key={ticket.id}
                onClick={() => navigate('/tickets', { state: { openTicketId: ticket.id } })}
                className="flex items-center justify-between p-3 border border-transparent hover:border-gray-200 hover:bg-gray-50 hover:shadow-sm rounded-lg transition-all cursor-pointer"
              >
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
