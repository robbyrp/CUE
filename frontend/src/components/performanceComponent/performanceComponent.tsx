import styles from './PerformanceComponent.module.scss';
import type PerformancePortal from "../../types/Performance";
import { useState, useEffect } from "react";
import { getAgeIcon } from '../../utils/ageHelper';
import type { MouseEvent } from "react";
import { useAuth } from '../../auth/AuthContext';
import { UserService } from '../../services/UserService';
import ReviewsPanel from '../reviewsPanel/ReviewsPanel';

function PerformanceComponent({ data }: { data: PerformancePortal }) {
    const { isAuthenticated } = useAuth();

    const currentAgeIcon = getAgeIcon(data.ageLimit);

    const [isPerformanceWatched, setIsPerformanceWatched] = useState(false);
    const [isPerformanceInWatchLater, setIsPerformanceInWatchLater] = useState(false);
    const [isButtonsLoading, setIsButtonsLoading] = useState(true);
    const [activeButton, setActiveButton] = useState<"watched" | "watchLater" | null>(null);

    useEffect(() => {
        const loadPerformanceStatus = async () => {
            if (!data.id || !isAuthenticated) {
                setIsButtonsLoading(false);
                return;
            }

            try {
                setIsButtonsLoading(true);

                const [watchedStatus, watchLaterStatus] = await Promise.all([
                    UserService.isInWatched(data.id),
                    UserService.isInWatchLater(data.id)
                ]);

                setIsPerformanceWatched(watchedStatus);
                setIsPerformanceInWatchLater(watchLaterStatus);
            } catch (error) {
                console.error("Error loading performance status:", error);
            } finally {
                setIsButtonsLoading(false);
            }
        };
        loadPerformanceStatus();
    }, [data.id, isAuthenticated]);

    const handleWatchedClick = async (event: MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        if (!data.id) return;

        try {
            setActiveButton("watched");
            if (isPerformanceWatched) {
                await UserService.deleteFromWatched(data.id);
                setIsPerformanceWatched(false);
            } else {
                await UserService.addToWatched(data.id);
                setIsPerformanceWatched(true);
            }
        } catch (error) {
            console.log("Error whe pressing Add to Watched: ", error);
        } finally {
            setActiveButton(null);
        }
    };

    const handleWatchLaterClick = async (event: MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        if (!data.id) return;

        try {
            setActiveButton("watchLater");
            if (isPerformanceInWatchLater) {
                await UserService.deleteFromWatchLater(data.id);
                setIsPerformanceInWatchLater(false);
            } else {
                await UserService.addToWatchLater(data.id);
                setIsPerformanceInWatchLater(true);
            }
        } catch (error) {
            console.log("Error when pressing Watch Later button: ", error);
        } finally {
            setActiveButton(null);
        }
    };

    return (
        <article className={styles.PerformanceContainer}>
            <section className={styles.HeroContainer}>
                <div className={styles.ImageContainer}>
                    {data.fullCoverImageURL ? (
                        <img src={data.fullCoverImageURL} alt={data.title} />
                    ) : null}
                    <div className={styles.TitleAndDirectorContainer}>
                        <div className={styles.DirectorName}> {data.director.toUpperCase()} </div>
                        <div className={styles.Title}> {data.title.toUpperCase()} </div>
                    </div>
                </div>
            </section>

            <div className={styles.AgeAndTimeHeader}>
                <div className={styles.AgeAndDuration}>
                    {currentAgeIcon && (
                        <img
                            src={currentAgeIcon}
                            className={styles.AgeImage}
                            alt={`Varsta ${data.ageLimit}+`}
                        />
                    )}
                    <div> {`${data.duration} min`} </div>
                </div>

                {isAuthenticated && (
                    <div className={styles.ActionButtonsGroup}>
                        <button
                            type="button"
                            className={`${styles.WatchedButton} ${isPerformanceWatched ? styles.ActionButtonActive : ""}`}
                            onClick={handleWatchedClick}
                            disabled={isButtonsLoading || activeButton === "watched"}
                        >
                            <span className={styles.ActionButtonText}>
                                {isPerformanceWatched ? "STERGE DIN VIZIONATE" : "VIZIONAT"}
                            </span>
                        </button>

                        <button
                            type="button"
                            className={`${styles.WatchLaterButton} ${isPerformanceInWatchLater ? styles.ActionButtonActive : ""}`}
                            onClick={handleWatchLaterClick}
                            disabled={isButtonsLoading || activeButton === "watchLater"}
                        >
                            <span className={styles.ActionButtonText}>
                                {isPerformanceInWatchLater ? "STERGE DIN SALVATE" : "SALVEAZA"}
                            </span>
                        </button>
                    </div>
                )}

                <a className={styles.TicketButton} href={data.purchaseTicketLink} target="_blank" rel="noreferrer">
                    CUMPARA BILET
                </a>
            </div>

            <div className={styles.ContentGrid}>
                <div className={styles.DescriptionPanel}>
                    <div className={styles.DescriptionText}>
                        {data.description}
                    </div>
                </div>

                <div className={styles.RightColumn}>
                    <div className={styles.CreditsContainer}>
                        <div className={styles.SectionTitle}>CREDITE</div>
                        {data.credits.map((credit, index) => (
                            <div key={`${credit.role}-${index}`} className={styles.Credit}>
                                <div className={styles.Role}>{credit.role.toUpperCase() + ':'}</div>
                                <div className={styles.Names}>{credit.names.join(', ')}</div>
                            </div>
                        ))}
                    </div>

                    {data.id !== undefined && <ReviewsPanel performanceId={data.id} />}
                </div>
            </div>

        </article>

    )
}
export default PerformanceComponent;