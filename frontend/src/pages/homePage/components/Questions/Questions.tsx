import styles from './Questions.module.scss';
import { useState } from 'react';

const CaretDown = () => (
    <svg width="18" height="8" viewBox="0 0 18 8" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path d="M17.5 -4.00543e-05L8.75 7.91663L0 -4.00543e-05L17.5 -4.00543e-05Z" fill="currentColor" />
    </svg>
);

const AccordionItem = ({ question, answer }: { question: string, answer: string }) => {
    const [isOpen, setIsOpen] = useState(false);

    return (
        <div className={styles.questionEntry}>
            <div className={styles.questionHeader} onClick={() => setIsOpen(!isOpen)}>
                <div className={styles.question}>{question}</div>
                <span className={`${styles.icon} ${isOpen ? styles.rotateUp : styles.rotateDown}`}>
                    <CaretDown />
                </span>
            </div>
            <div className={`${styles.answer} ${isOpen ? styles.show : styles.hide}`}>
                <div className={styles.answerContent}>
                    {answer}
                </div>
            </div>
        </div>
    );
};

export default function Questions() {
    const data = [{
        question: `Ce este CUE?`,
        answer: `CUE este o platformă care oferă expunere în mediul online spectacolelor de artă teatrală din întreaga țară
         cu publicul tânăr.`
    }, {
        question: `Cum funcționează CUE?`,
        answer: `CUE oferă o interfață prietenoasă în care administratorii teatrelor își pot încărca spectacolele din următoarea perioadă estivală. Utilizatorii pot adăuga review-uri și își pot crea liste personalizate de spectacole pe care doresc să le urmărească, sau pe care le-au urmărit și le recomandă. `
    }, {
        question: `CUE este gratuit?`,
        answer: `CUE este o platformă complet gratuită, atât pentru utilizatori, cât și pentru cei care doresc să promoveze spectacolele.`
    }, {
        question: `Pot folosi CUE pe telefonul mobil?`,
        answer: `Momentan, CUE este disponibil doar pe browser. În viitor, plănuim să lansăm aplicația și pe Android/iOS.`
    }
    ];

    return (
        <div className={styles.questionsContainer}>
            <div className={styles.centeredContent}>
                <h2 className={styles.title}>FAQ</h2>
                <div className={styles.gridContainer}>
                    {data.map((item, index) => (
                        <AccordionItem key={index} {...item} />
                    ))}
                </div>
            </div>
        </div>
    );
}
