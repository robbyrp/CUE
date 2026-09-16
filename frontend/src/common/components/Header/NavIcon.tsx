import { useId } from 'react';
import styles from './Header.module.scss';

const ICONS = {
    house: {
        viewBox: '0 0 24 22',
        paths: [
            'M9 19.2762V10.5143H15V19.2762M3 7.88574L12 1.75244L21 7.88574V17.5238C21 17.9885 20.7893 18.4343 20.4142 18.7629C20.0391 19.0915 19.5304 19.2762 19 19.2762H5C4.46957 19.2762 3.96086 19.0915 3.58579 18.7629C3.21071 18.4343 3 17.9885 3 17.5238V7.88574Z',
        ],
    },
    star: {
        viewBox: '0 0 25 25',
        paths: [
            'M12.5 2.08325L15.7188 8.60409L22.9167 9.65617L17.7084 14.7291L18.9375 21.8958L12.5 18.5103L6.06254 21.8958L7.29171 14.7291L2.08337 9.65617L9.28129 8.60409L12.5 2.08325Z',
        ],
    },
    calendar: {
        viewBox: '0 0 24 22',
        paths: [
            'M16 1.83276V5.49827M8 1.83276V5.49827M3 9.16377H21M5 3.66551H19C20.1046 3.66551 21 4.48606 21 5.49827V18.3275C21 19.3397 20.1046 20.1603 19 20.1603H5C3.89543 20.1603 3 19.3397 3 18.3275V5.49827C3 4.48606 3.89543 3.66551 5 3.66551Z',
        ],
    },
    profile: {
        viewBox: '0 0 17 21',
        paths: [
            'M8.24445 9.25721C8.13448 9.24762 8.00251 9.24762 7.88154 9.25721C5.26424 9.18048 3.18579 7.31024 3.18579 5.0084C3.18579 2.65861 5.36321 0.75 8.06849 0.75C10.7628 0.75 12.9512 2.65861 12.9512 5.0084C12.9402 7.31024 10.8618 9.18048 8.24445 9.25721Z',
            'M2.74597 12.7963C0.0846769 14.3501 0.0846769 16.8821 2.74597 18.4263C5.77017 20.191 10.7298 20.191 13.754 18.4263C16.4153 16.8725 16.4153 14.3405 13.754 12.7963C10.7408 11.0412 5.78116 11.0412 2.74597 12.7963Z',
        ],
    },
};

export type NavIconName = keyof typeof ICONS;

interface NavIconProps {
    name: NavIconName;
    label: string;
}

export default function NavIcon({ name, label }: NavIconProps) {
    const { viewBox, paths } = ICONS[name];
    const clipId = `nav-fill-${useId().replace(/[^a-zA-Z0-9_-]/g, '')}`;

    return (
        <svg className={styles.navIcon} viewBox={viewBox} role="img" aria-label={label}>
            <defs>
                <clipPath id={clipId}>
                    <rect className={styles.fillRect} width="100%" height="100%" />
                </clipPath>
            </defs>
            {paths.map((d) => (
                <path key={`fill-${d}`} d={d} fill="white" clipPath={`url(#${clipId})`} />
            ))}
            {paths.map((d) => (
                <path
                    key={`stroke-${d}`}
                    d={d}
                    fill="none"
                    stroke="white"
                    strokeWidth="1.5"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                />
            ))}
        </svg>
    );
}
