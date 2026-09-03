import styles from './PerformanceCardComponent.module.scss';

import type PerformancePortal from '../../types/Performance.ts';
import { getAgeIcon } from '../../utils/ageHelper.ts';


function PerformanceCardComponent({ data }: { data: PerformancePortal }) {
    const currentAgeIcon = getAgeIcon(data.ageLimit);
    return (
        <article className={styles.CardContainer}>
            <div className={styles.ImageContainer}>
                {data.coverImageURL && <img src={data.coverImageURL} alt={data.title} />}
            </div>
            <div className={styles.ContentContainer}>
                <div className={styles.NameAndAgeRow}>
                    <div className={styles.Title}>{data.title.toUpperCase()}</div>
                    {currentAgeIcon && (
                        <img
                            src={currentAgeIcon}
                            className={styles.AgeImage}
                        />
                    )}
                </div>

                <div className={styles.DirectorAndTimeRow}>
                    <div className={styles.Director}>{data.director.toUpperCase()}</div>
                    <div className={styles.Time}>{data.duration} min</div>
                </div>
            </div>
        </article>
    );
}

export default PerformanceCardComponent;