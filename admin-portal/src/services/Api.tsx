import axios from 'axios';
import type { AxiosError, InternalAxiosRequestConfig } from 'axios';
import PerformancePortalService from '../services/PerformancePortalService';

const apiClient = axios.create({
    baseURL: 'http://localhost:8080/api',
});

apiClient.interceptors.request.use((config: InternalAxiosRequestConfig) => {
    const userId = sessionStorage.getItem('userId');

    if (userId) {
        config.headers.set('X-User-Id', userId);
    }
    return config;
},
    (error: AxiosError) => {
        return Promise.reject(error);
    });

export default apiClient;