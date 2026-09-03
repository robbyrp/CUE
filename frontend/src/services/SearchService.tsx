import apiClient from './Api.tsx';
import type PageResponse from '../types/PageResponse.ts';
import type { SearchSuggestion } from '../types/SearchSuggestion.ts';
import type PerformanceCard from '../types/PerformanceCard';

const ENDPOINTS = {
    GET_SEARCH_TITLE_SUGGESTIONS: `/search/suggestions`,
    GET_SEARCH_RESULTS: `/search/performances`
};

export const SearchService = {
    getSearchTitleSuggestions: async (q: string): Promise<SearchSuggestion[]> => {
        const response = await apiClient.get(ENDPOINTS.GET_SEARCH_TITLE_SUGGESTIONS, {
            params: {
                q: q
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
};
