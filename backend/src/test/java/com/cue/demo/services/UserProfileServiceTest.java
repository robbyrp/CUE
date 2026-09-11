package com.cue.demo.services;

import com.cue.demo.dtos.profile.UserProfileDTO;
import com.cue.demo.dtos.profile.UserProfileUpdateRequestDTO;
import com.cue.demo.entities.User;
import com.cue.demo.enums.UserRole;
import com.cue.demo.exceptions.UserNotAuthorizedException;
import com.cue.demo.exceptions.UserNotFoundException;
import com.cue.demo.mapper.UserProfileMapper;
import com.cue.demo.repositories.ReviewRepository;
import com.cue.demo.repositories.UserRepository;
import com.cue.demo.repositories.WatchLaterPerformanceItemRepository;
import com.cue.demo.repositories.WatchedPerformanceItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTest {
    @Mock private UserRepository userRepository;
    @Mock private UserProfileMapper userProfileMapper;
    @Mock private ReviewRepository reviewRepository;
    @Mock private WatchLaterPerformanceItemRepository watchLaterPerformanceItemRepository;
    @Mock private WatchedPerformanceItemRepository watchedPerformanceItemRepository;
    @InjectMocks private UserProfileService service;

    /**
     * Test case for retrieving a user profile with a valid user ID.
     * Verifies that the service correctly aggregates user data and statistics.
     */
    @Test
    void givenValidUserId_whenGetCurrentUserProfileById_thenReturnUserProfile() {
        final Long existingUserId = 1L;
        final Integer counter = 100;
        User testUser = User.builder().id(existingUserId).build();
        UserProfileDTO expected = UserProfileDTO.builder()
                .id(existingUserId)
                .city("Constanța")
                .reviewedCount(counter)
                .watchLaterCount(counter)
                .watchedCount(counter)
                .build();

        Mockito.when(userRepository.findById(existingUserId)).thenReturn(Optional.of(testUser));
        Mockito.when(reviewRepository.countByUser_Id(existingUserId)).thenReturn(counter);
        Mockito.when(watchedPerformanceItemRepository.countByUser_Id(existingUserId)).thenReturn(counter);
        Mockito.when(watchLaterPerformanceItemRepository.countByUser_Id(existingUserId)).thenReturn(counter);

        UserProfileDTO result = service.getCurrentUserProfileById(existingUserId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expected, result);

        Mockito.verify(userRepository, Mockito.times(1)).findById(existingUserId);
        Mockito.verify(reviewRepository, Mockito.times(1)).countByUser_Id(existingUserId);
        Mockito.verify(watchedPerformanceItemRepository, Mockito.times(1)).countByUser_Id(existingUserId);
        Mockito.verify(watchLaterPerformanceItemRepository, Mockito.times(1)).countByUser_Id(existingUserId);
    }

    /**
     * Test case for attempting to retrieve a user profile with an invalid user ID.
     * Verifies that {@link UserNotFoundException} is thrown when the user does not exist.
     */
    @Test
    void givenInvalidUserId_whenGetCurrentUserProfileById_thenThrowUserNotFoundException() {
        final Long invalidUserId = 1L;
        Mockito.when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> service.getCurrentUserProfileById(invalidUserId));
    }

    /**
     * Test case for a user successfully updating their own profile.
     * Verifies that the user entity is updated and the returned DTO reflects the changes.
     */
    @Test
    void givenValidData_whenUserUpdateCurrentUserProfile_thenReturnUpdatedUserProfile() {
        final Long existingUserId = 1L;
        final Integer counter = 100;
        final String username = "testUser";

        User testUser = User.builder()
                .id(existingUserId)
                .username(username)
                .role(UserRole.USER)
                .build();

        UserProfileUpdateRequestDTO request = UserProfileUpdateRequestDTO.builder()
                .userId(existingUserId)
                .firstName("newFirstName")
                .lastName("newLastName")
                .email("newEmail@example.com")
                .city("newCity")
                .profilePictureUrl("newUrl")
                .bio("newBio")
                .build();

        UserProfileDTO expected = UserProfileDTO.builder()
                .id(existingUserId)
                .username(username)
                .firstName("newFirstName")
                .lastName("newLastName")
                .email("newEmail@example.com")
                .city("newCity")
                .profilePictureUrl("newUrl")
                .bio("newBio")
                .reviewedCount(counter)
                .watchLaterCount(counter)
                .watchedCount(counter)
                .build();

        Mockito.when(userRepository.findById(existingUserId)).thenReturn(Optional.of(testUser));
        Mockito.doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            UserProfileUpdateRequestDTO dto = invocation.getArgument(1);
            new UserProfileMapper().updateUserEntityFromUserProfileDTO(user, dto);
            return null;
        }).when(userProfileMapper).updateUserEntityFromUserProfileDTO(Mockito.any(User.class), Mockito.any(UserProfileUpdateRequestDTO.class));

        Mockito.when(reviewRepository.countByUser_Id(existingUserId)).thenReturn(counter);
        Mockito.when(watchedPerformanceItemRepository.countByUser_Id(existingUserId)).thenReturn(counter);
        Mockito.when(watchLaterPerformanceItemRepository.countByUser_Id(existingUserId)).thenReturn(counter);

        UserProfileDTO result = service.updateCurrentUserProfile(existingUserId, request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expected, result);

        Mockito.verify(userRepository, Mockito.times(2)).findById(existingUserId);
        Mockito.verify(reviewRepository, Mockito.times(1)).countByUser_Id(existingUserId);
        Mockito.verify(watchedPerformanceItemRepository, Mockito.times(1)).countByUser_Id(existingUserId);
        Mockito.verify(watchLaterPerformanceItemRepository, Mockito.times(1)).countByUser_Id(existingUserId);
    }

    /**
     * Test case for attempting to update a profile with an invalid user ID in the request.
     * Verifies that {@link UserNotFoundException} is thrown before any update logic.
     */
    @Test
    void givenInvalidUserId_whenUpdateCurrentUserProfile_thenThrowUserNotFoundException() {
        final Long invalidUserId = 1L;
        UserProfileUpdateRequestDTO request = UserProfileUpdateRequestDTO.builder()
                .userId(invalidUserId)
                .build();

        Mockito.when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> service.updateCurrentUserProfile(invalidUserId, request));

        Mockito.verify(userRepository, Mockito.times(1)).findById(invalidUserId);
    }

    /**
     * Test case for an administrator updating another user's profile.
     * Verifies that the administrator has the authority to perform updates on other accounts.
     */
    @Test
    void givenAdminUser_whenUpdateCurrentUserProfile_thenReturnUpdatedUserProfile() {
        final Long adminUserId = 1L;
        final Long targetUserId = 2L;
        final Integer counter = 50;
        final String targetUsername = "targetUser";

        User adminUser = User.builder()
                .id(adminUserId)
                .role(UserRole.ADMIN)
                .build();

        User targetUser = User.builder()
                .id(targetUserId)
                .username(targetUsername)
                .role(UserRole.USER)
                .build();

        UserProfileUpdateRequestDTO request = UserProfileUpdateRequestDTO.builder()
                .userId(targetUserId)
                .firstName("newNameByAdmin")
                .lastName("newLastNameByAdmin")
                .build();

        UserProfileDTO expected = UserProfileDTO.builder()
                .id(targetUserId)
                .username(targetUsername)
                .firstName("newNameByAdmin")
                .lastName("newLastNameByAdmin")
                .reviewedCount(counter)
                .watchLaterCount(counter)
                .watchedCount(counter)
                .build();

        Mockito.when(userRepository.findById(adminUserId)).thenReturn(Optional.of(adminUser));
        Mockito.when(userRepository.findById(targetUserId)).thenReturn(Optional.of(targetUser));
        Mockito.doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            UserProfileUpdateRequestDTO dto = invocation.getArgument(1);
            new UserProfileMapper().updateUserEntityFromUserProfileDTO(user, dto);
            return null;
        }).when(userProfileMapper).updateUserEntityFromUserProfileDTO(Mockito.any(User.class), Mockito.any(UserProfileUpdateRequestDTO.class));

        Mockito.when(reviewRepository.countByUser_Id(targetUserId)).thenReturn(counter);
        Mockito.when(watchedPerformanceItemRepository.countByUser_Id(targetUserId)).thenReturn(counter);
        Mockito.when(watchLaterPerformanceItemRepository.countByUser_Id(targetUserId)).thenReturn(counter);

        UserProfileDTO result = service.updateCurrentUserProfile(adminUserId, request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expected, result);

        Mockito.verify(userRepository, Mockito.times(1)).findById(adminUserId);
        Mockito.verify(userRepository, Mockito.times(1)).findById(targetUserId);
    }

    /**
     * Test case for when a non-admin user attempts to update another user's profile.
     * Should throw {@link UserNotAuthorizedException}.
     */
    @Test
    void givenUnauthorizedUser_whenUpdateCurrentUserProfile_thenThrowUserNotAuthorizedException() {
        final Long principalUserId = 1L;
        final Long targetUserId = 2L;

        User principalUser = User.builder()
                .id(principalUserId)
                .role(UserRole.USER)
                .build();

        User targetUser = User.builder()
                .id(targetUserId)
                .role(UserRole.USER)
                .build();

        UserProfileUpdateRequestDTO request = UserProfileUpdateRequestDTO.builder()
                .userId(targetUserId)
                .build();

        Mockito.when(userRepository.findById(principalUserId)).thenReturn(Optional.of(principalUser));
        Mockito.when(userRepository.findById(targetUserId)).thenReturn(Optional.of(targetUser));

        Assertions.assertThrows(UserNotAuthorizedException.class, () -> service.updateCurrentUserProfile(principalUserId, request));

        Mockito.verify(userRepository, Mockito.times(1)).findById(principalUserId);
        Mockito.verify(userRepository, Mockito.times(1)).findById(targetUserId);
        Mockito.verifyNoInteractions(userProfileMapper, reviewRepository, watchedPerformanceItemRepository, watchLaterPerformanceItemRepository);
    }

}
