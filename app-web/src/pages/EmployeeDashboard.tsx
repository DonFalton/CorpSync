import React, { useState, useEffect, useCallback } from 'react';
import { supabase } from '../shared/api/supabase/client';
import { useSupabase } from '../app/providers/SupabaseProvider';
import { NewTicketModal } from '../components/tickets/NewTicketModal';
import { useUIStore } from '../store/useUIStore';
import { useTicketNotifications } from '../hooks/useTicketNotifications';
import { useNotificationStore } from '../store/useNotificationStore';

export const EmployeeDashboard: React.FC = () => {
  const { session, perfil } = useSupabase();
  const { theme, setTheme } = useUIStore();
  const [tickets, setTickets] = useState<any[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  
  const { notificaciones, unreadCount, markAllAsRead, clearNotifications } = useNotificationStore();
  const [isNotifOpen, setIsNotifOpen] = useState(false);

  // Iniciar escucha de notificaciones para este empleado
  useTicketNotifications(session?.user?.id);
  useEffect(() => {
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme === 'dark' || (!savedTheme && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
      setTheme('dark');
      document.documentElement.classList.add('dark');
    } else {
      setTheme('light');
      document.documentElement.classList.remove('dark');
    }
  }, [setTheme]);

  const toggleTheme = () => {
    if (theme === 'light') {
      setTheme('dark');
      document.documentElement.classList.add('dark');
      localStorage.setItem('theme', 'dark');
    } else {
      setTheme('light');
      document.documentElement.classList.remove('dark');
      localStorage.setItem('theme', 'light');
    }
  };

  const handleLogout = async () => {
    clearNotifications();
    await supabase.auth.signOut();
  };

  const toggleNotifications = async () => {
    if (!isNotifOpen) {
      markAllAsRead();
      if (session?.user?.id) {
        try {
          await supabase.from('notificaciones')
            .update({ leida: true })
            .eq('perfil_id', session.user.id);
        } catch (err) {
          console.error('Error updating notifications in DB:', err);
        }
      }
    }
    setIsNotifOpen(!isNotifOpen);
  };

  const fetchTickets = useCallback(async () => {
    if (!session?.user?.id) return;
    setIsLoading(true);
    try {
      const { data, error } = await supabase
        .from('tickets')
        .select('*')
        .eq('empleado_id', session.user.id)
        .order('creado_en', { ascending: false });

      if (error) throw error;
      setTickets(data || []);
    } catch (error) {
      console.error('Error fetching tickets:', error);
    } finally {
      setIsLoading(false);
    }
  }, [session?.user?.id]);

  useEffect(() => {
    fetchTickets();
  }, [fetchTickets]);

  const getStatusBadge = (estado: string) => {
    const baseClasses = "px-2.5 py-0.5 rounded-full text-xs font-medium";
    switch (estado?.toLowerCase()) {
      case 'urgente':
      case 'crítico':
        return `${baseClasses} bg-red-100 text-red-800 border border-red-200`;
      case 'pendiente':
        return `${baseClasses} bg-purple-100 text-purple-800 border border-purple-200`;
      case 'en_proceso':
      case 'en proceso':
        return `${baseClasses} bg-yellow-100 text-yellow-800 border border-yellow-200`;
      case 'resuelto':
        return `${baseClasses} bg-green-100 text-green-800 border border-green-200`;
      default:
        return `${baseClasses} bg-gray-100 text-gray-800 border border-gray-200`;
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 transition-colors duration-200">
      {/* Header */}
      <header className="bg-white dark:bg-gray-800 border-b border-gray-200 dark:border-gray-700 sticky top-0 z-30 transition-colors">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16 items-center">
            <div className="flex items-center gap-4">
              <span className="text-xl font-bold text-gray-900 dark:text-white">CorpSync ITSM</span>
              <span className="hidden sm:block text-sm text-gray-500 dark:text-gray-400">Portal de Empleado</span>
            </div>
            
            <div className="flex items-center gap-4">
              {/* Notificaciones */}
              <div className="relative">
                <button 
                  onClick={toggleNotifications}
                  className="relative p-2 text-gray-500 hover:text-gray-900 dark:text-gray-400 dark:hover:text-white rounded-lg transition-colors focus:outline-none"
                >
                  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" />
                  </svg>
                  {unreadCount > 0 && (
                    <span className="absolute top-1 right-1 flex h-4 w-4 items-center justify-center rounded-full bg-red-500 text-[10px] font-bold text-white border-2 border-white dark:border-gray-800">
                      {unreadCount > 9 ? '9+' : unreadCount}
                    </span>
                  )}
                </button>

                {/* Dropdown Notificaciones */}
                {isNotifOpen && (
                  <div className="absolute right-0 mt-2 w-80 bg-white dark:bg-gray-800 rounded-lg shadow-xl border border-gray-100 dark:border-gray-700 overflow-hidden z-50">
                    <div className="p-4 border-b border-gray-100 dark:border-gray-700 bg-gray-50 dark:bg-gray-900">
                      <h3 className="font-semibold text-gray-800 dark:text-gray-100">Notificaciones</h3>
                    </div>
                    <div className="max-h-96 overflow-y-auto">
                      {notificaciones.length === 0 ? (
                        <div className="p-4 text-center text-sm text-gray-500 dark:text-gray-400">
                          No tienes notificaciones recientes.
                        </div>
                      ) : (
                        notificaciones.map(notif => (
                          <div 
                            key={notif.id}
                            onClick={() => setIsNotifOpen(false)}
                            className={`p-4 border-b border-gray-100 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-700 cursor-pointer transition-colors ${!notif.leida ? 'bg-blue-50 dark:bg-gray-700/50' : ''}`}
                          >
                            <p className="font-medium text-sm text-gray-900 dark:text-white">{notif.titulo}</p>
                            <p className="text-sm text-gray-600 dark:text-gray-300 mt-1 line-clamp-2">{notif.mensaje}</p>
                            <p className="text-xs text-gray-400 dark:text-gray-500 mt-2">
                              {new Date(notif.fecha).toLocaleString()}
                            </p>
                          </div>
                        ))
                      )}
                    </div>
                  </div>
                )}
              </div>

              <div className="h-6 w-px bg-gray-300 dark:bg-gray-600 hidden sm:block"></div>

              <span className="text-sm font-medium text-gray-700 dark:text-gray-200 hidden sm:block">
                Hola, {perfil?.nombre ? perfil.nombre.split(' ')[0] : 'Usuario'}
              </span>
              
              <button
                onClick={toggleTheme}
                className="p-2 text-gray-500 hover:text-gray-900 dark:text-gray-400 dark:hover:text-white rounded-lg transition-colors"
                aria-label="Alternar modo oscuro"
              >
                {theme === 'dark' ? '☀️' : '🌙'}
              </button>

              <div className="h-6 w-px bg-gray-300 dark:bg-gray-600"></div>

              <button
                onClick={handleLogout}
                className="text-sm font-medium text-red-600 dark:text-red-400 hover:text-red-800 dark:hover:text-red-300 transition-colors"
              >
                Salir
              </button>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="flex justify-between items-center mb-8 flex-wrap gap-4">
          <div>
            <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Mis Tickets</h1>
            <p className="text-sm text-gray-500 dark:text-gray-400 mt-1">
              Gestiona y revisa el estado de tus solicitudes
            </p>
          </div>
          <button
            onClick={() => setIsModalOpen(true)}
            className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg font-medium shadow-sm transition-colors focus:ring-4 focus:ring-blue-300"
          >
            + Crear Nuevo Ticket
          </button>
        </div>

        {isLoading ? (
          <div className="flex justify-center items-center h-64">
            <div className="text-gray-500 dark:text-gray-400 animate-pulse">Cargando tus tickets...</div>
          </div>
        ) : tickets.length === 0 ? (
          <div className="bg-white dark:bg-gray-800 rounded-xl p-8 text-center shadow-sm border border-gray-200 dark:border-gray-700 transition-colors">
            <h3 className="text-lg font-medium text-gray-900 dark:text-white mb-2">No tienes tickets</h3>
            <p className="text-gray-500 dark:text-gray-400 mb-6">
              Parece que aún no has creado ningún ticket. Si tienes algún problema, ¡crea uno nuevo!
            </p>
            <button
              onClick={() => setIsModalOpen(true)}
              className="text-blue-600 dark:text-blue-400 font-medium hover:underline"
            >
              Crear tu primer ticket
            </button>
          </div>
        ) : (
          <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
            {tickets.map((ticket) => (
              <div
                key={ticket.id}
                className="bg-white dark:bg-gray-800 rounded-xl p-5 shadow-sm border border-gray-200 dark:border-gray-700 flex flex-col transition-colors hover:shadow-md"
              >
                <div className="flex justify-between items-start mb-3">
                  <span className={`text-xs font-semibold text-gray-500 dark:text-gray-400`}>
                    #{ticket.id}
                  </span>
                  <span className={getStatusBadge(ticket.estado)}>
                    {ticket.estado}
                  </span>
                </div>
                
                <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-2 line-clamp-1">
                  {ticket.titulo}
                </h3>
                
                <p className="text-sm text-gray-600 dark:text-gray-300 mb-4 line-clamp-3 flex-1">
                  {ticket.descripcion}
                </p>
                
                <div className="flex items-center justify-between mt-auto pt-4 border-t border-gray-100 dark:border-gray-700">
                  <span className="text-xs text-gray-500 dark:text-gray-400">
                    {new Date(ticket.creado_en).toLocaleDateString()}
                  </span>
                  {ticket.categoria && (
                    <span className="text-xs bg-gray-100 dark:bg-gray-700 text-gray-600 dark:text-gray-300 px-2 py-1 rounded">
                      {ticket.categoria}
                    </span>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </main>

      <NewTicketModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onTicketCreated={fetchTickets}
      />
    </div>
  );
};
