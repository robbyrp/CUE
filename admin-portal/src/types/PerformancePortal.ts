import type {Credit} from './Credit';
import type {Review} from './Review';

export default interface PerformancePortal {
    id?: number;
    title: string;
    director: string;
    coverImageURL: string;
    ageLimit: number;
    duration: number;
    location: string;
    theaterName: string;
    startDateTime: string;
    fullCoverImageURL: string;
    purchaseTicketLink: string;
    description: string;
    credits: Credit[];
    reviews: Review[];
}

