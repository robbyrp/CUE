import type { LoginRequest, LoginResponse } from "../types/LoginInterfaces";
import type { RegisterUserRequest } from "../types/RegisterUserRequest";
import apiClient from "./Api";

const ENDPOINTS = {
    LOGIN: `/auth/login `,
    REGISTER: `/auth/register`
};

export const AuthService = {

    login: async (request: LoginRequest): Promise<LoginResponse> => {
        const response = await apiClient.post(ENDPOINTS.LOGIN, request);
        return response.data;
    },

    register: async (request: RegisterUserRequest): Promise<LoginResponse> => {
        const response = await apiClient.post(ENDPOINTS.REGISTER, request);
        return response.data;
    }

}