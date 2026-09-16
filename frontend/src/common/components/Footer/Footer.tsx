import styles from './Footer.module.scss';
import linkedinLogo from '../../../assets/icons/Linkedin.svg';
import githubLogo from '../../../assets/icons/Github.svg';

export default function Footer() {
    return (
        <div className={styles.footerContainer}>
            <div className={styles.content}>
                <div className={styles.footerLogos}>
                    <div className={styles.footerLeftLogo}>
                        <a href="https://www.linkedin.com/in/robert-ionut-pana" target="_blank" rel="noopener noreferrer">
                            <img src={linkedinLogo} alt="Linkedin" />
                        </a>
                    </div>
                    <div className={styles.footerRightLogo}>
                        <a href="https://github.com/robbyrp" target="_blank" rel="noopener noreferrer">
                            <img src={githubLogo} alt="Github" />
                        </a>
                    </div>
                </div>
            </div>
        </div>
    );
}
