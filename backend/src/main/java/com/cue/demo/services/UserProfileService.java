package com.cue.demo.services;

import com.cue.demo.dtos.profile.UserProfileDTO;
import com.cue.demo.dtos.profile.UserProfileUpdateRequestDTO;
import com.cue.demo.entities.User;
import com.cue.demo.exceptions.UserNotAuthorizedException;
import com.cue.demo.exceptions.UserNotFoundException;
import com.cue.demo.mapper.UserProfileMapper;
import com.cue.demo.repositories.ReviewRepository;
import com.cue.demo.repositories.UserRepository;
import com.cue.demo.repositories.WatchLaterPerformanceItemRepository;
import com.cue.demo.repositories.WatchedPerformanceItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing user profiles.
 * Provides functionality for retrieving and updating profile information.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserProfileService {
    private final UserRepository userRepository;
    private final UserProfileMapper userProfileMapper;
    private final ReviewRepository reviewRepository;
    private final WatchLaterPerformanceItemRepository watchLaterPerformanceItemRepository;
    private final WatchedPerformanceItemRepository watchedPerformanceItemRepository;

    /**
     * Retrieves the current profile of a user based on their ID.
     *
     * @param userId The ID of the user whose profile is requested.
     * @return A {@link UserProfileDTO} object containing the profile data.
     * @throws UserNotFoundException If the user with the specified ID is not found.
     */
    public UserProfileDTO getCurrentUserProfileById(final Long userId)
            throws UserNotFoundException {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return getUserProfileDTO(userId, user);
    }

    /**
     * Updates the current user's profile.
     * Allows updates only if the user is an admin or is updating their own profile.
     *
     * @param userId The ID of the user performing the update (or the target ID).
     * @param request New data for updating the profile.
     * @return A {@link UserProfileDTO} reflecting the changes made.
     * @throws UserNotFoundException If the user is not found.
     * @throws UserNotAuthorizedException If the user does not have permission to edit this profile.
     */
    @Transactional
    public UserProfileDTO updateCurrentUserProfile(final Long userId, final UserProfileUpdateRequestDTO request)
            throws UserNotFoundException, UserNotAuthorizedException
    {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        boolean canEdit = user.isAdmin() || user.getId().equals(userId);

        if (!canEdit) { throw new UserNotAuthorizedException(user.getId());}

        userProfileMapper.updateUserEntityFromUserProfileDTO(user, request);

        return getUserProfileDTO(userId, user);
    }

    /**
     * Helper method to build a {@link UserProfileDTO} from a {@link User} entity
     * and aggregate statistics (reviews, watch later, and watched counts).
     *
     * @param userId The ID of the user.
     * @param user The user entity.
     * @return The profile DTO populated with statistics.
     */
    private UserProfileDTO getUserProfileDTO(Long userId, User user) {
        Integer reviewCount = reviewRepository.countByUser_Id(userId);
        Integer watchLaterCount = watchLaterPerformanceItemRepository.countByUser_Id(userId);
        Integer watchedPerformanceCount = watchedPerformanceItemRepository.countByUser_Id(userId);

        return UserProfileDTO.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .city(user.getCity())
                .profilePictureUrl(user.getProfilePictureUrl())
                .bio(user.getBio())
                .reviewedCount(reviewCount)
                .watchLaterCount(watchLaterCount)
                .watchedCount(watchedPerformanceCount)
                .build();
    }
}
