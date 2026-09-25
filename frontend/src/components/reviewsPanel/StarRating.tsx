import { useState } from 'react';
import { Star } from 'lucide-react';
import styles from './ReviewsPanel.module.scss';

const MAX_STARS = 5;

interface StarRatingProps {
    value: number;
    size?: number;
    onChange?: (value: number) => void;
}

export default function StarRating({ value, size = 16, onChange }: StarRatingProps) {
    const [hovered, setHovered] = useState<number | null>(null);
    const interactive = Boolean(onChange);
    const shown = hovered ?? value;

    return (
        <div
            className={`${styles.stars} ${interactive ? styles.starsInteractive : ''}`}
            onMouseLeave={() => setHovered(null)}
            role={interactive ? 'radiogroup' : 'img'}
            aria-label={interactive ? 'Alege numărul de stele' : `${value} din ${MAX_STARS} stele`}
        >
            {Array.from({ length: MAX_STARS }, (_, index) => {
                const starValue = index + 1;
                const filled = starValue <= shown;
                const star = <Star size={size} className={filled ? styles.starFilled : styles.starEmpty} />;

                if (!interactive) {
                    return <span key={starValue}>{star}</span>;
                }

                return (
                    <button
                        key={starValue}
                        type="button"
                        role="radio"
                        aria-checked={value === starValue}
                        aria-label={`${starValue} stele`}
                        className={styles.starButton}
                        onMouseEnter={() => setHovered(starValue)}
                        onClick={() => onChange?.(starValue)}
                    >
                        {star}
                    </button>
                );
            })}
        </div>
    );
}
