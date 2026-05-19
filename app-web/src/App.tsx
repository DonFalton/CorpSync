import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { SupabaseProvider } from './app/providers/SupabaseProvider';
import { ProtectedRoute } from './components/auth/ProtectedRoute';
import { Login } from './pages/Login';
import { Dashboard } from './pages/Dashboard';
import { Tickets } from './pages/Tickets';
import { CreateTicket } from './pages/CreateTicket';
import { MainLayout } from './components/layout/MainLayout';
import { Toaster } from 'react-hot-toast';
import './App.css';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 60 * 1000,
      refetchOnWindowFocus: false,
      retry: 1,
    },
  },
});

import { EmployeeDashboard } from './pages/EmployeeDashboard';
import { Navigate } from 'react-router-dom';
import { useSupabase } from './app/providers/SupabaseProvider';

const IndexRedirect = () => {
  const { perfil } = useSupabase();
  
  if (perfil?.rol === 'empleado' || (perfil?.rol === 'admin' && perfil?.departamento === 'Recursos Humanos')) {
    return <Navigate to="/mis-tickets" replace />;
  }
  return <Navigate to="/dashboard" replace />;
};

const router = createBrowserRouter([
  {
    path: '/login',
    element: <Login />,
  },
  {
    element: <ProtectedRoute />,
    children: [
      {
        path: '/',
        element: <IndexRedirect />,
      },
      {
        path: '/mis-tickets',
        element: <EmployeeDashboard />,
      },
      {
        element: <MainLayout />,
        children: [
          {
            path: '/dashboard',
            element: <Dashboard />,
          },
          {
            path: '/tickets',
            element: <Tickets />,
          },
          {
            path: '/tickets/new',
            element: <CreateTicket />,
          },
        ],
      },
    ],
  },
]);

function App() {
  return (
    <SupabaseProvider>
      <QueryClientProvider client={queryClient}>
        <RouterProvider router={router} />
        <Toaster position="bottom-right" />
      </QueryClientProvider>
    </SupabaseProvider>
  );
}

export default App;
