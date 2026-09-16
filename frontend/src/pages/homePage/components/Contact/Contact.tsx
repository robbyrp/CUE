import styles from './Contact.module.scss';

export default function Contact() {
    return (
        <div className={styles.contact}>
            <div className={styles.centeredContent}>
                <h2 className={styles.title}>CONTACT</h2>
                <div className={styles.contactInfo}>
                    <p>Email: <a href="mailto:robertionutpana@gmail.com">robertionutpana@gmail.com</a></p>
                    <p>GitHub: <a href="https://github.com/robbyrp" target="_blank" rel="noreferrer">github.com/robbyrp</a></p>
                    <p>Address: BUCHAREST, ROMANIA</p>
                </div>
            </div>
        </div>
    );
}
