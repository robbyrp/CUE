package com.cue.demo.dtos.profile;

public record UserProfileUpdateRequestDTO(
    String firstName,
    String lastName,
    String email,
    String city,
    String profilePictureUrl,
    String bio
) { }
