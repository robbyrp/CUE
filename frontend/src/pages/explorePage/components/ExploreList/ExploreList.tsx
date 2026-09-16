import styles from './ExploreList.module.scss';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import CategoryCarousel from '../../../../common/components/categoryCarousel/CategoryCarousel';
import PerformanceCardComponent from '../../../../components/performanceCardComponent/PerformanceCardComponent';
import { PerformanceService } from '../../../../services/PerformanceService';
import type PerformancePortal from '../../../../types/Performance';

const PAGE_NUMBER = 0;
const PAGE_SIZE = 10;

const CATEGORIES = [
    { key: 'popular', title: 'POPULARE', sort: 'averageRating,desc' },
    { key: 'short', title: 'DURATĂ SCURTĂ', sort: 'duration,asc' },
    { key: 'mostViewed', title: 'CELE MAI VIZIONATE', sort: 'viewsCount,desc' },
] as const;

type CategoryKey = typeof CATEGORIES[number]['key'];

export default function ExploreList() {
    const [performancesByCategory, setPerformancesByCategory] = useState<Partial<Record<CategoryKey, PerformancePortal[]>>>({});
    const navigate = useNavigate();

    useEffect(() => {
        const loadData = async () => {
            try {
                const responses = await Promise.all(
                    CATEGORIES.map((category) =>
                        PerformanceService.getPerformancesByCategory({
                            page: PAGE_NUMBER,
                            size: PAGE_SIZE,
                            sort: category.sort,
                        })
                    )
                );
                const result: Partial<Record<CategoryKey, PerformancePortal[]>> = {};
                CATEGORIES.forEach((category, index) => {
                    result[category.key] = responses[index].content;
                });
                setPerformancesByCategory(result);
            } catch (error) {
                console.error("Eroare la aducerea spectacolelor pe categorii", error);
            }
        };
        loadData();
    }, []);

    return (
        <div className={styles.exploreList}>
            <div className={styles.centeredContent}>
                {CATEGORIES.map((category) => (
                    <div key={category.key} className={styles.carouselContainer}>
                        <CategoryCarousel title={category.title}>
                            {(performancesByCategory[category.key] ?? []).map((dto) => (
                                <div
                                    key={dto.id}
                                    className={styles.card}
                                    onClick={() => navigate(`/spectacole/${dto.id}`)}
                                >
                                    <PerformanceCardComponent data={dto} />
                                </div>
                            ))}
                        </CategoryCarousel>
                    </div>
                ))}
            </div>
        </div>
    );
}
