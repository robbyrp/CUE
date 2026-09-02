package com.cue.demo.dtos.profile;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserProfileUpdateRequestDTO(
        Long userId,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String email,
        @NotBlank String city,
        @NotBlank String profilePictureUrl,
        @NotBlank String bio
) { }
