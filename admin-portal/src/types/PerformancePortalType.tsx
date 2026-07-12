export interface Credit {
    role: string;
    names: string[];
}

export interface Review {
    id?: number;
    // The backend has @JsonIgnore on user and Performance until the entities are set in stone
    // so we do not include them here
    //TODO: Add user and performance here when everything is ready
    publishingDate: string;
    stars: number;
    hearts: number;
    text: string;
    isSpoiler: boolean;
    reports: number;
}

export default interface PerformancePortalType {
    id?: number;
    title: string;
    director: string;
    coverImageURL: string;
    ageLimit: number;
    duration: number;
    location: string;
    startDateTime: string;
    fullCoverImageURL: string;
    purchaseTicketLink: string;
    description: string;
    credits: Credit[];
    reviews: Review[];
}