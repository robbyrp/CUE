import styles from './ExplorePage.module.scss';
import {useState, useEffect} from 'react';
import {PerformancePortalService} from '../../services/PerformancePortalService';
import PerformanceCardComponent from '../../components/performanceCardComponent/PerformanceCardComponent';
import type PerformancePortal from '../../types/PerformancePortal';
import { useNavigate } from 'react-router-dom';
import DesktopHeader from '../../common/components/DesktopHeader/DesktopHeader';
import DesktopFooter from '../../common/components/DesktopFooter/DesktopFooter';


function ExplorePage() {
    const [performanceDTOS, setPerformanceDTOS] = useState<PerformancePortal[]>([]);
    const navigate = useNavigate();

    const PAGE_NUMBER = 0;
    const PAGE_SIZE = 10;
    useEffect(() => {
        const loadData = async () => {
            try {
                const response = await PerformancePortalService.getAllPerformances(PAGE_NUMBER, PAGE_SIZE);
                setPerformanceDTOS(response.content);
            } catch (error) {
                console.error("Eroare la aducerea spectacolelor", error);
            }
        };
        loadData();
    }, []);

    if (!performanceDTOS) {
        return <div> Se incarca spectacolele..</div>;
    }
    return (
        <>
            <DesktopHeader/>
            <div className = {styles.ListContainer}>
            {performanceDTOS.map((dto: PerformancePortal) => (
                <div key = {dto.id} onClick={() => navigate(`/spectacole/${dto.id}`)} style={{cursor: `pointer`}}>
                    <PerformanceCardComponent data = {dto} />
                </div>
            ))}
            </div>
            <DesktopFooter />
        </>
        
    )
}
export default ExplorePage;
