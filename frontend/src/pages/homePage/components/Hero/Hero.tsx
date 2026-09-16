import { Link } from 'react-router-dom';
import styles from './Hero.module.scss';
import { ROUTES } from '../../../../utils/constants';

export default function Hero() {
    return (
        <div className={styles.hero}>
            <div className={styles.text}>
                <div className={styles.description}>Tot ce se joacă. Tot ce s-a jucat. Tot ce urmează.</div>
                <div className={styles.siteName}>CUE</div>
            </div>
            <Link to={ROUTES.REGISTER} className={styles.CTA}>
                INSCRIE-TE ACUM
            </Link>
        </div>
    );
}
