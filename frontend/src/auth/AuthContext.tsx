import { createContext, useContext, useState, useEffect } from 'react';
import type { ReactNode } from 'react';
import type { LoginRequest, LoginResponse } from '../types/LoginInterfaces';
import type { RegisterUserRequest } from '../types/RegisterUserRequest';
import type { UserRole } from '../types/UserRole';
import { LOCAL_STORAGE_TOKEN_NAME } from '../services/Api';
import { AuthService } from '../services/AuthService';
import { decodeUserRole } from './jwt';

interface AuthContextType {
    isAuthenticated: boolean;
    role: UserRole | null;
    isAdmin: boolean;
    login: (request: LoginRequest) => Promise<void>
    register: (request: RegisterUserRequest) => Promise<void>;
    logout: () => void;
    loading: boolean;
}

interface AuthProviderProps { children: ReactNode; }

const AuthContext = createContext<AuthContextType | null>(null);

const AuthProvider = ({ children }: AuthProviderProps) => {

    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);

    const [role, setRole] = useState<UserRole | null>(null);

    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        const savedToken = localStorage.getItem(LOCAL_STORAGE_TOKEN_NAME);
        if (savedToken) {
            setIsAuthenticated(true);
            setRole(decodeUserRole(savedToken));
        }
        setLoading(false);
    }, []);

    const saveSession = (response: LoginResponse) => {
        localStorage.setItem(LOCAL_STORAGE_TOKEN_NAME, response.token);
        setIsAuthenticated(true);
        setRole(decodeUserRole(response.token));
    };

    const login = async (request: LoginRequest): Promise<void> => {
        saveSession(await AuthService.login(request));
    };

    const register = async (request: RegisterUserRequest): Promise<void> => {
        saveSession(await AuthService.register(request));
    };

    const logout = () => {
        localStorage.removeItem(LOCAL_STORAGE_TOKEN_NAME);
        setIsAuthenticated(false);
        setRole(null);
    };

    return (
        <AuthContext.Provider
            value={{ isAuthenticated, role, isAdmin: role === 'ADMIN', login, register, logout, loading }}>
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
