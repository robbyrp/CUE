import apiClient from "./Api";
import type { UserProfile } from "../types/UserProfile";
import type { UserProfileUpdateRequest } from "../types/UserProfileUpdateRequest";

const ENDPOINTS = {
    GET_CURRENT_USER: `/profile/me`,
    UPDATE_USER_PROFILE: `/profile/me/update`
};

export const UserProfileService = {
    get_current_user: async (): Promise<UserProfile> => {
        const response = await apiClient.get(ENDPOINTS.GET_CURRENT_USER);
        return response.data;
    },

    update_user_profile: async (updateRequest: UserProfileUpdateRequest): Promise<UserProfile> => {
        const response = await apiClient.put(ENDPOINTS.UPDATE_USER_PROFILE, updateRequest);
        return response.data;
    }
}
