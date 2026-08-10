import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import styles from './LoginPage.module.scss';

function LoginPage() {
    const [userId, setUserId] = useState('');
    const [error, setError] = useState('');
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        const trimmedUserId = userId.trim();

        if (!trimmedUserId) {
            setError('Please enter a user ID.');
            return;
        }

        login(trimmedUserId);
        navigate('/profil/vizualizare');
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
                        <h1 id="login-title" className={styles.title}>
                            Sign In
                        </h1>

                        <div className={styles.formGroup}>
                            <label htmlFor="userId">User ID</label>
                            <input
                                id="userId"
                                name="userId"
                                type="number"
                                min="1"
                                value={userId}
                                onChange={(event) => {
                                    setUserId(event.target.value);
                                    setError('');
                                }}
                                placeholder="Enter your user ID"
                            />
                        </div>

                        {error && <p className={styles.errorMessage}>{error}</p>}

                        <button className={styles.signInButton} type="submit">
                            Sign In
                        </button>
                    </form>
                </div>
            </section>
        </main>
    );
}

export default LoginPage;