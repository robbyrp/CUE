import { useLayoutEffect, useRef, useState } from 'react';
import { Heart, MoreHorizontal, Trash2 } from 'lucide-react';
import styles from './ReviewsPanel.module.scss';
import type { Review } from '../../types/Review';
import Avatar from './Avatar';
import StarRating from './StarRating';
import { timeAgo } from '../../utils/timeAgo';

interface ReviewItemProps {
    review: Review;
    authorName: string;
    authorImageUrl?: string | null;
    isHearted: boolean;
    isOwn: boolean;
    isRevealed: boolean;
    onToggleHeart: () => void;
    onRequestReveal: () => void;
    onDelete: () => void;
}

export default function ReviewItem({
    review,
    authorName,
    authorImageUrl,
    isHearted,
    isOwn,
    isRevealed,
    onToggleHeart,
    onRequestReveal,
    onDelete,
}: ReviewItemProps) {
    const [expanded, setExpanded] = useState(false);
    const [isClamped, setIsClamped] = useState(false);
    const [menuOpen, setMenuOpen] = useState(false);
    const textRef = useRef<HTMLParagraphElement>(null);

    const isBlurred = review.isSpoiler && !isRevealed;

    useLayoutEffect(() => {
        const element = textRef.current;
        if (element && !expanded) {
            setIsClamped(element.scrollHeight > element.clientHeight + 1);
        }
    }, [review.text, expanded]);

    return (
        <article className={styles.review}>
            <header className={styles.reviewHeader}>
                <Avatar name={authorName} imageUrl={authorImageUrl} />
                <div className={styles.reviewMeta}>
                    <span className={styles.authorName}>{authorName}</span>
                    <time className={styles.reviewDate} dateTime={review.createdAt}>
                        {timeAgo(review.createdAt)}
                    </time>
                </div>

                {isOwn && (
                    <div className={styles.menuWrapper}>
                        <button
                            type="button"
                            className={styles.iconButton}
                            onClick={() => setMenuOpen((prev) => !prev)}
                            aria-label="Opțiuni review"
                            aria-expanded={menuOpen}
                        >
                            <MoreHorizontal size={20} />
                        </button>
                        {menuOpen && (
                            <div className={styles.menu}>
                                <button
                                    type="button"
                                    className={styles.menuItem}
                                    onClick={() => {
                                        setMenuOpen(false);
                                        onDelete();
                                    }}
                                >
                                    <Trash2 size={16} /> Șterge review-ul
                                </button>
                            </div>
                        )}
                    </div>
                )}
            </header>

            <div className={styles.reviewBody}>
                <p
                    ref={textRef}
                    className={`${styles.reviewText} ${expanded ? '' : styles.reviewTextClamped} ${isBlurred ? styles.blurred : ''}`}
                    aria-hidden={isBlurred}
                >
                    {review.text}
                </p>

                {isBlurred && (
                    <button type="button" className={styles.spoilerOverlay} onClick={onRequestReveal}>
                        Conține spoilere · Apasă ca să vezi
                    </button>
                )}

                {!isBlurred && (isClamped || expanded) && (
                    <button
                        type="button"
                        className={styles.moreButton}
                        onClick={() => setExpanded((prev) => !prev)}
                    >
                        {expanded ? 'mai puțin' : '...mai mult'}
                    </button>
                )}
            </div>

            <footer className={styles.reviewFooter}>
                <button
                    type="button"
                    className={`${styles.heartButton} ${isHearted ? styles.heartActive : ''}`}
                    onClick={onToggleHeart}
                    aria-pressed={isHearted}
                    aria-label={isHearted ? 'Nu mai aprecia' : 'Apreciază'}
                >
                    <span>{review.hearts}</span>
                    <Heart size={16} />
                </button>
                <StarRating value={review.stars} />
            </footer>
        </article>
    );
}
