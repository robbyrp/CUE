import React from 'react';
import useEmblaCarousel from 'embla-carousel-react';
import { ChevronLeft, ChevronRight } from 'lucide-react';
import styles from './CategoryCarousel.module.scss';

export interface CategoryCarouselProps {
    title: string;
    children: React.ReactNode;
}

export default function CategoryCarousel({ title, children }: CategoryCarouselProps) {
    const [emblaRef, emblaApi] = useEmblaCarousel({
        loop: true,
        align: 'start',
    });

    const scrollNext = () => emblaApi?.scrollNext();
    const scrollPrev = () => emblaApi?.scrollPrev();

    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <h2 className={styles.title}>{title}</h2>
                <div className={styles.titleLine}></div>
            </div>

            <div className={styles.carouselWrapper}>
                <button className={styles.navButton} onClick={scrollPrev}>
                    <ChevronLeft size={48} strokeWidth={1} />
                </button>

                <div className={styles.embla} ref={emblaRef}>
                    <div className={styles.emblaContainer}>
                        {React.Children.map(children, (child) => (
                            <div className={styles.emblaSlide}>
                                {child}
                            </div>
                        ))}
                    </div>
                </div>

                <button className={styles.navButton} onClick={scrollNext}>
                    <ChevronRight size={48} strokeWidth={1} />
                </button>
            </div>
        </div>
    );
}