package com.cue.demo.services;

import com.cue.demo.dtos.review.ReviewDTO;
import com.cue.demo.entities.Performance;
import com.cue.demo.entities.Review;
import com.cue.demo.entities.User;
import com.cue.demo.enums.UserRole;
import com.cue.demo.exceptions.PerformanceNotFoundByIdException;
import com.cue.demo.exceptions.ReviewAlreadyExistsException;
import com.cue.demo.exceptions.ReviewNotFoundException;
import com.cue.demo.exceptions.UserNotAuthorizedException;
import com.cue.demo.mapper.ReviewMapper;
import com.cue.demo.repositories.PerformanceRepository;
import com.cue.demo.repositories.ReviewRepository;
import com.cue.demo.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public final class ReviewServiceTest {

    @Mock private ReviewRepository reviewRepository;
    @Mock private PerformanceRepository performanceRepository;
    @Mock private UserRepository userRepository;
    @Mock private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewService service;

    final Long requestHeaderUserId = 10L;
    final Long existingPerformanceId = 10L;
    /** For PUT endpoint **/
    final Long pathVariableReviewId = 1L;
    /** For PUT endpoint, so that an Admin can edit any review **/
    final Long requestHeaderAdminUserId = 0L;
    final Long invalidPerformanceId = 999L;

    /**
     * Tests the successful creation of a review.
     */
    @Test
    void givenValidData_whenCreateReview_thenCreateReview ()
    {
        final Integer starsNumber = 3;
        final String description = "Test description";
        final boolean spoiler = false;
        final Long newReviewId = 1L;

        User testUser = User.builder().id(requestHeaderUserId).role(UserRole.USER).build();
        Performance performanceMock = Performance.builder().id(existingPerformanceId).build();
        ReviewDTO requestBodyReviewDTO = ReviewDTO.builder().stars(starsNumber).text(description).isSpoiler(spoiler).build();
        Review reviewMock = Review.builder().id(newReviewId).performance(performanceMock).user(testUser).stars(starsNumber).text(description).isSpoiler(spoiler).build();
        ReviewDTO expectedReviewDTO = ReviewDTO.builder().id(newReviewId).stars(starsNumber).text(description).isSpoiler(spoiler).build();

        Mockito.when(reviewRepository.existsByUser_IdAndPerformance_Id(requestHeaderUserId, existingPerformanceId)).thenReturn(false);
        Mockito.when(userRepository.getReferenceById(requestHeaderUserId)).thenReturn(testUser);
        Mockito.when(performanceRepository.findById(existingPerformanceId)).thenReturn(Optional.of(performanceMock));
        Mockito.when(reviewRepository.saveAndFlush(Mockito.any(Review.class))).thenReturn(reviewMock);
        Mockito.when(reviewMapper.fromReviewToReviewDTO(reviewMock)).thenReturn(expectedReviewDTO);

        ReviewDTO result = service.createReview(requestHeaderUserId, existingPerformanceId, requestBodyReviewDTO);

        Assertions.assertNull(result);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedReviewDTO, result);
        Mockito.verify(userRepository, Mockito.times(1)).getReferenceById(requestHeaderUserId);
        Mockito.verify(reviewMapper).fromReviewToReviewDTO(reviewMock);
    }

    /**
     * Tests that a ReviewAlreadyExistsException is thrown when a user attempts to review a performance again.
     */
    @Test
    void givenDuplicateUserIdAndPerformanceId_whenCreateReview_thenThrowReviewAlreadyExistsException ()
    {
        ReviewDTO requestBodyReviewDTO = ReviewDTO.builder().build();

        Mockito.when(reviewRepository.existsByUser_IdAndPerformance_Id(requestHeaderUserId, existingPerformanceId)).thenReturn(true);

        Assertions.assertThrows(ReviewAlreadyExistsException.class, () -> service.createReview(requestHeaderUserId, existingPerformanceId, requestBodyReviewDTO));
    }

    /**
     * Tests that a PerformanceNotFoundByIdException is thrown when the performance ID is invalid during review creation.
     */
    @Test
    void givenInvalidPerformanceId_whenCreateReview_thenThrowPerformanceNotFoundByIdException ()
    {
        User testUser = User.builder().id(requestHeaderUserId).role(UserRole.USER).build();
        ReviewDTO requestBodyReviewDTO = ReviewDTO.builder().build();

        Mockito.when(reviewRepository.existsByUser_IdAndPerformance_Id(requestHeaderUserId, existingPerformanceId)).thenReturn(false);
        Mockito.when(userRepository.getReferenceById(requestHeaderUserId)).thenReturn(testUser);
        Mockito.when(performanceRepository.findById(existingPerformanceId)).thenReturn(Optional.empty());

        Assertions.assertThrows(PerformanceNotFoundByIdException.class, () -> service.createReview(requestHeaderUserId, existingPerformanceId, requestBodyReviewDTO));

        Mockito.verify(userRepository, Mockito.times(1)).getReferenceById(requestHeaderUserId);
    }

    /**
     * Tests the case where the review author updates their own review.
     */
    @Test
    void givenUserIsReviewAuthor_whenUpdateReview_thenUpdateReview()
    {
        final Integer newStarsNumber = 5;
        final String newDescription = "New test description";

        final Integer oldStarsNumber = 3;
        final String oldDescription = "Test description";
        final boolean spoiler = false;
        final Long pathVariableReviewId = 1L;

        User testUser = User.builder().id(requestHeaderUserId).role(UserRole.USER).build();
        Performance performanceMock = Performance.builder().id(existingPerformanceId).build();
        ReviewDTO reviewDtoMock = ReviewDTO.builder().id(pathVariableReviewId).stars(newStarsNumber).text(newDescription).isSpoiler(spoiler).build();

        Review oldReviewMock = Review.builder().id(pathVariableReviewId).performance(performanceMock).user(testUser).stars(oldStarsNumber).text(oldDescription).isSpoiler(spoiler).build();
        Review newReviewMock = Review.builder().id(pathVariableReviewId).performance(performanceMock).user(testUser).stars(newStarsNumber).text(newDescription).isSpoiler(spoiler).build();
        ReviewDTO expectedNewReviewDto = ReviewDTO.builder().id(pathVariableReviewId).stars(newStarsNumber).text(newDescription).isSpoiler(spoiler).build();

        Mockito.when(reviewRepository.findById(pathVariableReviewId)).thenReturn(Optional.of(oldReviewMock));
        Mockito.when(userRepository.getReferenceById(requestHeaderUserId)).thenReturn(testUser);
        Mockito.when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(newReviewMock);
        Mockito.when(reviewMapper.fromReviewToReviewDTO(newReviewMock)).thenReturn(expectedNewReviewDto);

        ReviewDTO result = service.updateReview(requestHeaderUserId, pathVariableReviewId, reviewDtoMock);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedNewReviewDto, result);
        Mockito.verify(userRepository, Mockito.times(1)).getReferenceById(requestHeaderUserId);
        Mockito.verify(reviewMapper).fromReviewToReviewDTO(newReviewMock);
    }

    /**
     * Tests the case where an admin updates another user's review.
     */
    @Test
    void givenUserIsAdmin_whenUpdateReview_thenUpdateReview()
    {
        final Integer newStarsNumber = 5;
        final String newDescription = "New test description";

        final Integer oldStarsNumber = 3;
        final String oldDescription = "Test description";
        final boolean spoiler = false;

        final Long reviewAuthorUserId = 100L;
        User adminUser = User.builder().id(requestHeaderAdminUserId).role(UserRole.ADMIN).build();

        User testUser = User.builder().id(reviewAuthorUserId).role(UserRole.USER).build();
        Performance performanceMock = Performance.builder().id(existingPerformanceId).build();
        ReviewDTO reviewDtoMock = ReviewDTO.builder().id(pathVariableReviewId).stars(newStarsNumber).text(newDescription).isSpoiler(spoiler).build();

        Review oldReviewMock = Review.builder().id(pathVariableReviewId).performance(performanceMock).user(testUser).stars(oldStarsNumber).text(oldDescription).isSpoiler(spoiler).build();
        Review newReviewMock = Review.builder().id(pathVariableReviewId).performance(performanceMock).user(testUser).stars(newStarsNumber).text(newDescription).isSpoiler(spoiler).build();
        ReviewDTO expectedNewReviewDto = ReviewDTO.builder().id(pathVariableReviewId).stars(newStarsNumber).text(newDescription).isSpoiler(spoiler).build();

        Mockito.when(reviewRepository.findById(pathVariableReviewId)).thenReturn(Optional.of(oldReviewMock));
        Mockito.when(userRepository.getReferenceById(requestHeaderAdminUserId)).thenReturn(adminUser);
        Mockito.when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(newReviewMock);
        Mockito.when(reviewMapper.fromReviewToReviewDTO(newReviewMock)).thenReturn(expectedNewReviewDto);

        ReviewDTO result = service.updateReview(requestHeaderAdminUserId, pathVariableReviewId, reviewDtoMock);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedNewReviewDto, result);
        Mockito.verify(userRepository, Mockito.times(1)).getReferenceById(requestHeaderAdminUserId);
        Mockito.verify(reviewMapper).fromReviewToReviewDTO(newReviewMock);
    }

    /**
     * Tests the case where a user who is not an admin attempts to edit
     * a review created by another user.
     */
    @Test
    void givenUserIsNotAuthorAndNotAdmin_whenUpdateReview_thenThrowUserNotAuthorizedException() {
        final Long otherUserId = 200L;
        final Integer newStarsNumber = 5;
        final String newDescription = "Attempted edit";

        final Integer oldStarsNumber = 3;
        final String oldDescription = "Original text";

        User requester = User.builder().id(requestHeaderUserId).role(UserRole.USER).build();
        User otherUser = User.builder().id(otherUserId).role(UserRole.USER).build();

        ReviewDTO reviewDtoMock = ReviewDTO.builder()
                .id(pathVariableReviewId)
                .stars(newStarsNumber)
                .text(newDescription)
                .isSpoiler(false)
                .build();

        Review existingReview = Review.builder()
                .id(pathVariableReviewId)
                .user(otherUser)
                .stars(oldStarsNumber)
                .text(oldDescription)
                .build();

        Mockito.when(reviewRepository.findById(pathVariableReviewId)).thenReturn(Optional.of(existingReview));
        Mockito.when(userRepository.getReferenceById(requestHeaderUserId)).thenReturn(requester);

        Assertions.assertThrows(UserNotAuthorizedException.class, () ->
                service.updateReview(requestHeaderUserId, pathVariableReviewId, reviewDtoMock));

        Mockito.verify(userRepository, Mockito.times(1)).getReferenceById(requestHeaderUserId);
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any(Review.class));
    }

    /**
     * Tests the case where the reviewId from the path does not correspond to any
     * record in the database.
     */
    @Test
    void givenInvalidReviewId_whenUpdateReview_thenThrowReviewNotFoundException() {
        ReviewDTO reviewDtoMock = ReviewDTO.builder().id(pathVariableReviewId).build();

        Mockito.when(reviewRepository.findById(pathVariableReviewId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ReviewNotFoundException.class, () ->
                service.updateReview(requestHeaderUserId, pathVariableReviewId, reviewDtoMock));

        Mockito.verify(userRepository, Mockito.never()).findById(Mockito.anyLong());
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any(Review.class));
    }

    /**
     * Tests the successful retrieval of reviews for a given performance.
     */
    @Test
    void givenValidPerformanceId_whenGetPerformanceReviews_thenReturnPageOfReviewDTOs() {
        final Integer starsNumber = 4;
        final String description = "Test description";
        final Long reviewId = 1L;

        Pageable pageable = PageRequest.of(0, 10);
        Review reviewMock = Review.builder()
                .id(reviewId)
                .stars(starsNumber)
                .text(description)
                .build();
        ReviewDTO reviewDTOMock = ReviewDTO.builder()
                .id(reviewId)
                .stars(starsNumber)
                .text(description)
                .build();

        Page<Review> reviewPage = new PageImpl<>(List.of(reviewMock));

        Mockito.when(performanceRepository.existsById(existingPerformanceId)).thenReturn(true);
        Mockito.when(reviewRepository.findByPerformance_Id(existingPerformanceId, pageable)).thenReturn(reviewPage);
        Mockito.when(reviewMapper.fromReviewToReviewDTO(reviewMock)).thenReturn(reviewDTOMock);

        Page<ReviewDTO> result = service.getPerformanceReviews(existingPerformanceId, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals(reviewDTOMock, result.getContent().getFirst());
        Mockito.verify(performanceRepository).existsById(existingPerformanceId);
        Mockito.verify(reviewRepository).findByPerformance_Id(existingPerformanceId, pageable);
    }

    /**
     * Tests the case where the performanceId does not exist in the database.
     */
    @Test
    void givenInvalidPerformanceId_whenGetPerformanceReviews_thenThrowPerformanceNotFoundByIdException() {
        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(performanceRepository.existsById(invalidPerformanceId)).thenReturn(false);

        Assertions.assertThrows(PerformanceNotFoundByIdException.class, () ->
                service.getPerformanceReviews(invalidPerformanceId, pageable));

        Mockito.verify(reviewRepository, Mockito.never()).findByPerformance_Id(Mockito.anyLong(), Mockito.any());
    }

    /**
     * Tests the case where the user's review for a performance is successfully retrieved.
     */
    @Test
    void givenUserHasReviewedPerformance_whenGetMyReview_thenReturnOptionalReviewDTO() {
        final Integer starsNumber = 4;
        final String description = "My review";
        final Long reviewId = 1L;

        Review reviewMock = Review.builder()
                .id(reviewId)
                .stars(starsNumber)
                .text(description)
                .build();
        ReviewDTO reviewDTOMock = ReviewDTO.builder()
                .id(reviewId)
                .stars(starsNumber)
                .text(description)
                .build();

        Mockito.when(reviewRepository.findByUser_IdAndPerformance_Id(requestHeaderUserId, existingPerformanceId))
                .thenReturn(Optional.of(reviewMock));
        Mockito.when(reviewMapper.fromReviewToReviewDTO(reviewMock)).thenReturn(reviewDTOMock);

        Optional<ReviewDTO> result = service.getMyReview(requestHeaderUserId, existingPerformanceId);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(reviewDTOMock, result.get());
        Mockito.verify(reviewRepository).findByUser_IdAndPerformance_Id(requestHeaderUserId, existingPerformanceId);
    }

    /**
     * Tests the case where the user has not reviewed the performance.
     */
    @Test
    void givenUserHasNotReviewedPerformance_whenGetMyReview_thenReturnOptionalEmpty() {
        Mockito.when(reviewRepository.findByUser_IdAndPerformance_Id(requestHeaderUserId, existingPerformanceId))
                .thenReturn(Optional.empty());

        Optional<ReviewDTO> result = service.getMyReview(requestHeaderUserId, existingPerformanceId);

        Assertions.assertTrue(result.isEmpty());
        Mockito.verify(reviewRepository).findByUser_IdAndPerformance_Id(requestHeaderUserId, existingPerformanceId);
        Mockito.verify(reviewMapper, Mockito.never()).fromReviewToReviewDTO(Mockito.any());
    }

    /**
     * Tests the case where a user hearts a review (toggles from 0 to 1 heart).
     */
    @Test
    void givenUserHeartsReview_whenToggleHeartReview_thenAddHeart() {
        User requester = User.builder().id(requestHeaderUserId).role(UserRole.USER).build();
        Review reviewMock = Review.builder().id(pathVariableReviewId).build();

        Mockito.when(userRepository.getReferenceById(requestHeaderUserId)).thenReturn(requester);
        Mockito.when(reviewRepository.findById(pathVariableReviewId)).thenReturn(Optional.of(reviewMock));

        service.toggleHeartReview(requestHeaderUserId, pathVariableReviewId);

        Assertions.assertTrue(reviewMock.getHeartedByUsers().contains(requester));
        Mockito.verify(userRepository, Mockito.times(1)).getReferenceById(requestHeaderUserId);
        Mockito.verify(reviewRepository).save(reviewMock);
    }

    /**
     * Tests the case where a user unhearts a review (toggles from 1 to 0 hearts).
     */
    @Test
    void givenUserUnheartsReview_whenToggleHeartReview_thenRemoveHeart() {
        User requester = User.builder().id(requestHeaderUserId).role(UserRole.USER).build();
        java.util.Set<User> hearts = new java.util.HashSet<>();
        hearts.add(requester);
        Review reviewMock = Review.builder().id(pathVariableReviewId).heartedByUsers(hearts).build();

        Mockito.when(userRepository.getReferenceById(requestHeaderUserId)).thenReturn(requester);
        Mockito.when(reviewRepository.findById(pathVariableReviewId)).thenReturn(Optional.of(reviewMock));

        service.toggleHeartReview(requestHeaderUserId, pathVariableReviewId);

        Assertions.assertFalse(reviewMock.getHeartedByUsers().contains(requester));
        Mockito.verify(userRepository, Mockito.times(1)).getReferenceById(requestHeaderUserId);
        Mockito.verify(reviewRepository).save(reviewMock);
    }

    /**
     * Tests the case where the review does not exist when toggling a heart.
     */
    @Test
    void givenInvalidReviewId_whenToggleHeartReview_thenThrowReviewNotFoundException() {
        User requester = User.builder().id(requestHeaderUserId).role(UserRole.USER).build();

        Mockito.when(userRepository.getReferenceById(requestHeaderUserId)).thenReturn(requester);
        Mockito.when(reviewRepository.findById(pathVariableReviewId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ReviewNotFoundException.class, () ->
                service.toggleHeartReview(requestHeaderUserId, pathVariableReviewId));

        Mockito.verify(userRepository, Mockito.times(1)).getReferenceById(requestHeaderUserId);
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
    }

    /**
     * Tests the case where the review author successfully deletes their own review.
     */
    @Test
    void givenUserIsReviewAuthor_whenDeleteReview_thenDeleteReview() {
        User author = User.builder().id(requestHeaderUserId).role(UserRole.USER).build();
        Review reviewMock = Review.builder().id(pathVariableReviewId).user(author).build();

        Mockito.when(userRepository.getReferenceById(requestHeaderUserId)).thenReturn(author);
        Mockito.when(reviewRepository.findById(pathVariableReviewId)).thenReturn(Optional.of(reviewMock));

        service.deleteReview(requestHeaderUserId, pathVariableReviewId);

        Mockito.verify(userRepository, Mockito.times(1)).getReferenceById(requestHeaderUserId);
        Mockito.verify(reviewRepository).delete(reviewMock);
    }

    /**
     * Tests the case where an admin successfully deletes another user's review.
     */
    @Test
    void givenUserIsAdmin_whenDeleteReview_thenDeleteReview() {
        final Long authorId = 200L;
        User admin = User.builder().id(requestHeaderAdminUserId).role(UserRole.ADMIN).build();
        User author = User.builder().id(authorId).role(UserRole.USER).build();
        Review reviewMock = Review.builder().id(pathVariableReviewId).user(author).build();

        Mockito.when(userRepository.getReferenceById(requestHeaderAdminUserId)).thenReturn(admin);
        Mockito.when(reviewRepository.findById(pathVariableReviewId)).thenReturn(Optional.of(reviewMock));

        service.deleteReview(requestHeaderAdminUserId, pathVariableReviewId);

        Mockito.verify(userRepository, Mockito.times(1)).getReferenceById(requestHeaderAdminUserId);
        Mockito.verify(reviewRepository).delete(reviewMock);
    }

    /**
     * Tests the case where a user who is not an admin attempts to delete
     * a review created by another user.
     */
    @Test
    void givenUserIsNotAuthorAndNotAdmin_whenDeleteReview_thenThrowUserNotAuthorizedException() {
        final Long otherUserId = 200L;
        User requester = User.builder().id(requestHeaderUserId).role(UserRole.USER).build();
        User otherUser = User.builder().id(otherUserId).role(UserRole.USER).build();
        Review reviewMock = Review.builder().id(pathVariableReviewId).user(otherUser).build();

        Mockito.when(userRepository.getReferenceById(requestHeaderUserId)).thenReturn(requester);
        Mockito.when(reviewRepository.findById(pathVariableReviewId)).thenReturn(Optional.of(reviewMock));

        Assertions.assertThrows(UserNotAuthorizedException.class, () ->
                service.deleteReview(requestHeaderUserId, pathVariableReviewId));

        Mockito.verify(userRepository, Mockito.times(1)).getReferenceById(requestHeaderUserId);
        Mockito.verify(reviewRepository, Mockito.never()).delete(Mockito.any());
    }

    /**
     * Tests the case where the reviewId does not exist when attempting to delete.
     */
    @Test
    void givenInvalidReviewId_whenDeleteReview_thenThrowReviewNotFoundException() {

        Mockito.when(reviewRepository.findById(pathVariableReviewId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ReviewNotFoundException.class, () ->
                service.deleteReview(requestHeaderUserId, pathVariableReviewId));

        Mockito.verify(reviewRepository, Mockito.never()).delete(Mockito.any());
    }
}
