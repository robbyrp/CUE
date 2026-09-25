import { useState } from 'react';
import type { FormEvent } from 'react';
import { isAxiosError } from 'axios';
import { ArrowLeft } from 'lucide-react';
import styles from './ReviewsPanel.module.scss';
import type { Review, ReviewRequest } from '../../types/Review';
import { ReviewService } from '../../services/ReviewService';
import Avatar from './Avatar';
import StarRating from './StarRating';

interface ReviewFormProps {
    performanceId: number;
    authorName: string;
    authorImageUrl?: string | null;
    existingReview: Review | null;
    onCancel: () => void;
    onSaved: () => void;
}

export default function ReviewForm({
    performanceId,
    authorName,
    authorImageUrl,
    existingReview,
    onCancel,
    onSaved,
}: ReviewFormProps) {
    const [stars, setStars] = useState(existingReview?.stars ?? 0);
    const [text, setText] = useState(existingReview?.text ?? '');
    const [isSpoiler, setIsSpoiler] = useState(existingReview?.isSpoiler ?? false);
    const [error, setError] = useState('');
    const [submitting, setSubmitting] = useState(false);

    const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        if (stars === 0) {
            setError('Alege cel puțin o stea.');
            return;
        }

        const request: ReviewRequest = { stars, text: text.trim(), isSpoiler };
        setError('');
        setSubmitting(true);

        try {
            if (existingReview?.id) {
                await ReviewService.updateReview(existingReview.id, request);
            } else {
                await ReviewService.createReview(performanceId, request);
            }
            onSaved();
        } catch (err) {
            if (isAxiosError(err) && err.response?.status === 409) {
                setError('Ai scris deja un review la acest spectacol.');
            } else {
                setError('Review-ul nu a putut fi salvat. Încearcă din nou.');
            }
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <form className={styles.form} onSubmit={handleSubmit}>
            <button type="button" className={styles.squareButton} onClick={onCancel} aria-label="Înapoi la review-uri">
                <ArrowLeft size={18} />
            </button>

            <div className={styles.formHeader}>
                <Avatar name={authorName} imageUrl={authorImageUrl} />
                <span className={styles.authorName}>{authorName}</span>
                <StarRating value={stars} size={22} onChange={setStars} />
            </div>

            <textarea
                className={styles.textarea}
                placeholder="Scrie ce ai simțit la spectacol..."
                value={text}
                onChange={(event) => setText(event.target.value)}
                required
                aria-label="Textul review-ului"
            />

            <label className={styles.spoilerCheckbox}>
                <input
                    type="checkbox"
                    checked={isSpoiler}
                    onChange={(event) => setIsSpoiler(event.target.checked)}
                />
                Conține spoilere
            </label>

            {error && <p className={styles.formError}>{error}</p>}

            <button type="submit" className={styles.submitButton} disabled={submitting}>
                {submitting ? 'SE TRIMITE...' : existingReview ? 'ACTUALIZEAZĂ' : 'TRIMITE'}
            </button>
        </form>
    );
}
