import styles from './MyActivityPage.module.scss';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import CategoryCarousel from '../../common/components/categoryCarousel/CategoryCarousel';
import { PerformanceService } from '../../services/PerformanceService';
import { UserService } from '../../services/UserService';
import PerformanceCardComponent from '../../components/performanceCardComponent/PerformanceCardComponent';
import Header from '../../common/components/Header/Header';
import Footer from '../../common/components/Footer/Footer';
import type PerformancePortal from '../../types/Performance';
import type { PageRequest } from '../../types/PageRequest';

function MyActivityPage() {
    const [watchLaterPerformanceDTOs, setWatchLaterPerformanceDTOs] =
        useState<PerformancePortal[]>([]);
    const [watchedPerformanceDTOs, setWatchedPerformanceDTOs] =
        useState<PerformancePortal[]>([]);
    const [reviewedPerformanceDTOs, setReviewedPerformanceDTOs] =
        useState<PerformancePortal[]>([]);
    const navigate = useNavigate();

    const PAGE_NUMBER = 0;
    const PAGE_SIZE = 10;

    const getFallbackContent = (
        content: PerformancePortal[] | undefined,
        fallback: PerformancePortal[]
    ) => {
        return content && content.length > 0 ? content : fallback;
    };

    useEffect(() => {
        const loadMyActivity = async () => {
        
        const requestQueryParams: PageRequest = {
            page: PAGE_NUMBER,
            size: PAGE_SIZE,
            sort: "averageRating,desc" //TODO: EXPLORE PAGE CUSTOM CU MAI MULTE CATEGORII DE SORT
        };
        const watchItemRequestQueryParams: PageRequest = {
            page: PAGE_NUMBER,
            size: PAGE_SIZE,
        };

            try {
                const [
                    mockResponse,
                    watchLaterResponse,
                    watchedResponse,
                    reviewedResponse
                ] = await Promise.all([
                    PerformanceService.getPerformancesByCategory(requestQueryParams),
                    UserService.getWatchLaterPerformances(watchItemRequestQueryParams),
                    UserService.getWatchedPerformances(watchItemRequestQueryParams),
                    UserService.getReviewedPerformances(watchItemRequestQueryParams)
                ]);

                const mockPerformances = mockResponse.content;

                setWatchLaterPerformanceDTOs(
                    getFallbackContent(watchLaterResponse.content, mockPerformances)
                );
                setWatchedPerformanceDTOs(
                    getFallbackContent(watchedResponse.content, mockPerformances)
                );
                setReviewedPerformanceDTOs(
                    getFallbackContent(reviewedResponse.content, mockPerformances)
                );
            } catch (error) {
                console.error('Error loading my activity performances:', error);
            }
        };

        loadMyActivity();
    }, []);

    if (!watchedPerformanceDTOs || !watchLaterPerformanceDTOs || !reviewedPerformanceDTOs)
        return <div>Loading...</div>;

    return (
        <div className={styles.myActivityPage}>
            <Header />
            <div className={styles.myActivityContainer}>

                <CategoryCarousel
                    title="DE VĂZUT">
                    {watchLaterPerformanceDTOs.map((dto: PerformancePortal) => (
                        <div key={dto.id} onClick={() => navigate(`/spectacole/${dto.id}`)} style={{ cursor: `pointer` }}>
                            <PerformanceCardComponent data={dto} />
                        </div>
                    ))}
                </CategoryCarousel>

                <CategoryCarousel
                    title="VIZIONATE">
                    {watchedPerformanceDTOs.map((dto: PerformancePortal) => (
                        <div key={dto.id} onClick={() => navigate(`/spectacole/${dto.id}`)} style={{ cursor: `pointer` }}>
                            <PerformanceCardComponent data={dto} />
                        </div>
                    ))}
                </CategoryCarousel>

                <CategoryCarousel
                    title="RECENZII">
                    {reviewedPerformanceDTOs.map((dto: PerformancePortal) => (
                        <div key={dto.id} onClick={() => navigate(`/spectacole/${dto.id}`)} style={{ cursor: `pointer` }}>
                            <PerformanceCardComponent data={dto} />
                        </div>
                    ))}
                </CategoryCarousel>

            </div>
            <Footer />
        </div>
    )
}

export default MyActivityPage;
