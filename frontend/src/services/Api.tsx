import axios from 'axios';
import type { AxiosError, InternalAxiosRequestConfig } from 'axios';

export const LOCAL_STORAGE_TOKEN_NAME = 'token';

const apiClient = axios.create({
    baseURL: 'http://localhost:8080/api',
});


apiClient.interceptors.request.use((config: InternalAxiosRequestConfig) => {

    const token = localStorage.getItem(LOCAL_STORAGE_TOKEN_NAME);

    if (token) {
        config.headers.set('Authorization', `Bearer ${token}`);
    }

    return config;
},
    (error: AxiosError) => {
        return Promise.reject(error);
    });

apiClient.interceptors.response.use(
    (response) => {
        return response;
    },
    (error: AxiosError) => {
        if (error.response?.status === 401) {
            localStorage.removeItem('token');
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
)

export default apiClient;