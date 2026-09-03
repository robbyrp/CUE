import styles from './DesktopFooter.module.scss';
import facebookLogo from '../../../assets/Facebook.svg';
import instagramLogo from '../../../assets/Instagram.svg';

export default function DesktopFooter() {
    return (
        <div className={styles.footerContainer}>
            <div className={styles.content}>
                <div className={styles.footerLogos}>
                    { /**TODO: Add on-click redirect to the social media pages*/ }
                    <div className={styles.footerLeftLogo}>
                        <img src={facebookLogo} alt="Facebook" />
                    </div>
                    <div className={styles.footerRightLogo}>
                        <img src={instagramLogo} alt="Instagram" />
                    </div>
                </div>
            </div>
        </div>
    );
}
