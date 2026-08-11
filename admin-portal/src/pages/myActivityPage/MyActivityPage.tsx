import styles from './MyActivityPage.module.scss';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import CategoryCarousel from '../../common/components/categoryCarousel/CategoryCarousel';
import {PerformancePortalService} from '../../services/PerformancePortalService'
import PerformanceCardComponent from '../../components/performanceCardComponent/PerformanceCardComponent';
import DesktopHeader from '../../common/components/DesktopHeader/DesktopHeader';
import DesktopFooter from '../../common/components/DesktopFooter/DesktopFooter';
import type PerformancePortal from '../../types/PerformancePortal';

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
    const MOCK_PERFORMANCES_PAGE_SIZE = 2;
    const [MOCK_PERFORMANCES, SET_MOCK_PERFORMANCES] = useState<PerformancePortal[]>([])

    const getFallbackContent = (
        content: PerformancePortal[] | undefined,
        fallback: PerformancePortal[]
    ) => {
        return content && content.length > 0 ? content : fallback;
    };

    useEffect(() => {
        PerformancePortalService.getAllPerformances(PAGE_NUMBER, MOCK_PERFORMANCES_PAGE_SIZE)
            .then( (response) => {
                SET_MOCK_PERFORMANCES(response.content);
            })
            .catch((error) => console.log("Error fetching mock performances: ", error));
    }, []);

    useEffect(() => {
        const loadMyActivity = async () => {
            try {
                const [
                    mockResponse,
                    watchLaterResponse,
                    watchedResponse,
                    reviewedResponse
                ] = await Promise.all([
                    PerformancePortalService.getAllPerformances(PAGE_NUMBER, MOCK_PERFORMANCES_PAGE_SIZE),
                    PerformancePortalService.getWatchLaterPerformances(PAGE_NUMBER, PAGE_SIZE),
                    PerformancePortalService.getWatchedPerformances(PAGE_NUMBER, PAGE_SIZE),
                    PerformancePortalService.getReviewedPerformances(PAGE_NUMBER, PAGE_SIZE)
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
            <DesktopHeader/>
            <div className={styles.myActivityContainer}>

                <CategoryCarousel
                    title = "WATCH LATER">
                    {watchLaterPerformanceDTOs.map((dto: PerformancePortal) => (
                        <div key = {dto.id} onClick={() => navigate(`/spectacole/${dto.id}`)} style={{cursor: `pointer`}}>
                            <PerformanceCardComponent data={dto} />
                        </div>
                    ))}
                </CategoryCarousel>

                <CategoryCarousel
                    title = "WATCHED">
                    {watchedPerformanceDTOs.map((dto: PerformancePortal) => (
                        <div key = {dto.id} onClick={() => navigate(`/spectacole/${dto.id}`)} style={{cursor: `pointer`}}>
                            <PerformanceCardComponent data={dto} />
                        </div>
                    ))}
                </CategoryCarousel>

                <CategoryCarousel
                    title = "REVIEWED">
                    {reviewedPerformanceDTOs.map((dto: PerformancePortal) => (
                        <div key = {dto.id} onClick={() => navigate(`/spectacole/${dto.id}`)} style={{cursor: `pointer`}}>
                            <PerformanceCardComponent data={dto} />
                        </div>
                    ))}
                </CategoryCarousel>

            </div>
            <DesktopFooter/>
    </div>
    )
}

export default MyActivityPage;
