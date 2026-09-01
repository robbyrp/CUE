package com.cue.demo.dtos.profile;

import lombok.Builder;

@Builder
public record UserProfileUpdateRequestDTO(
    Long userId,
    String firstName,
    String lastName,
    String email,
    String city,
    String profilePictureUrl,
    String bio
) { }
