import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { User } from 'lucide-react';
import { useAuth } from '../../auth/AuthContext';
import AuthCard from '../../common/components/AuthCard/AuthCard';
import PasswordInput from '../../common/components/AuthCard/PasswordInput';
import styles from '../../common/components/AuthCard/AuthCard.module.scss';
import type { LoginRequest } from '../../types/LoginInterfaces';
import { ROUTES } from '../../utils/constants';

function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const { login, isAuthenticated, loading } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (!loading && isAuthenticated) {
            navigate(ROUTES.PROFIL);
        }
    }, [isAuthenticated, loading, navigate]);

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        try {
            const request: LoginRequest = { username, password };
            await login(request);
            navigate(ROUTES.PROFIL);
        } catch (error) {
            setError('Nume de utilizator sau parolă incorecte.');
        }
    };

    return (
        <AuthCard
            titleId="login-title"
            title="AUTENTIFICARE"
            subtitle="Bine ai revenit! Intră în cont ca să continui."
        >
            <form className={styles.loginForm} onSubmit={handleSubmit}>
                <div className={styles.field}>
                    <label htmlFor="username">Nume utilizator</label>
                    <div className={styles.inputWrapper}>
                        <User className={styles.inputIcon} size={20} />
                        <input
                            type="text"
                            id="username"
                            autoComplete="username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>
                </div>

                <div className={styles.field}>
                    <label htmlFor="password">Parolă</label>
                    <PasswordInput
                        id="password"
                        autoComplete="current-password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>

                {error && <p className={styles.errorMessage}>{error}</p>}

                <button className={styles.signInButton} type="submit">
                    Autentificare
                </button>
            </form>

            <p className={styles.switchAuth}>
                Nu ai cont? <Link to={ROUTES.REGISTER}>Înregistrează-te</Link>
            </p>
        </AuthCard>
    );
}

export default LoginPage;
