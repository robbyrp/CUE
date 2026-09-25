import styles from './ReviewsPanel.module.scss';

interface AvatarProps {
    name: string;
    imageUrl?: string | null;
}

export default function Avatar({ name, imageUrl }: AvatarProps) {
    if (imageUrl) {
        return <img className={styles.avatar} src={imageUrl} alt="" />;
    }

    const initials = name
        .split(' ')
        .filter(Boolean)
        .slice(0, 2)
        .map((part) => part[0].toUpperCase())
        .join('');

    return <span className={`${styles.avatar} ${styles.avatarInitials}`}>{initials}</span>;
}
