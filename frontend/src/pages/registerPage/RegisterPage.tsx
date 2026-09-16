import { useEffect, useState } from 'react';
import type { ChangeEvent, FormEvent } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { isAxiosError } from 'axios';
import { Mail, MapPin, User } from 'lucide-react';
import { useAuth } from '../../auth/AuthContext';
import AuthCard from '../../common/components/AuthCard/AuthCard';
import PasswordInput from '../../common/components/AuthCard/PasswordInput';
import styles from '../../common/components/AuthCard/AuthCard.module.scss';
import type { RegisterUserRequest } from '../../types/RegisterUserRequest';
import { ROUTES } from '../../utils/constants';

const EMPTY_FORM: RegisterUserRequest = {
    username: '',
    password: '',
    firstName: '',
    lastName: '',
    email: '',
    city: '',
};

const NAME_PATTERN = "\\p{L}+([ '\\-]\\p{L}+)*";
const CITY_PATTERN = '\\p{L}+([ \\-]\\p{L}+)*';

function RegisterPage() {
    const [form, setForm] = useState<RegisterUserRequest>(EMPTY_FORM);
    const [error, setError] = useState('');
    const [submitting, setSubmitting] = useState(false);

    const { register, isAuthenticated, loading } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (!loading && isAuthenticated) {
            navigate(ROUTES.PROFIL);
        }
    }, [isAuthenticated, loading, navigate]);

    const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
        const { name, value } = event.target;
        setForm((prev) => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setError('');
        setSubmitting(true);

        try {
            const request: RegisterUserRequest = {
                ...form,
                username: form.username.trim(),
                firstName: form.firstName.trim(),
                lastName: form.lastName.trim(),
                email: form.email.trim(),
                city: form.city.trim(),
            };
            await register(request);
            navigate(ROUTES.PROFIL);
        } catch (err) {
            if (isAxiosError(err) && err.response?.status === 409) {
                setError('Acest nume de utilizator este deja folosit.');
            } else if (isAxiosError(err) && err.response?.status === 400) {
                setError('Unele date nu sunt valide. Verifică toate câmpurile.');
            } else {
                setError('A apărut o eroare. Încearcă din nou.');
            }
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <AuthCard
            titleId="register-title"
            title="ÎNREGISTRARE"
            subtitle="Creează-ți un cont ca să urmărești spectacolele preferate."
            wide
        >
            <form className={styles.loginForm} onSubmit={handleSubmit}>
                <div className={styles.fieldRow}>
                    <div className={styles.field}>
                        <label htmlFor="firstName">Prenume</label>
                        <div className={styles.inputWrapper}>
                            <input
                                className={styles.noIcon}
                                type="text"
                                id="firstName"
                                name="firstName"
                                autoComplete="given-name"
                                pattern={NAME_PATTERN}
                                title="Folosește doar litere, spații sau cratimă."
                                value={form.firstName}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </div>

                    <div className={styles.field}>
                        <label htmlFor="lastName">Nume</label>
                        <div className={styles.inputWrapper}>
                            <input
                                className={styles.noIcon}
                                type="text"
                                id="lastName"
                                name="lastName"
                                autoComplete="family-name"
                                pattern={NAME_PATTERN}
                                title="Folosește doar litere, spații sau cratimă."
                                value={form.lastName}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </div>
                </div>

                <div className={styles.field}>
                    <label htmlFor="username">Nume utilizator</label>
                    <div className={styles.inputWrapper}>
                        <User className={styles.inputIcon} size={20} />
                        <input
                            type="text"
                            id="username"
                            name="username"
                            autoComplete="username"
                            pattern="\S{3,}"
                            title="Minim 3 caractere, fără spații."
                            value={form.username}
                            onChange={handleChange}
                            required
                        />
                    </div>
                </div>

                <div className={styles.field}>
                    <label htmlFor="email">Email</label>
                    <div className={styles.inputWrapper}>
                        <Mail className={styles.inputIcon} size={20} />
                        <input
                            type="email"
                            id="email"
                            name="email"
                            autoComplete="email"
                            value={form.email}
                            onChange={handleChange}
                            required
                        />
                    </div>
                </div>

                <div className={styles.field}>
                    <label htmlFor="city">Oraș</label>
                    <div className={styles.inputWrapper}>
                        <MapPin className={styles.inputIcon} size={20} />
                        <input
                            type="text"
                            id="city"
                            name="city"
                            autoComplete="address-level2"
                            pattern={CITY_PATTERN}
                            title="Introdu un nume de oraș valid (ex: Cluj-Napoca)."
                            value={form.city}
                            onChange={handleChange}
                            required
                        />
                    </div>
                </div>

                <div className={styles.field}>
                    <label htmlFor="password">Parolă</label>
                    <PasswordInput
                        id="password"
                        name="password"
                        autoComplete="new-password"
                        minLength={8}
                        title="Minim 8 caractere."
                        value={form.password}
                        onChange={handleChange}
                        required
                    />
                </div>

                {error && <p className={styles.errorMessage}>{error}</p>}

                <button className={styles.signInButton} type="submit" disabled={submitting}>
                    {submitting ? 'Se creează contul...' : 'Creează cont'}
                </button>
            </form>

            <p className={styles.switchAuth}>
                Ai deja cont? <Link to={ROUTES.LOGIN}>Autentifică-te</Link>
            </p>
        </AuthCard>
    );
}

export default RegisterPage;
