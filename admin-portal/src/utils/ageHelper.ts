import age4 from '../assets/icons/ageLimit/4.svg';
import age6 from '../assets/icons/ageLimit/6.svg';
import age8 from '../assets/icons/ageLimit/8.svg';
import age10 from '../assets/icons/ageLimit/10.svg';
import age12 from '../assets/icons/ageLimit/12.svg';
import age14 from '../assets/icons/ageLimit/14.svg';
import age16 from '../assets/icons/ageLimit/16.svg';
import age18 from '../assets/icons/ageLimit/18.svg';

const ageIcons: Record<number, string> = {
    4: age4,
    6: age6,
    8: age8,
    10: age10,
    12: age12,
    14: age14,
    16: age16,
    18: age18,
};

const AVAILABLE_AGE_ICONS = Object.keys(ageIcons).map(Number);

export function getAgeIcon(ageLimit: number | undefined | null): string | null {
    if (!ageLimit) return null;
    const validAge = AVAILABLE_AGE_ICONS.slice().reverse().find(iconAge => iconAge <= ageLimit);
    return validAge ? ageIcons[validAge] : null;
}