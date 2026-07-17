import type PerformancePortal from "../../types/PerformancePortal";
import styles from './PerformanceComponent.module.scss';
import {getAgeIcon} from '../../utils/ageHelper'

function PerformanceComponent ({data}: {data: PerformancePortal}) {
    const currentAgeIcon = getAgeIcon(data.ageLimit);

        return (

        <article className={styles.PerformanceContainer}>
            <div className={styles.ImageContainer}>
                {/* Image is handled via background-color in SCSS for now, following ExploreCard pattern */}
                <div className={styles.TitleAndDirectorContainer}>
                    <div className={styles.DirectorName}> {data.director.toUpperCase()} </div>
                    <div className={styles.Title}> {data.title} </div>
                </div>
            </div>

            <div className={styles.AgeAndTimeHeader}>
                {currentAgeIcon && (
                    <img
                        src={currentAgeIcon}
                        className={styles.AgeImage}
                    />
                )}
                <div> {`${data.duration} min`} </div>
            </div>

            <div className={styles.DescriptionAndCreditsContainer}>
                <div className={styles.DescriptionContainer}>
                    {data.description}
                </div>

                <div className={styles.CreditsContainer}>
                    {data.credits.map((credit, index) => (
                        <div key={index} className={styles.Credit}>
                            <div className={styles.Role}>{credit.role.toUpperCase() + ':'}</div>
                            <div className={styles.Names}>{credit.names.join(', ')}</div>
                        </div>
                    ))}
                </div>
            </div>

        </article>

    )
}
export default PerformanceComponent;