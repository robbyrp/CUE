import apiClient from './Api.tsx'
import type PageResponse from '../types/PageResponse.ts';
import type { PageRequest } from '../types/PageRequest.ts';
import type { Review } from '../types/Review.ts';


const ENDPOINTS = {
    CREATE_REVIEW: (performanceId: number) => `/reviews/spectacole/${performanceId}`,
    UPDATE_REVIEW: (reviewId: number) => `/reviews/${reviewId}`,
    GET_PERFORMANCE_REVIEWS: (performanceId: number) => `/reviews/spectacole/${performanceId}`,
    GET_MY_REVIEW: (performanceId: number) => `/reviews/spectacole/${performanceId}/me`,
    TOGGLE_HEART_REVIEW: (reviewId: number) => `/reviews/${reviewId}/heart`,
    GET_MY_REVIEWS: `/reviews/me/all`,
    DELETE_REVIEW: (reviewId: number) => `/reviews/${reviewId}`,
    GET_CURRENT_USER: `/profile/me`,
    UPDATE_USER_PROFILE: `/profile/me/update`,
};



export const ReviewService = {
    createReview: async (performanceId: number, request: Review): Promise<Review> => {
        const response = await apiClient.post(ENDPOINTS.CREATE_REVIEW(performanceId), request);
        return response.data;
    },

    updateReview: async (reviewId: number, request: Review): Promise<Review> => {
        const response = await apiClient.post(ENDPOINTS.UPDATE_REVIEW(reviewId), request);
        return response.data;
    },

    getPerformanceReviews: async (performanceId: number, requestQueryParams: PageRequest): Promise<PageResponse<Review>> => {
        const response = await apiClient.get(ENDPOINTS.GET_PERFORMANCE_REVIEWS(performanceId), {
            params: requestQueryParams
        });
        return response.data;
    },

    getMyReview: async (performanceId: number): Promise<Review> => {
        const response = await apiClient.get(ENDPOINTS.GET_MY_REVIEW(performanceId));
        return response.data;
    },

    toggleHeartReview: async (reviewId: number): Promise<void> => {
        const response = await apiClient.put(ENDPOINTS.TOGGLE_HEART_REVIEW(reviewId));
        return response.data;
    },

    getMyReviews: async (requestQueryParams: PageRequest): Promise<PageResponse<Review>> => {
        const response = await apiClient.get(ENDPOINTS.GET_MY_REVIEWS, {
            params: requestQueryParams
        });
        return response.data;
    },

    deleteReview: async (reviewId: number): Promise<Review> => {
        const response = await apiClient.delete(ENDPOINTS.DELETE_REVIEW(reviewId));
        return response.data;
    }
};
