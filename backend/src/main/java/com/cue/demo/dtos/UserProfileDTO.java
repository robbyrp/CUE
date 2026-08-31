package com.cue.demo.dtos;

import com.cue.demo.enums.UserRole;
import lombok.Builder;

import java.util.Set;

@Builder
public record UserProfileDTO(
        Long id,
        UserRole role,
        String username,
        String firstName,
        String lastName,
        String email,
        String city,
        String profilePictureUrl,
        String bio,
        Set<ReviewDTO> reviews
) {
}
