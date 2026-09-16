import type { ReactNode } from 'react';
import styles from './AuthCard.module.scss';
import LogoIcon from '../Header/assets/logoIcon.svg';

interface AuthCardProps {
    titleId: string;
    title: string;
    subtitle: string;
    wide?: boolean;
    children: ReactNode;
}

export default function AuthCard({ titleId, title, subtitle, wide = false, children }: AuthCardProps) {
    return (
        <main className={styles.loginPage}>
            <section
                className={`${styles.loginCard} ${wide ? styles.wide : ''}`}
                aria-labelledby={titleId}
            >
                <div className={styles.brand}>
                    <img src={LogoIcon} alt="" className={styles.logo} />
                    <span className={styles.brandName}>CUE</span>
                </div>

                <div className={styles.header}>
                    <div className={styles.titleLine} />
                    <h1 id={titleId} className={styles.title}>{title}</h1>
                    <div className={styles.titleLine} />
                </div>
                <p className={styles.subtitle}>{subtitle}</p>

                {children}
            </section>
        </main>
    );
}
