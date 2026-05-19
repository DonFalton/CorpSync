import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useTickets, TicketWithRelations } from '../hooks/useTickets';
import { TicketModal } from '../components/tickets/TicketModal';
import { useSupabase } from '../app/providers/SupabaseProvider';
import { useState, useEffect } from 'react';
import { getTicketSLAStatus } from '../shared/utils/dateUtils';

const getPriorityColor = (prioridad: string | null) => {
  switch (prioridad) {
    case 'critica':
      return 'bg-red-100 text-red-800 border-red-200';
    case 'alta':
      return 'bg-orange-100 text-orange-800 border-orange-200';
    case 'media':
      return 'bg-blue-100 text-blue-800 border-blue-200';
    case 'baja':
      return 'bg-green-100 text-green-800 border-green-200';
    default:
      return 'bg-gray-100 text-gray-800 border-gray-200';
  }
};

const getStatusBadge = (estado: string) => {
  const baseClasses = "px-2.5 py-0.5 rounded-full text-xs font-medium border";
  switch (estado.toLowerCase()) {
    case 'pendiente':
      return `${baseClasses} bg-purple-100 text-purple-800 border-purple-200`;
    case 'en_proceso':
      return `${baseClasses} bg-yellow-100 text-yellow-800 border-yellow-200`;
    case 'resuelto':
      return `${baseClasses} bg-green-100 text-green-800 border-green-200`;
    default:
      return `${baseClasses} bg-gray-100 text-gray-800 border-gray-200`;
  }
};

