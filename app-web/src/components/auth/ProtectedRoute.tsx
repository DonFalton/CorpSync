import { Navigate, Outlet } from 'react-router-dom';
import { useSupabase } from '../../app/providers/SupabaseProvider';

export const ProtectedRoute = () => {
  // Suscripción al proveedor de contexto para validación de tokens JWT
  const { session, isLoading } = useSupabase();

  if (isLoading) {
    return (
      <div className="flex h-screen items-center justify-center bg-gray-100">
        <div className="text-xl font-semibold text-gray-700 animate-pulse">
          Cargando sesión...
        </div>
      </div>
    );
  }

  if (!session) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
};
