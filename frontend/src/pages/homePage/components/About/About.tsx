import { useState, useRef } from 'react';
import styles from './About.module.scss';
import { ChevronLeft, ChevronRight } from 'lucide-react';
import gsap from 'gsap';
import { useGSAP } from '@gsap/react';

gsap.registerPlugin(useGSAP);

const CAROUSEL_DATA = [
    {
        id: 1,
        opacity: 1,
        imageUrl: 'https://images.unsplash.com/photo-1560184611-5b5749138c3c?auto=format&fit=crop&w=1200&q=80',
    },
    {
        id: 2,
        opacity: 0.7,
        imageUrl: 'https://images.unsplash.com/photo-1758529226619-aa78157c64b2?auto=format&fit=crop&w=1200&q=80',
    },
    {
        id: 3,
        opacity: 0.4,
        imageUrl: 'https://images.unsplash.com/photo-1545129139-1beb780cf337?auto=format&fit=crop&w=1200&q=80',
    },
];

export default function About() {
    const [currentIndex, setCurrentIndex] = useState(0);
    const [direction, setDirection] = useState(1); // 1: right, -1: left
    const container = useRef<HTMLDivElement>(null);
    const photoRef = useRef<HTMLDivElement>(null);

    const handleNext = () => {
        setDirection(-1);
        setCurrentIndex((prev) => (prev + 1) % CAROUSEL_DATA.length);
    };

    const handlePrev = () => {
        setDirection(1);
        setCurrentIndex((prev) => (prev - 1 + CAROUSEL_DATA.length) % CAROUSEL_DATA.length);
    };

    useGSAP(() => {
        if (photoRef.current) {
            gsap.fromTo(
                photoRef.current,
                { x: direction * 343, opacity: 0 },
                { x: 0, opacity: 1, duration: 0.75, ease: 'power2.inOut' }
            );
        }
    }, { dependencies: [currentIndex] });

    const currentItem = CAROUSEL_DATA[currentIndex];

    return (
        <div className={styles.about} ref={container}>
            <div className={styles.centeredContent}>
                <div className={styles.leftSection}>
                    <div className={styles.content}>
                        <div className={styles.title}>DESPRE NOI</div>
                        <div className={styles.description}>
                            Suntem o echipă pasionată de Artă și teatru. Dorința noastră este să aducem toate spectacolele din țară într-un singur loc - CUE.
                        </div>
                    </div>
                </div>
                <div className={styles.rightSection}>
                    <ChevronLeft className={styles.arrow} size={48} onClick={handlePrev} />
                    <div
                        ref={photoRef}
                        className={styles.carouselPhoto}
                        style={{
                            opacity: currentItem.opacity,
                            backgroundImage: `url(${currentItem.imageUrl})`,
                        }}
                    />
                    <ChevronRight className={styles.arrow} size={48} onClick={handleNext} />
                </div>
            </div>
        </div>
    );
}
