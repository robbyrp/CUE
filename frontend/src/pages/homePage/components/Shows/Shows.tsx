import styles from './Shows.module.scss';
import { useState, useEffect } from 'react';
import { PerformanceService } from '../../../../services/PerformanceService';
import HomepagePerformanceCardComponent from '../../../../components/homepagePerformanceCardComponent/HomepagePerformanceCardComponent';
import type PerformancePortal from '../../../../types/Performance';
import type { PageRequest } from '../../../../types/PageRequest';

export default function Shows() {
    const [performances, setPerformances] = useState<PerformancePortal[]>([]);

    const PAGE_NUMBER = 0;
    const PAGE_SIZE = 4;

    useEffect(() => {
        const loadData = async () => {
            try {
                const requestQueryParams: PageRequest = {
                    page: PAGE_NUMBER,
                    size: PAGE_SIZE,
                    sort: "startDateTime,asc"
                }
                const response = await PerformanceService.getPerformancesByCategory(requestQueryParams);
                setPerformances(response.content);
            } catch (error) {
                console.error("Eroare la aducerea urmatoarelor spectacole", error);
            }
        };
        loadData();
    }, []);

    return (
        <div className={styles.shows}>
            <div className={styles.centeredContent}>
                <div className={styles.title}>URMATOARELE SPECTACOLE</div>
                <div className={styles.carouselContainer}>
                    {performances.map((dto) => (
                        <HomepagePerformanceCardComponent
                            key={dto.id}
                            id={dto.id!}
                            title={dto.title}
                            coverImageURL={dto.fullCoverImageURL}
                        />
                    ))}
                </div>
            </div>
        </div>
    );
}
