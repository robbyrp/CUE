import { useCallback, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Pencil, Plus } from 'lucide-react';
import styles from './ReviewsPanel.module.scss';
import type { Review } from '../../types/Review';
import type { UserProfile } from '../../types/UserProfile';
import { ReviewService } from '../../services/ReviewService';
import { UserProfileService } from '../../services/UserProfileService';
import { useAuth } from '../../auth/AuthContext';
import { ROUTES } from '../../utils/constants';
import ReviewItem from './ReviewItem';
import ReviewForm from './ReviewForm';
import SpoilerConfirmModal from './SpoilerConfirmModal';

const PAGE_SIZE = 10;
const SORT = 'createdAt,desc';

interface ReviewsPanelProps {
    performanceId: number;
}

export default function ReviewsPanel({ performanceId }: ReviewsPanelProps) {
    const { isAuthenticated } = useAuth();
    const navigate = useNavigate();

    const [mode, setMode] = useState<'list' | 'write'>('list');
    const [reviews, setReviews] = useState<Review[]>([]);
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(false);
    const [loading, setLoading] = useState(true);

    const [me, setMe] = useState<UserProfile | null>(null);
    const [myReview, setMyReview] = useState<Review | null>(null);

    const [heartedIds, setHeartedIds] = useState<Set<number>>(new Set());

    const [revealedIds, setRevealedIds] = useState<Set<number>>(new Set());
    const [spoilerToConfirm, setSpoilerToConfirm] = useState<number | null>(null);

    const fetchReviews = useCallback(async (upToPage: number) => {
        const response = await ReviewService.getPerformanceReviews(performanceId, {
            page: 0,
            size: (upToPage + 1) * PAGE_SIZE,
            sort: SORT,
        });
        setReviews(response.content);
        setHasMore(response.totalElements > response.content.length);
        return response.content;
    }, [performanceId]);

    const fetchMyReview = useCallback(async () => {
        if (!isAuthenticated) {
            setMyReview(null);
            return;
        }
        setMyReview(await ReviewService.getMyReview(performanceId));
    }, [performanceId, isAuthenticated]);

    useEffect(() => {
        const loadData = async () => {
            setLoading(true);
            try {
                await Promise.all([fetchReviews(0), fetchMyReview()]);
                setPage(0);
            } catch (error) {
                console.error('Eroare la aducerea review-urilor', error);
            } finally {
                setLoading(false);
            }
        };
        loadData();
    }, [fetchReviews, fetchMyReview]);

    useEffect(() => {
        if (!isAuthenticated) {
            setMe(null);
            return;
        }
        UserProfileService.get_current_user()
            .then(setMe)
            .catch((error) => console.error('Eroare la aducerea profilului', error));
    }, [isAuthenticated]);

    const handleLoadMore = async () => {
        const nextPage = page + 1;
        try {
            await fetchReviews(nextPage);
            setPage(nextPage);
        } catch (error) {
            console.error('Eroare la aducerea review-urilor', error);
        }
    };

    const isHearted = (review: Review) => review.id !== undefined && heartedIds.has(review.id);

    const handleToggleHeart = async (review: Review) => {
        if (!isAuthenticated) {
            navigate(ROUTES.LOGIN);
            return;
        }
        if (review.id === undefined) return;

        const reviewId = review.id;
        const wasHearted = isHearted(review);
        const heartsBefore = review.hearts;

        setHeartedIds((prev) => {
            const next = new Set(prev);
            if (wasHearted) next.delete(reviewId); else next.add(reviewId);
            return next;
        });
        setReviews((prev) => prev.map((r) =>
            r.id === reviewId ? { ...r, hearts: r.hearts + (wasHearted ? -1 : 1) } : r
        ));

        try {
            await ReviewService.toggleHeartReview(reviewId);

            const fresh = await fetchReviews(page);
            const updated = fresh.find((r) => r.id === reviewId);
            if (updated) {
                setHeartedIds((prev) => {
                    const next = new Set(prev);
                    if (updated.hearts > heartsBefore) next.add(reviewId); else next.delete(reviewId);
                    return next;
                });
            }
        } catch (error) {
            console.error('Eroare la aprecierea review-ului', error);
            await fetchReviews(page).catch(() => undefined);
        }
    };

    const handleDelete = async (reviewId: number) => {
        if (!window.confirm('Sigur vrei să ștergi review-ul?')) return;
        try {
            await ReviewService.deleteReview(reviewId);
            await Promise.all([fetchReviews(page), fetchMyReview()]);
        } catch (error) {
            console.error('Eroare la stergerea review-ului', error);
        }
    };

    const handleOpenWrite = () => {
        if (!isAuthenticated) {
            navigate(ROUTES.LOGIN);
            return;
        }
        setMode('write');
    };

    const handleSaved = async () => {
        setMode('list');
        await Promise.all([fetchReviews(page), fetchMyReview()]);
    };

    const handleConfirmSpoiler = () => {
        if (spoilerToConfirm === null) return;
        setRevealedIds((prev) => new Set(prev).add(spoilerToConfirm));
        setSpoilerToConfirm(null);
    };

    const handleCancelSpoiler = useCallback(() => setSpoilerToConfirm(null), []);

    const myName = me?.username ?? 'Tu';

    return (
        <section className={styles.panel} aria-label="Review-uri">
            {mode === 'write' ? (
                <ReviewForm
                    performanceId={performanceId}
                    authorName={myName}
                    authorImageUrl={me?.profilePictureUrl}
                    existingReview={myReview}
                    onCancel={() => setMode('list')}
                    onSaved={handleSaved}
                />
            ) : (
                <>
                    <div className={styles.panelHeader}>
                        <span className={styles.panelTitle}>REVIEW-URI</span>
                        <button
                            type="button"
                            className={styles.squareButton}
                            onClick={handleOpenWrite}
                            aria-label={myReview ? 'Editează review-ul tău' : 'Scrie un review'}
                            title={myReview ? 'Editează review-ul tău' : 'Scrie un review'}
                        >
                            {myReview ? <Pencil size={16} /> : <Plus size={18} />}
                        </button>
                    </div>

                    <div className={styles.scrollArea}>
                        {loading && <p className={styles.emptyState}>Se încarcă review-urile...</p>}

                        {!loading && reviews.length === 0 && (
                            <p className={styles.emptyState}>
                                Nu există încă review-uri. Fii primul care scrie unul!
                            </p>
                        )}

                        {reviews.map((review) => {
                            return (
                                <ReviewItem
                                    key={review.id}
                                    review={review}
                                    authorName={review.authorUsername}
                                    authorImageUrl={review.authorProfilePictureUrl}
                                    isHearted={isHearted(review)}
                                    isOwn={me !== null && review.authorUsername === me.username}
                                    isRevealed={review.id !== undefined && revealedIds.has(review.id)}
                                    onToggleHeart={() => handleToggleHeart(review)}
                                    onRequestReveal={() => review.id !== undefined && setSpoilerToConfirm(review.id)}
                                    onDelete={() => review.id !== undefined && handleDelete(review.id)}
                                />
                            );
                        })}

                        {hasMore && (
                            <button type="button" className={styles.loadMoreButton} onClick={handleLoadMore}>
                                Încarcă mai multe
                            </button>
                        )}
                    </div>
                </>
            )}

            {spoilerToConfirm !== null && (
                <SpoilerConfirmModal onConfirm={handleConfirmSpoiler} onCancel={handleCancelSpoiler} />
            )}
        </section>
    );
}
