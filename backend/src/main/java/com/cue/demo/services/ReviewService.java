package com.cue.demo.services;

import com.cue.demo.dtos.ReviewDTO;
import com.cue.demo.entities.Performance;
import com.cue.demo.entities.Review;
import com.cue.demo.entities.User;
import com.cue.demo.exceptions.*;
import com.cue.demo.mapper.ReviewMapper;
import com.cue.demo.repositories.PerformanceRepository;
import com.cue.demo.repositories.ReviewRepository;
import com.cue.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    public final ReviewRepository reviewRepository;
    private final PerformanceRepository performanceRepository;
    private final UserRepository userRepository;
    private final ReviewMapper mapper;

    /**
     * Creates a new review for a performance.
     *
     * @param userId The ID of a valid user, filtered before reaching the
     *                controller.
     * @param performanceId The ID of the performance.
     * @param reviewDTO The DTO containing the review data.
     * @return The newly created review DTO.
     * @throws ReviewAlreadyExistsException If the user has already left a review
     *                                      for the performance.
     * @throws PerformanceNotFoundByIdException If a performance with the
     *                                           specified ID is not found.
     */
    @Transactional
    public ReviewDTO createReview(final Long userId, final Long performanceId, final ReviewDTO reviewDTO)
            throws ReviewAlreadyExistsException, PerformanceNotFoundByIdException {

        if (hasUserReviewedPerformance(userId, performanceId))
            throw new ReviewAlreadyExistsException(userId, performanceId);

        User userProxy = userRepository.getReferenceById(userId);
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(()-> new PerformanceNotFoundByIdException(performanceId));

        Review review = Review.builder()
                .performance(performance)
                .user(userProxy)
                .stars(reviewDTO.stars())
                .text(reviewDTO.text())
                .isSpoiler(reviewDTO.isSpoiler())
                .build();

        review = reviewRepository.saveAndFlush(review);

        return mapper.fromReviewToReviewDTO(review);
    }

    /**
     * Updates an existing review. Only the text and stars can be modified.
     *
     * @param userId The ID of a valid user, filtered before reaching the
     *                controller.
     * @param reviewId The ID of the review.
     * @param reviewDTO The DTO containing the review data.
     * @return The updated review DTO.
     * @throws ReviewNotFoundException If a review with the specified ID is not
     *                                 found.
     * @throws UserNotAuthorizedException If the user is not authorized to update
     *                                     this review.
     */
    @Transactional
    public ReviewDTO updateReview(final Long userId, final Long reviewId, final ReviewDTO reviewDTO)
            throws ReviewNotFoundException, UserNotAuthorizedException {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
        User userProxy = userRepository.getReferenceById(userId);

        if (!review.isCreatedBy(userId) && !userProxy. isAdmin()) {
            throw new UserNotAuthorizedException(userId);
        }

        review.setStars(reviewDTO.stars());
        review.setText(reviewDTO.text());

        review = reviewRepository.save(review);
        return mapper.fromReviewToReviewDTO(review);
    }

    /**
     * Gets the reviews of a certain performance.
     *
     * @param performanceId The ID of the performance.
     * @param pageable The pagination and sorting information.
     * @return A page of review DTOs.
     * @throws PerformanceNotFoundByIdException If a performance with the
     *                                           specified ID is not found.
     */
    public Page<ReviewDTO> getPerformanceReviews(final Long performanceId, final Pageable pageable)
            throws PerformanceNotFoundByIdException {

        if (!performanceRepository.existsById(performanceId)) {
            throw new PerformanceNotFoundByIdException(performanceId);
        }

        Page<Review> reviewsPage = reviewRepository.findByPerformance_Id(performanceId, pageable);
        return reviewsPage.map(mapper::fromReviewToReviewDTO);
    }

    /**
     * Gets the user's review for a specific performance.
     *
     * @param userId The ID of a valid user, filtered before reaching the
     *                controller.
     * @param performanceId The ID of the performance.
     * @return An Optional containing the review DTO, or empty if not found.
     */
    public Optional<ReviewDTO> getMyReview(final Long userId, final Long performanceId) {
        Optional<Review> review = reviewRepository.findByUser_IdAndPerformance_Id(userId, performanceId);
        return review.map(mapper::fromReviewToReviewDTO);
    }

    /**
     * Toggles the heart (like) on a review for a user.
     *
     * @param userId The ID of a valid user, filtered before reaching the
     *                controller.
     * @param reviewId The ID of the review.
     * @throws ReviewNotFoundException If a review with the specified ID is not
     *                                 found.
     */
    @Transactional
    public void toggleHeartReview(final Long userId, final Long reviewId)
            throws ReviewNotFoundException {

        User userProxy = userRepository.getReferenceById(userId);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));

        if (review.getHeartedByUsers().contains(userProxy)) {
            review.getHeartedByUsers().remove(userProxy);
        } else {
            review.getHeartedByUsers().add(userProxy);
        }

        reviewRepository.save(review);
    }

    /**
     * Deletes a review.
     *
     * @param userId The ID of a valid user, filtered before reaching the
     *                controller.
     * @param reviewId The ID of the review.
     * @throws ReviewNotFoundException If a review with the specified ID is not
     *                                 found.
     * @throws UserNotAuthorizedException If the user is not authorized to delete
     *                                     this review.
     */
    @Transactional
    public void deleteReview(final Long userId, final Long reviewId)
            throws ReviewNotFoundException, UserNotAuthorizedException {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));

        User userProxy = userRepository.getReferenceById(userId);

        if (review.isCreatedBy(userId) || userProxy.isAdmin()) {
            reviewRepository.delete(review);
        } else {
            throw new UserNotAuthorizedException(userId);
        }
    }

    /**
     * Internal helper method to check if a user has already reviewed a
     * performance.
     *
     * @param userId The ID of a valid user, filtered before reaching the
     *                controller.
     * @param performanceId The ID of the performance.
     * @return True if the user has reviewed the performance, false otherwise.
     */
    private boolean hasUserReviewedPerformance(final Long userId, final Long performanceId) {
        return reviewRepository.existsByUser_IdAndPerformance_Id(userId, performanceId);
    }
}
