export interface Review {
    id?: number;
    createdAt: string;
    stars: number;
    hearts: number;
    text: string;
    isSpoiler: boolean;
}

