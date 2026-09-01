package com.cue.demo.mapper;

import com.cue.demo.dtos.profile.UserProfileUpdateRequestDTO;
import com.cue.demo.entities.User;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public final class UserProfileMapper {

    public void updateUserEntityFromUserProfileDTO(final User user, final UserProfileUpdateRequestDTO profile) {

        Objects.requireNonNull(profile);
        Objects.requireNonNull(user);

        if (!Objects.equals(user.getFirstName(), profile.firstName())) {
            user.setFirstName(profile.firstName());
        }

        if (!Objects.equals(user.getLastName(), profile.lastName())) {
            user.setLastName(profile.lastName());
        }

        if (!Objects.equals(user.getEmail(), profile.email())) {
            user.setEmail(profile.email());
        }

        if (!Objects.equals(user.getCity(), profile.city())) {
            user.setCity(profile.city());
        }

        if (!Objects.equals(user.getProfilePictureUrl(), profile.profilePictureUrl())) {
            user.setProfilePictureUrl(profile.profilePictureUrl());
        }

        if (!Objects.equals(user.getBio(), profile.bio())) {
            user.setBio(profile.bio());
        }

    }

}
