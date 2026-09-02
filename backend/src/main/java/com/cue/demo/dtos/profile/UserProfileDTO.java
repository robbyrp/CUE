package com.cue.demo.dtos.profile;

import lombok.Builder;

@Builder
public record UserProfileDTO(
        Long id,
        String username,
        String firstName,
        String lastName,
        String email,
        String city,
        String profilePictureUrl,
        String bio,
        Integer reviewedCount,
        Integer watchLaterCount,
        Integer watchedCount
) {
}
