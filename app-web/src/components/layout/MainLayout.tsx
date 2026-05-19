import { useState, useEffect } from 'react';
import { Outlet, Link, useNavigate } from 'react-router-dom';
import { useUIStore } from '../../store/useUIStore';
import { supabase } from '../../shared/api/supabase/client';
import { useSupabase } from '../../app/providers/SupabaseProvider';
import { useTicketNotifications } from '../../hooks/useTicketNotifications';
import { useNotificationStore } from '../../store/useNotificationStore';
import { useTicketsRealtime } from '../../hooks/useTicketsRealtime';

export const MainLayout = () => {
  const { isSidebarOpen, toggleSidebar, theme, setTheme } = useUIStore();
  const { session, perfil, isLoading } = useSupabase();
  const navigate = useNavigate();

  const { notificaciones, unreadCount, markAllAsRead, clearNotifications } = useNotificationStore();
  const [isNotifOpen, setIsNotifOpen] = useState(false);

  useTicketNotifications(session?.user?.id);
  useTicketsRealtime();

  // Motor de Control de Acceso Basado en Roles (RBAC) con protección de rutas técnicas
  useEffect(() => {
    if (!isLoading && perfil) {
      if (perfil.rol === 'empleado' || (perfil.rol === 'admin' && perfil.departamento === 'Recursos Humanos')) {
        navigate('/mis-tickets', { replace: true });
      }
    }
  }, [isLoading, perfil, navigate]);

  // Inicialización y persistencia local del modo oscuro (Client-Side)
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

  const handleNotificationClick = (ticketId: number | null) => {
    setIsNotifOpen(false);
    if (ticketId) {
      navigate('/tickets', { state: { openTicketId: ticketId } });
    }
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

  return (
    <div className="flex h-screen bg-gray-50 dark:bg-gray-900 text-gray-900 dark:text-gray-100 overflow-hidden transition-colors duration-200">
      {/* Sidebar */}
      <aside
        className={`fixed inset-y-0 left-0 z-50 w-64 bg-gray-800 dark:bg-gray-900 border-r border-transparent dark:border-gray-800 text-white transform transition-transform duration-300 ease-in-out lg:relative lg:translate-x-0 ${isSidebarOpen ? 'translate-x-0' : '-translate-x-full'
          }`}
      >
        <div className="flex items-center justify-between h-16 px-6 bg-gray-900 dark:bg-gray-950 border-b border-transparent dark:border-gray-800">
          <span className="text-xl font-bold">CorpSync ITSM</span>
          <button
            onClick={toggleSidebar}
            className="lg:hidden text-gray-300 hover:text-white focus:outline-none"
            aria-label="Cerrar menú"
          >
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <div className="flex flex-col flex-1 h-[calc(100vh-4rem)] justify-between">
          <nav className="p-4 space-y-2">
            <Link
              to="/"
              onClick={() => { if (window.innerWidth < 1024) toggleSidebar(); }}
              className="block px-4 py-2 rounded transition-colors hover:bg-gray-700 hover:text-white"
            >
              Dashboard
            </Link>
            <Link
              to="/tickets"
              onClick={() => { if (window.innerWidth < 1024) toggleSidebar(); }}
              className="block px-4 py-2 rounded transition-colors hover:bg-gray-700 hover:text-white"
            >
              Tickets
            </Link>
          </nav>

          <div className="p-4 border-t border-gray-700">
            <button
              onClick={toggleTheme}
              className="flex items-center gap-3 w-full px-4 py-2 text-sm text-gray-300 hover:text-white hover:bg-gray-700 rounded transition-colors"
            >
              {theme === 'dark' ? (
                <>
                  <span className="text-lg">☀️</span> Modo Claro
                </>
              ) : (
                <>
                  <span className="text-lg">🌙</span> Modo Oscuro
                </>
              )}
            </button>
          </div>
        </div>
      </aside>

      {/* Main Content Area */}
      <div className="flex flex-col flex-1 min-w-0 overflow-hidden">
        {/* Header */}
        <header className="flex items-center justify-between h-16 px-6 bg-white dark:bg-gray-800 border-b border-gray-200 dark:border-gray-700 transition-colors z-30">
          <button
            onClick={toggleSidebar}
            className="text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200 focus:outline-none lg:hidden"
            aria-label="Abrir menú"
          >
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
            </svg>
          </button>

          <div className="flex-1"></div> {/* Spacer */}

          <div className="flex items-center gap-6">
            {/* Notificaciones */}
            <div className="relative">
              <button
                onClick={toggleNotifications}
                className="relative text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200 focus:outline-none transition-colors"
              >
                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" />
                </svg>
                {unreadCount > 0 && (
                  <span className="absolute -top-1 -right-1 flex h-4 w-4 items-center justify-center rounded-full bg-red-500 text-[10px] font-bold text-white border-2 border-white dark:border-gray-800">
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
                          onClick={() => handleNotificationClick(notif.ticket_id)}
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

            {/* Usuario */}
            <div className="flex items-center gap-3 border-l border-gray-200 dark:border-gray-700 pl-6">
              <span className="text-sm font-medium text-gray-700 dark:text-gray-200">
                {perfil?.nombre ? perfil.nombre.split(' ')[0] : 'Usuario'}
              </span>
              <button
                onClick={handleLogout}
                className="text-sm font-medium text-red-600 dark:text-red-400 hover:text-red-800 dark:hover:text-red-300 focus:outline-none transition-colors"
              >
                Salir
              </button>
            </div>
          </div>
        </header>

        {/* Content Outlet */}
        <main className="flex-1 overflow-y-auto p-6 dark:bg-gray-900 dark:text-white transition-colors relative z-0">
          <Outlet />
        </main>
      </div>

      {/* Overlay for mobile sidebar */}
      {isSidebarOpen && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 z-40 lg:hidden"
          onClick={toggleSidebar}
          aria-hidden="true"
        ></div>
      )}
    </div>
  );
};
