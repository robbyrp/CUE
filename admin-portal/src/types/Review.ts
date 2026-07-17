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
