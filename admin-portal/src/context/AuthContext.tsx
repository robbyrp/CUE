import {createContext, useContext, useState, useEffect} from 'react';
import type {ReactNode} from 'react';
interface AuthContextType {
    userId: string | null;
    login: (id: string) => void;
    logout: () => void;
    isAuthenticated: boolean;
    loading: boolean;
}


interface AuthProviderProps { children: ReactNode; }

const AuthContext = createContext<AuthContextType | null> (null);

const AuthProvider = ({children} : AuthProviderProps) => {
    const [userId, setUserId] = useState<string | null>(null);

    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        const saveUserId = localStorage.getItem('userId');
        if (saveUserId) {
            setUserId(saveUserId);
        }
        setLoading(false);
    }, []);

    const login = (id :string) => {
        localStorage.setItem('userId', id);
        setUserId(id.toString());
    };

    const logout = () => {
        localStorage.removeItem('userId');
        setUserId(null);
    };

    return (
        <AuthContext.Provider
            value={{ userId, login, logout,
        isAuthenticated: !!userId, loading }}>
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