export const Tickets = () => {
  const { session, perfil } = useSupabase();
  const usuarioId = session?.user?.id;
  const isReadOnly = perfil?.rol === 'admin' && perfil?.departamento === 'Dirección';
  const location = useLocation();
  const navigate = useNavigate();
  
  // Suscripción de lectura y selección activa
  const { data: tickets, isLoading, isError, error } = useTickets();
  const [selectedTicket, setSelectedTicket] = useState<TicketWithRelations | null>(null);

  // Inicialización de estado para motor de búsqueda y segmentación
  const [activeTab, setActiveTab] = useState('todos');
  const [searchTerm, setSearchTerm] = useState('');
  const [filterEstado, setFilterEstado] = useState('todos');
  const [filterPrioridad, setFilterPrioridad] = useState('todas');
  const [showOnlyMine, setShowOnlyMine] = useState(false);

  // Interceptación de payload de enrutamiento para aplicación de filtros o auto-apertura (Drill-down)
  useEffect(() => {
    const state = location.state as any;
    if (state && (state.filterEstado || state.activeTab || state.searchTerm || state.openTicketId)) {
      // 1. Aplicar filtros
      if (state.filterEstado !== undefined) setFilterEstado(state.filterEstado);
      if (state.activeTab !== undefined) setActiveTab(state.activeTab);
      if (state.searchTerm !== undefined) setSearchTerm(state.searchTerm);
      
      // 2. Auto-abrir ticket si existe
      if (state.openTicketId) {
        if (tickets && tickets.length > 0) {
          const ticketToOpen = tickets.find(t => t.id === state.openTicketId);
          if (ticketToOpen) {
            setSelectedTicket(ticketToOpen);
          }
          // Limpiar state
          navigate(location.pathname, { replace: true, state: {} });
        } else if (!isLoading) {
          // Ya cargó pero no está, limpiamos igual
          navigate(location.pathname, { replace: true, state: {} });
        }
      } else {
        // No hay ticket que abrir, limpiamos el state
        navigate(location.pathname, { replace: true, state: {} });
      }
    }
  }, [location.state, tickets, isLoading, navigate, location.pathname]);

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

  // Motor de filtrado en memoria (Client-Side) por tab, texto, estado y propiedad
  const filteredTickets = tickets?.filter((ticket) => {
    // 0. Filtro Rápido (Tabs / Colas de Trabajo)
    if (activeTab === 'sin_asignar' && ticket.tecnico_id !== null) {
      return false;
    }
    if (activeTab === 'urgentes') {
      const isUrgent = String(ticket.prioridad) === 'alta' || String(ticket.prioridad) === 'critica';
      const isOpen = ticket.estado !== 'resuelto';
      if (!isUrgent || !isOpen) return false;
    }
    if (activeTab === 'pendientes_cierre' && ticket.estado !== 'resuelto') {
      return false;
    }

    // 1. Filtro de Búsqueda por texto (Título o Descripción o Categoría)
    if (searchTerm) {
      const term = searchTerm.toLowerCase();
      const matchTitle = ticket.titulo.toLowerCase().includes(term);
      const matchDesc = ticket.descripcion?.toLowerCase().includes(term);
      const matchCat = ticket.categoria?.toLowerCase().includes(term);
      if (!matchTitle && !matchDesc && !matchCat) return false;
    }

    // 2. Filtro por Estado
    if (filterEstado !== 'todos' && ticket.estado !== filterEstado) {
      return false;
    }

    // 3. Filtro por Prioridad
    if (filterPrioridad !== 'todas' && String(ticket.prioridad) !== filterPrioridad) {
      return false;
    }

    // 4. Filtro "Mis asignados"
    if (showOnlyMine && ticket.tecnico_id !== usuarioId) {
      return false;
    }

    return true;
  }) || [];

  return (
    <div className="p-8 max-w-7xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Gestión de Tickets</h1>
        {!isReadOnly && (
          <Link 
            to="/tickets/new"
            className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors shadow-sm inline-block"
          >
            Nuevo Ticket
          </Link>
        )}
      </div>

      {/* Vistas Rápidas (Tabs / Colas de Trabajo) */}
      <div className="flex overflow-x-auto gap-2 pb-2 mb-4 hide-scrollbar">
        {[
          { id: 'todos', label: 'Todos los tickets' },
          { id: 'sin_asignar', label: 'Sin Asignar' },
          { id: 'urgentes', label: 'Urgentes 🔥' },
          { id: 'pendientes_cierre', label: 'Pendientes de Cierre ✅' }
        ].map(tab => (
          <button
            key={tab.id}
            onClick={() => setActiveTab(tab.id)}
            className={`whitespace-nowrap px-4 py-2 rounded-full text-sm font-medium transition-all duration-200 border ${
              activeTab === tab.id
                ? 'bg-blue-600 text-white border-blue-600 shadow-md'
                : 'bg-white text-gray-600 border-gray-200 hover:bg-gray-50 hover:border-gray-300'
            }`}
          >
            {tab.label}
          </button>
        ))}
      </div>

      {/* Barra de Filtros */}
      <div className="bg-white dark:bg-gray-800 p-4 rounded-xl shadow-sm border border-gray-200 dark:border-gray-700 mb-8 transition-colors">
        <div className="flex flex-col lg:flex-row gap-4 items-end lg:items-center">
          {/* Búsqueda por texto */}
          <div className="flex-1 w-full relative">
            <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <svg className="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
            </div>
            <input
              type="text"
              placeholder="Buscar por título o descripción..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 shadow-sm sm:text-sm bg-white dark:bg-gray-700 text-gray-900 dark:text-white transition-colors"
            />
          </div>

          {/* Filtro Estado */}
          <div className="w-full lg:w-48">
            <select
              value={filterEstado}
              onChange={(e) => setFilterEstado(e.target.value)}
              className="w-full py-2 px-3 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 shadow-sm sm:text-sm bg-white dark:bg-gray-700 text-gray-900 dark:text-white transition-colors"
            >
              <option value="todos">Todos los estados</option>
              <option value="pendiente">Pendiente</option>
              <option value="en_proceso">En Proceso</option>
              <option value="resuelto">Resuelto</option>
            </select>
          </div>

          {/* Filtro Prioridad */}
          <div className="w-full lg:w-48">
            <select
              value={filterPrioridad}
              onChange={(e) => setFilterPrioridad(e.target.value)}
              className="w-full py-2 px-3 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 shadow-sm sm:text-sm bg-white dark:bg-gray-700 text-gray-900 dark:text-white transition-colors"
            >
              <option value="todas">Todas las prioridades</option>
              <option value="baja">Baja</option>
              <option value="media">Media</option>
              <option value="alta">Alta</option>
              <option value="critica">Crítica</option>
              <option value="por_asignar">Sin Asignar</option>
            </select>
          </div>

          {/* Toggle "Mis asignados" */}
          <div className="w-full lg:w-auto flex items-center shrink-0 lg:pl-2">
            <label className="flex items-center cursor-pointer">
              <div className="relative">
                <input
                  type="checkbox"
                  className="sr-only"
                  checked={showOnlyMine}
                  onChange={(e) => setShowOnlyMine(e.target.checked)}
                />
                <div className={`block w-10 h-6 rounded-full transition-colors ${showOnlyMine ? 'bg-blue-600' : 'bg-gray-300'}`}></div>
                <div className={`absolute left-1 top-1 bg-white w-4 h-4 rounded-full transition-transform ${showOnlyMine ? 'transform translate-x-4' : ''}`}></div>
              </div>
              <span className="ml-3 text-sm font-medium text-gray-700 dark:text-gray-200">Mis asignados</span>
            </label>
          </div>
        </div>
      </div>

      {/* Renderizado Condicional de Resultados */}
      {!tickets || tickets.length === 0 ? (
        <div className="text-center py-16 bg-white rounded-xl border border-gray-200 shadow-sm">
          <svg className="mx-auto h-12 w-12 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
          </svg>
          <h3 className="mt-2 text-sm font-medium text-gray-900">No hay tickets</h3>
          <p className="mt-1 text-sm text-gray-500">Comienza creando un nuevo ticket.</p>
        </div>
      ) : filteredTickets.length === 0 ? (
        <div className="text-center py-16 bg-white rounded-xl border border-gray-200 shadow-sm">
          <svg className="mx-auto h-12 w-12 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
          </svg>
          <h3 className="mt-2 text-sm font-medium text-gray-900">No se encontraron tickets con estos filtros</h3>
          <p className="mt-1 text-sm text-gray-500">Intenta cambiar los criterios de búsqueda.</p>
        </div>
      ) : (
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
          {filteredTickets.map((ticket: TicketWithRelations) => {
            const sla = getTicketSLAStatus(ticket.creado_en, ticket.actualizado_en, ticket.estado);
            
            return (
              <div 
                key={ticket.id} 
                onClick={() => setSelectedTicket(ticket)}
                className={`bg-white rounded-xl shadow-sm border p-5 hover:shadow-md transition-shadow cursor-pointer relative overflow-hidden ${
                  sla.isOverdue ? 'border-red-500 ring-1 ring-red-500' : 'border-gray-200'
                }`}
              >
                <div className="flex justify-between items-start mb-3">
                  <div className="flex flex-col items-start gap-2">
                    <span className={getStatusBadge(ticket.estado)}>
                      {ticket.estado.replace('_', ' ').toUpperCase()}
                    </span>
                    {sla.isOverdue && (
                      <span className="animate-pulse bg-red-600 text-white px-2 py-0.5 rounded text-xs font-bold shadow-sm inline-flex items-center gap-1">
                        ⚠️ SLA VENCIDO
                      </span>
                    )}
                  </div>
                  <span className={`px-2 py-0.5 rounded text-xs font-semibold border ${getPriorityColor(ticket.prioridad)} shrink-0`}>
                    {ticket.prioridad === 'por_asignar' ? 'SIN PRIORIDAD' : ticket.prioridad?.toUpperCase()}
                  </span>
                </div>
                
                <h2 className="text-lg font-bold text-gray-900 mb-2 line-clamp-1">{ticket.titulo}</h2>
                <p className="text-gray-600 text-sm mb-4 line-clamp-3">{ticket.descripcion}</p>
                
                <div className="flex items-center justify-between mt-auto pt-4 border-t border-gray-100">
                  <div className="flex items-center mr-2 min-w-0">
                    <div className="h-8 w-8 rounded-full bg-blue-100 flex items-center justify-center text-blue-700 font-bold text-xs shrink-0">
                      {ticket.creador?.nombre?.charAt(0).toUpperCase() || '?'}
                    </div>
                    <span className="ml-2 text-sm font-medium text-gray-700 truncate">
                      {ticket.creador?.nombre || 'Usuario Desconocido'}
                    </span>
                  </div>
                  <div className={`flex items-center px-2 py-1 rounded text-xs font-medium border shrink-0 ${sla.badgeClass}`}>
                    <svg className="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                    {sla.text}
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      )}

      {selectedTicket && (
        <TicketModal 
          ticket={selectedTicket} 
          onClose={() => setSelectedTicket(null)} 
        />
      )}
    </div>
  );
};
