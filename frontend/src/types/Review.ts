/**Server response to the create review POST Request */
export interface Review {
    id?: number;
    authorUsername: string;
    authorProfilePictureUrl: string | null;
    createdAt: string;
    stars: number;
    hearts: number;
    text: string;
    isSpoiler: boolean;
}

/**Client POST and PUT Request BODY */
export interface ReviewRequest {
    stars: number;
    text: string;
    isSpoiler: boolean;
}
