import apiClient from '../services/Api.tsx'
import type Performance from '../types/Performance.ts';
import type PageResponse from '../types/PageResponse';
import type { PageRequest } from '../types/PageRequest.ts';
import { emptyPage } from '../types/EmtpyPage.tsx';

const ENDPOINTS = {
    GET_SEARCH_TITLE_SUGGESTIONS: `/search/suggestions`,
    GET_SEARCH_RESULTS: `/search/performances`,
    GET_WATCH_LATER: `/user/me/watch-later`,
    GET_WATCHED: `/user/me/watched`,
    ADD_TO_WATCH_LATER: (id: number) => `/user/me/watch-later/${id}`,
    ADD_TO_WATCHED: (id: number) => `/user/me/watched/${id}`,
    DELETE_FROM_WATCH_LATER: (id: number) => `/user/me/watch-later/${id}`,
    DELETE_FROM_WATCHED: (id: number) => `/user/me/watched/${id}`,
    EXISTS_IN_WATCH_LATER: (id: number) => `/user/me/watch-later/${id}/exists`,
    EXISTS_IN_WATCHED: (id: number) => `/user/me/watched/${id}/exists`
};

export const UserService = {

    getWatchLaterPerformances: async (requestQueryParams: PageRequest): Promise<PageResponse<Performance>> => {
        const response = await apiClient.get(ENDPOINTS.GET_WATCH_LATER, {
            params: requestQueryParams
        });
        return response.data;
    },

    getWatchedPerformances: async (requestQueryParams: PageRequest): Promise<PageResponse<Performance>> => {
        const response = await apiClient.get(ENDPOINTS.GET_WATCHED, {
            params: requestQueryParams
        });
        return response.data;
    },

    getReviewedPerformances: async (requestQueryParams: PageRequest): Promise<PageResponse<Performance>> => {
        return emptyPage<Performance>(requestQueryParams);
    },

    addToWatchLater: async (id: number): Promise<void> => {
        const response = await apiClient.post(ENDPOINTS.ADD_TO_WATCH_LATER(id));
        return response.data;
    },

    addToWatched: async (id: number): Promise<void> => {
        const response = await apiClient.post(ENDPOINTS.ADD_TO_WATCHED(id));
        return response.data;
    },

    deleteFromWatchLater: async (id: number): Promise<void> => {
        await apiClient.delete(ENDPOINTS.DELETE_FROM_WATCH_LATER(id));
    },

    deleteFromWatched: async (id: number): Promise<void> => {
        await apiClient.delete(ENDPOINTS.DELETE_FROM_WATCHED(id));
    },

    isInWatchLater: async (id: number): Promise<boolean> => {
        const response = await apiClient.get(ENDPOINTS.EXISTS_IN_WATCH_LATER(id));
        return response.data;
    },

    isInWatched: async (id: number): Promise<boolean> => {
        const response = await apiClient.get(ENDPOINTS.EXISTS_IN_WATCHED(id));
        return response.data;
    }
};
