import apiClient from '../services/Api.tsx'
import type PerformancePortal from '../types/PerformancePortal.ts';
import type PerformanceCard from '../types/PerformanceCard.ts';
import type { SearchSuggestion as SearchSuggestionType } from '../types/SearchSuggestion.ts';
import type PageResponse from '../types/PageResponse.ts';

const ENDPOINTS = {
    CREATE                          : `/admin/spectacole`,
    GET_ALL                         : `/admin/spectacole`,
    DELETE                          : (id:number) => `/admin/spectacole/${id}`,
    GET_BY_ID                       : (id:number) => `/admin/spectacole/${id}`,
    UPDATE_BY_ID                    : (id:number) => `/admin/spectacole/${id}`,
    GET_SEARCH_TITLE_SUGGESTIONS    : `/search/suggestions`,
    GET_SEARCH_RESULTS              : `/search/performances`,
    GET_WATCH_LATER                 : `/user/me/watch-later`,
    GET_WATCHED                     : `/user/me/watched`,
    ADD_TO_WATCH_LATER              : (id:number) => `/user/me/watch-later/${id}`,
    ADD_TO_WATCHED                  : (id:number) => `/user/me/watched/${id}`,
    DELETE_FROM_WATCH_LATER         : (id:number) => `/user/me/watch-later/${id}`,
    DELETE_FROM_WATCHED             : (id:number) => `/user/me/watched/${id}`,
    EXISTS_IN_WATCH_LATER          : (id:number) => `/user/me/watch-later/${id}/exists`,
    EXISTS_IN_WATCHED              : (id:number) => `/user/me/watched/${id}/exists`,
};

function emptyPage <T>(page: number, size: number): PageResponse<T> {
    return {
        content: [],
        totalPages: 0,
        totalElements: 0,
        size,
        number: page
    }
};

export const PerformancePortalService = {

    createPerformance: async (pData : PerformancePortal): Promise<PerformancePortal> => {
        const response = await apiClient.post<PerformancePortal>(ENDPOINTS.CREATE, pData);
        return response.data;
    },

    //TODO: Modify return type if backend method deletePerformanceByID() will return 
    // the entity that was deleted instead of void.
    deletePerformance: async (id: number): Promise<void> => {
        await apiClient.delete(ENDPOINTS.DELETE(id));
    },

    getPerformanceById: async (id:number): Promise<PerformancePortal> => {
        const response = await apiClient.get<PerformancePortal>(ENDPOINTS.GET_BY_ID(id));
        return response.data;
    },

    updatePerformanceById: async (id:number, pData: PerformancePortal): Promise<PerformancePortal> => {
        const response = await apiClient.put<PerformancePortal>(ENDPOINTS.UPDATE_BY_ID(id), pData);
        return response.data
    },

    getAllPerformances: async (page: number, size: number): Promise<PageResponse<PerformancePortal>> => {
        const response = await apiClient.get(ENDPOINTS.GET_ALL, {
            params: {
                page: page,
                size: size
            }
        });
        return response.data;
    },

    getSearchTitleSuggestions: async (q: string): Promise<SearchSuggestionType[]> => {
        const response = await apiClient.get(ENDPOINTS.GET_SEARCH_TITLE_SUGGESTIONS, {
            params: {
                q:q
            }
        });
        return response.data;
    },

    getSearchResults: async (page: number, size: number, q: string): Promise<PageResponse<PerformanceCard>> => {
       const response = await apiClient.get(ENDPOINTS.GET_SEARCH_RESULTS, {
            params: {
                page: page,
                size: size,
                q: q
            }
       });
       return response.data;
    },

    getWatchLaterPerformances: async (page: number, size: number): Promise<PageResponse<PerformancePortal>> => {
        const response = await apiClient.get(ENDPOINTS.GET_WATCH_LATER, {
            params: {
                page: page,
                size: size
            }
        });
        return response.data;
    },

    getWatchedPerformances: async (page: number, size: number): Promise<PageResponse<PerformancePortal>> => {
        const response = await apiClient.get(ENDPOINTS.GET_WATCHED, {
            params: {
                page: page,
                size: size
            }
        });
        return response.data;
    },
    
    getReviewedPerformances: async (page: number, size: number): Promise<PageResponse<PerformancePortal>> => {
        return emptyPage<PerformancePortal>(page, size);
    },

    addToWatchLater: async (id: number) : Promise<void> => {
        const response = await apiClient.post(ENDPOINTS.ADD_TO_WATCH_LATER(id));
        return response.data;
    },

    addToWatched: async (id: number) : Promise<void> => {
        const response = await apiClient.post(ENDPOINTS.ADD_TO_WATCHED(id));
        return response.data;
    },

    deleteFromWatchLater: async (id: number) : Promise<void> => {
        await apiClient.delete(ENDPOINTS.DELETE_FROM_WATCH_LATER(id));
    },

    deleteFromWatched: async (id: number) : Promise<void> => {
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