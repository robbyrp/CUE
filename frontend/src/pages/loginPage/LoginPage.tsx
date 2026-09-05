import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../auth/AuthContext';
import styles from './LoginPage.module.scss';
import type { LoginRequest } from '../../types/LoginInterfaces';

function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const {login ,isAuthenticated, loading} = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (!loading && isAuthenticated) {
            navigate('/profil/vizualizare')
        }
    }, [isAuthenticated, loading, navigate]);
    
    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        try {
            const request: LoginRequest = { username, password };
            await login(request);
            navigate('/profil/vizualizare');
        } catch (error) {
            setError("Incorrect credentials!");
        }
    };

    return (
        <main className={styles.loginPage}>
            <section className={styles.loginPanel} aria-labelledby="login-title">
                <p className={styles.pageLabel}>sign in</p>

                <div className={styles.loginContent}>
                    <div className={styles.logoBox} aria-hidden="true">
                        <div className={styles.logoIcon} />
                    </div>

                    <form className={styles.loginForm} onSubmit={handleSubmit}>

                        <div className={styles.formGroup}>
                            <label htmlFor="username">Username</label>
                            <input
                                type="text"
                                id="username"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                            />

                            <label htmlFor="password">Password</label>
                             <input
                                type="password"
                                id="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />

                            <h2 id="login-title" className={styles.title}>
                                Password
                            </h2>
                        </div>

                        {error && <p className={styles.errorMessage}>{error}</p>}

                        <button className={styles.signInButton} type="submit">
                            Autentificare
                        </button>
                    </form>
                </div>
            </section>
        </main>
    );
}

export default LoginPage;