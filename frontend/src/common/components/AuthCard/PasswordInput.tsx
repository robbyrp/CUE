import { useState } from 'react';
import type { InputHTMLAttributes } from 'react';
import { Eye, EyeOff, Lock } from 'lucide-react';
import styles from './AuthCard.module.scss';

type PasswordInputProps = Omit<InputHTMLAttributes<HTMLInputElement>, 'type'>;

export default function PasswordInput(props: PasswordInputProps) {
    const [showPassword, setShowPassword] = useState(false);

    return (
        <div className={styles.inputWrapper}>
            <Lock className={styles.inputIcon} size={20} />
            <input {...props} type={showPassword ? 'text' : 'password'} />
            <button
                type="button"
                className={styles.togglePassword}
                onClick={() => setShowPassword((prev) => !prev)}
                aria-label={showPassword ? 'Ascunde parola' : 'Arată parola'}
            >
                {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
            </button>
        </div>
    );
}
