import { createContext, useContext, useState, useEffect } from 'react';
import type { ReactNode } from 'react';
import type { LoginRequest } from '../types/LoginInterfaces';
import { LOCAL_STORAGE_TOKEN_NAME } from '../services/Api';
import { AuthService } from '../services/AuthService';

interface AuthContextType {
    isAuthenticated: boolean;
    login: (request: LoginRequest) => Promise<void>
    logout: () => void;
    loading: boolean;
}

interface AuthProviderProps { children: ReactNode; }

const AuthContext = createContext<AuthContextType | null>(null);

const AuthProvider = ({ children }: AuthProviderProps) => {

    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);

    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        const savedToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_NAME);
        if (savedToken) {
            setIsAuthenticated(true);
        }
        setLoading(false);
    }, []);

    const login = async (request: LoginRequest): Promise<void> => {
        const response = await AuthService.login(request);
        localStorage.setItem(LOCAL_STORAGE_TOKEN_NAME, response.token);
        setIsAuthenticated(true);
    };

    const logout = () => {
        localStorage.removeItem(LOCAL_STORAGE_TOKEN_NAME);
        setIsAuthenticated(false);
    };

    return (
        <AuthContext.Provider
            value={{ isAuthenticated, login, logout, loading }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const ctx = useContext(AuthContext);
    if (!ctx) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return ctx;
};

export default AuthProvider;
