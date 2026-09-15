import type { ReactNode } from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from './AuthContext';
import { ROUTES } from '../utils/constants';

interface AdminRouteProps {
    children: ReactNode;
}

const AdminRoute = ({ children }: AdminRouteProps) => {
    const { isAdmin, loading } = useAuth();

    if (loading) {
        return null;
    }

    if (!isAdmin) {
        return <Navigate to={ROUTES.HOME} replace />;
    }

    return children;
};

export default AdminRoute;
