import { useState, useEffect } from 'react';
import styles from './PerformancePage.module.scss'
import type PerformancePortal from '../../types/Performance';
import { PerformanceService } from '../../services/PerformanceService';
import PerformanceComponent from '../../components/performanceComponent/performanceComponent';
import { useParams } from 'react-router-dom';
import Header from '../../common/components/Header/Header';
import Footer from '../../common/components/Footer/Footer';

function PerformancePage() {
    const { id } = useParams();
    const [performanceDTO, setPerformanceDTO] = useState<PerformancePortal | null>(null);
    useEffect(() => {
        const loadData = async () => {
            if (id) {
                try {
                    const idNumber = Number(id);
                    const response = await PerformanceService.getPerformanceById(idNumber);
                    setPerformanceDTO(response);
                } catch (error) {
                    console.error("Eroare la aducerea spectacolului", error, id);
                }
            }

        };
        loadData();
    }, [id]);

    if (!performanceDTO) {
        return <div> Se incarca spectacolul..</div>;
    }

    return (
        <>
            <Header />
            <div className={styles.PerformanceComponentContainer} >
                <PerformanceComponent data={performanceDTO} />
            </div>
            <Footer />
        </>

    )
}

export default PerformancePage;                     