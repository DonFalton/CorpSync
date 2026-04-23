import { Outlet, Link } from 'react-router-dom';
import { useUIStore } from '../../store/useUIStore';
import { supabase } from '../../shared/api/supabase/client';

export const MainLayout = () => {
  const { isSidebarOpen, toggleSidebar } = useUIStore();

  const handleLogout = async () => {
    await supabase.auth.signOut();
  };

  return (
    <div className="flex h-screen bg-gray-50 overflow-hidden">
      {/* Sidebar */}
      <aside
        className={`fixed inset-y-0 left-0 z-50 w-64 bg-gray-800 text-white transform transition-transform duration-300 ease-in-out lg:relative lg:translate-x-0 ${
          isSidebarOpen ? 'translate-x-0' : '-translate-x-full'
        }`}
      >
        <div className="flex items-center justify-between h-16 px-6 bg-gray-900">
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
      </aside>

      {/* Main Content Area */}
      <div className="flex flex-col flex-1 min-w-0 overflow-hidden">
        {/* Header */}
        <header className="flex items-center justify-between h-16 px-6 bg-white border-b border-gray-200">
          <button
            onClick={toggleSidebar}
            className="text-gray-500 hover:text-gray-700 focus:outline-none lg:hidden"
            aria-label="Abrir menú"
          >
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
            </svg>
          </button>

          <div className="flex-1"></div> {/* Spacer */}

          <button
            onClick={handleLogout}
            className="text-sm font-medium text-red-600 hover:text-red-800 focus:outline-none transition-colors"
          >
            Cerrar Sesión
          </button>
        </header>

        {/* Content Outlet */}
        <main className="flex-1 overflow-y-auto p-6">
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
