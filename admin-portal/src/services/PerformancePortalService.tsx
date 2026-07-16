import axios from 'axios';
import type PerformancePortal from '../types/PerformancePortal.ts';
import type PerformanceCard from '../types/PerformanceCard.ts';
import type { SearchSuggestion as SearchSuggestionType } from '../types/SearchSuggestion.ts';
import type PageResponse from '../types/PageResponse.ts';

const API_ADMIN_BASE_URL = "http://localhost:8080/api/admin";
const API_SEARCH_BASE_URL = "http://localhost:8080/api/search";

const ENDPOINTS = {
    CREATE                          : `${API_ADMIN_BASE_URL}/spectacole`,
    GET_ALL                         : `${API_ADMIN_BASE_URL}/spectacole`,
    DELETE                          : (id:number) =>  `${API_ADMIN_BASE_URL}/spectacole/${id}`,
    GET_BY_ID                       : (id:number) => `${API_ADMIN_BASE_URL}/spectacole/${id}`,
    UPDATE_BY_ID                    : (id:number) => `${API_ADMIN_BASE_URL}/spectacole/${id}`,
    GET_SEARCH_TITLE_SUGGESTIONS    : `${API_SEARCH_BASE_URL}/suggestions`,
    GET_SEARCH_RESULTS              : `${API_SEARCH_BASE_URL}/performances`
};

export const PerformancePortalService = {

    createPerformance: async (pData : PerformancePortal): Promise<PerformancePortal> => {
        const response = await axios.post<PerformancePortal>(ENDPOINTS.CREATE, pData);
        return response.data;
    },

    //TODO: Modify return type if backend method deletePerformanceByID() will return 
    // the entity that was deleted instead of void.
    deletePerformance: async (id: number): Promise<void> => {
        await axios.delete(ENDPOINTS.DELETE(id));
    },

    getPerformanceById: async (id:number): Promise<PerformancePortal> => {
        const response = await axios.get<PerformancePortal>(ENDPOINTS.GET_BY_ID(id));
        return response.data;
    },

    updatePerformanceById: async (id:number, pData: PerformancePortal): Promise<PerformancePortal> => {
        const response = await axios.put<PerformancePortal>(ENDPOINTS.UPDATE_BY_ID(id), pData);
        return response.data
    },

    getAllPerformances: async (page: number, size: number): Promise<PageResponse<PerformancePortal>> => {
        const response = await axios.get(ENDPOINTS.GET_ALL, {
            params: {
                page: page,
                size: size
            }
        });
        return response.data;
    },

    getSearchTitleSuggestions: async (q: string): Promise<SearchSuggestionType[]> => {
        const response = await axios.get(ENDPOINTS.GET_SEARCH_TITLE_SUGGESTIONS, {
            params: {
                q:q
            }
        });
        return response.data;
    },

    getSearchResults: async (page: number, size: number, q: string): Promise<PageResponse<PerformanceCard>> => {
       const response = await axios.get(ENDPOINTS.GET_SEARCH_RESULTS, {
            params: {
                page: page,
                size: size,
                q: q
            }
       });
       return response.data;
    }


};