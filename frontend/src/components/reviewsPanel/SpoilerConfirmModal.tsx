import { useEffect } from 'react';
import { createPortal } from 'react-dom';
import { EyeOff } from 'lucide-react';
import styles from './ReviewsPanel.module.scss';

interface SpoilerConfirmModalProps {
    onConfirm: () => void;
    onCancel: () => void;
}

export default function SpoilerConfirmModal({ onConfirm, onCancel }: SpoilerConfirmModalProps) {

    useEffect(() => {
        const handleKeyDown = (event: KeyboardEvent) => {
            if (event.key === 'Escape') onCancel();
        };
        document.addEventListener('keydown', handleKeyDown);
        return () => document.removeEventListener('keydown', handleKeyDown);
    }, [onCancel]);


    return createPortal(
        <div className={styles.modalBackdrop} onClick={onCancel}>
            <div
                className={styles.modal}
                role="dialog"
                aria-modal="true"
                aria-labelledby="spoiler-modal-title"
                onClick={(event) => event.stopPropagation()}
            >
                <EyeOff className={styles.modalIcon} size={32} />
                <h2 id="spoiler-modal-title" className={styles.modalTitle}>Review cu spoilere</h2>
                <p className={styles.modalText}>
                    Acest review dezvăluie detalii din spectacol. Ești sigur că vrei să-l citești?
                </p>
                <div className={styles.modalActions}>
                    <button type="button" className={styles.secondaryButton} onClick={onCancel}>
                        Nu acum
                    </button>
                    <button type="button" className={styles.primaryButton} onClick={onConfirm} autoFocus>
                        Arată review-ul
                    </button>
                </div>
            </div>
        </div>,
        document.body
    );
}
