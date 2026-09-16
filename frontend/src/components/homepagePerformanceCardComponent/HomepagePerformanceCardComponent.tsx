import styles from './HomepagePerformanceCardComponent.module.scss';
import { useNavigate } from 'react-router-dom';

interface HomepagePerformanceCardComponentProps {
    id: number;
    title: string;
    coverImageURL: string;
}

function HomepagePerformanceCardComponent({ id, title, coverImageURL }: HomepagePerformanceCardComponentProps) {
    const navigate = useNavigate();

    return (
        <article className={styles.card} onClick={() => navigate(`/spectacole/${id}`)}>
            <div className={styles.image}>
                <img src={coverImageURL} alt={title} />
            </div>
            <div className={styles.info}>{title.toLocaleUpperCase()}</div>
        </article>
    );
}

export default HomepagePerformanceCardComponent;
