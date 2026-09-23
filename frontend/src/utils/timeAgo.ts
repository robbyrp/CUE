const UNITS: { unit: Intl.RelativeTimeFormatUnit; seconds: number }[] = [
    { unit: 'year', seconds: 60 * 60 * 24 * 365 },
    { unit: 'month', seconds: 60 * 60 * 24 * 30 },
    { unit: 'week', seconds: 60 * 60 * 24 * 7 },
    { unit: 'day', seconds: 60 * 60 * 24 },
    { unit: 'hour', seconds: 60 * 60 },
    { unit: 'minute', seconds: 60 },
];

const formatter = new Intl.RelativeTimeFormat('ro', { numeric: 'auto' });

export const timeAgo = (isoDate: string): string => {
    const diffSeconds = (new Date(isoDate).getTime() - Date.now()) / 1000;

    for (const { unit, seconds } of UNITS) {
        if (Math.abs(diffSeconds) >= seconds) {
            return formatter.format(Math.round(diffSeconds / seconds), unit);
        }
    }
    return 'chiar acum';
};
