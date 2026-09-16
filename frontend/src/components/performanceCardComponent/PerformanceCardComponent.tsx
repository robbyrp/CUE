import { useState } from 'react';
import { Clock, MapPin } from 'lucide-react';
import styles from './PerformanceCardComponent.module.scss';

import type PerformancePortal from '../../types/Performance.ts';
import { getAgeIcon } from '../../utils/ageHelper.ts';

function PerformanceCardComponent({ data }: { data: PerformancePortal }) {
    const [imageLoaded, setImageLoaded] = useState(false);
    const currentAgeIcon = getAgeIcon(data.ageLimit);

    return (
        <div className={styles.CardShell}>
            <article className={styles.CardContainer}>
                <div className={styles.ImageContainer}>
                    {data.coverImageURL && (
                        <img
                            src={data.coverImageURL}
                            alt={data.title}
                            loading="lazy"
                            className={imageLoaded ? styles.Loaded : ''}
                            onLoad={() => setImageLoaded(true)}
                        />
                    )}
                    <div className={styles.ImageOverlay} />

                    {currentAgeIcon && (
                        <img
                            src={currentAgeIcon}
                            className={styles.AgeImage}
                            alt={`Vârstă minimă ${data.ageLimit} ani`}
                        />
                    )}

                    <span className={styles.DurationPill}>
                        <Clock size={14} strokeWidth={2} />
                        {data.duration} min
                    </span>

                    <span className={styles.AccentLine} />
                </div>

                <div className={styles.ContentContainer}>
                    <h3 className={styles.Title} title={data.title}>{data.title}</h3>
                    <div className={styles.Director}>
                        <span className={styles.DirectorLabel}>Regia</span>
                        {data.director}
                    </div>

                    {(data.theaterName || data.location) && (
                        <div className={styles.Venue}>
                            <MapPin size={14} strokeWidth={2} />
                            <span>
                                {[data.theaterName, data.location].filter(Boolean).join(' · ')}
                            </span>
                        </div>
                    )}
                </div>
            </article>
        </div>
    );
}

export default PerformanceCardComponent;
