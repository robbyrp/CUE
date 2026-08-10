import axios from 'axios';
import type {AxiosError, InternalAxiosRequestConfig} from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
});

api.interceptors.request.use((config : InternalAxiosRequestConfig) => {
    const userId = localStorage.getItem('userId');
    if (userId) {
        config.headers['X-User-Id'] = userId;
    }
    return config;
},
    (error: AxiosError) => {
        return Promise.reject(error);
    });

export default api;