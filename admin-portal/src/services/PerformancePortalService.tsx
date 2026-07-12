import axios from 'axios';
import type PerformancePortalType from '../types/PerformancePortalType.tsx'
import type PageResponse from '../types/PageResponse.tsx';

const API_BASE_URL = "http://localhost:8080/api/admin";

const ENDPOINTS = {
    CREATE      : `${API_BASE_URL}/spectacole`, // E ACELASI LUCRU CU: API_BASE_URL.concat("/create"),
    GET_ALL     : `${API_BASE_URL}/spectacole`,
    DELETE      : (id:number) =>  `${API_BASE_URL}/spectacole/${id}`,
    GET_BY_ID   : (id:number) => `${API_BASE_URL}/spectacole/${id}`,
    UPDATE_BY_ID: (id:number) => `${API_BASE_URL}/spectacole/${id}`
};

export const PerformancePortalService = {

    createPerformance: async (pData : PerformancePortalType): Promise<PerformancePortalType> => {
        const response = await axios.post<PerformancePortalType>(ENDPOINTS.CREATE, pData);
        return response.data;
    },

    //TODO: Modify return type if backend method deletePerformanceByID() will return 
    // the entity that was deleted instead of void.
    deletePerformance: async (id: number): Promise<void> => {
        await axios.delete(ENDPOINTS.DELETE(id));
    },

    getPerformanceById: async (id:number): Promise<PerformancePortalType> => {
        const response = await axios.get<PerformancePortalType>(ENDPOINTS.GET_BY_ID(id));
        return response.data;
    },

    updatePerformanceById: async (id:number, pData: PerformancePortalType): Promise<PerformancePortalType> => {
        const response = await axios.put<PerformancePortalType>(ENDPOINTS.UPDATE_BY_ID(id), pData);
        return response.data
    },

    getAllPerformances: async (page: number, size: number): Promise<PageResponse<PerformancePortalType>> => {
        const response = await axios.get(ENDPOINTS.GET_ALL, {
            params: {
                page: page,
                size: size
            }
        });
        return response.data;
    }


};