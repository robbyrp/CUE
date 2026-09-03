import apiClient from './Api.tsx';
import type Performance from '../types/Performance.ts'
import type PageResponse from '../types/PageResponse.ts';
import type { PageRequest } from '../types/PageRequest.ts';

const ENDPOINTS = {
    CREATE: `/spectacole/admin`,
    GET_ALL: `/spectacole/`,
    GET_BY_ID: (id: number) => `/spectacole/${id}`,
    DELETE: (id: number) => `/spectacole/${id}/admin`,
    UPDATE_BY_ID: (id: number) => `/spectacole/${id}/admin`,
};

export const PerformanceService = {
    createPerformance: async (pData: Performance): Promise<Performance> => {
        const response = await apiClient.post<Performance>(ENDPOINTS.CREATE, pData);
        return response.data;
    },

    deletePerformance: async (id: number): Promise<void> => {
        await apiClient.delete(ENDPOINTS.DELETE(id));
    },

    getPerformanceById: async (id: number): Promise<Performance> => {
        const response = await apiClient.get<Performance>(ENDPOINTS.GET_BY_ID(id));
        return response.data;
    },

    updatePerformanceById: async (id: number, pData: Performance): Promise<Performance> => {
        const response = await apiClient.put<Performance>(ENDPOINTS.UPDATE_BY_ID(id), pData);
        return response.data
    },

    getPerformancesByCategory: async (requestQueryParams: PageRequest): Promise<PageResponse<Performance>> => {
        const response = await apiClient.get(ENDPOINTS.GET_ALL, {
            params: requestQueryParams
        });
        return response.data;
    }
};
