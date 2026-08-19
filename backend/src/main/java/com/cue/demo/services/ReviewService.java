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

import java.util.Objects;
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
     *
     * @param userId Provided in the request header.
     * @param performanceId PerformanceId in query param.
     * @param reviewDTO Review object exposed to the frontend.
     * @return Returns the newly created reviewDTO object.
     * @throws ReviewAlreadyExistsException The user has already left a review for the performance.
     */
    @Transactional
    public ReviewDTO createReview(final Long userId, final Long performanceId, final ReviewDTO reviewDTO)
            throws ReviewAlreadyExistsException, UserNotFoundByIdException, PerformanceNotFoundByIdException {
        if (hasUserReviewedPerformance(userId, performanceId)) throw new ReviewAlreadyExistsException(userId, performanceId);

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundByIdException(userId));
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(()-> new PerformanceNotFoundByIdException(performanceId));

        Review review = Review.builder()
                .performance(performance)
                .user(user)
                .stars(reviewDTO.stars())
                .text(reviewDTO.text())
                .isSpoiler(reviewDTO.isSpoiler())
                .build();

        reviewRepository.saveAndFlush(review);

        return mapper.fromReviewToReviewDTO(review);
    }

    /**
     * IMPORTANT: This method can only update the Text comment or the Stars number!
     * @param userId Provided in the request header. Is compared to the Review.user.id member for auth reasons.
     * @param performanceId PerformanceId in query param.
     * @param reviewDTO The review object which contains the new fields.
     * @return Returns the newly updated reviewDTO object.
     * @throws ReviewAlreadyExistsException The user has already left a review for the performance.
     */
    @Transactional
    public ReviewDTO updateReview(final Long userId, final Long performanceId, final ReviewDTO reviewDTO)
            throws ReviewNotFoundException, UserNotAuthorizedException {
        Review review = reviewRepository.findByUser_IdAndPerformance_Id(userId, performanceId)
                .orElseThrow(() -> new ReviewNotFoundException(userId, performanceId));

        if (!Objects.equals(review.getUser().getId(), userId)) {
            throw new UserNotAuthorizedException(userId);
        }

        review.setStars(reviewDTO.stars());
        review.setText(reviewDTO.text());

        reviewRepository.save(review);
        return mapper.fromReviewToReviewDTO(review);

    }

    /**
     * Gets the reviews of a certain performance identified by the Path variable performanceId.
     * @param performanceId PerformanceId in query param.
     * @param pageable The pagination and sorting information (page number, size, sort criteria).
     * @return Returns a page of ReviewDTO.
     * @throws PerformanceNotFoundByIdException Throws it if the performance with the given ID does not exist in the database.
     */
    public Page<ReviewDTO> getPerformanceReviews(final Long performanceId, final Pageable pageable) throws PerformanceNotFoundByIdException {
        if (!performanceRepository.existsById(performanceId)) {
            throw new PerformanceNotFoundByIdException(performanceId);
        }

        Page<Review> reviewsPage = reviewRepository.findByPerformance_Id(performanceId, pageable);
        return reviewsPage.map(mapper::fromReviewToReviewDTO);
    }

    /**
     * Gets the user's review from a performance.
     * @param performanceId PerformanceId in query param.
     * @param userId The ID of the user received in the HTTP Request header.
     * @return The user's own review as a ReviewDTO object or Optional.empty() if the user did not review the performance.
     */
    public Optional<ReviewDTO> getMyReview(final Long userId, final Long performanceId) {
        Optional<Review> review = reviewRepository.findByUser_IdAndPerformance_Id(userId, performanceId);
        return review.map(mapper::fromReviewToReviewDTO);
    }

    /**
     * Adds the user to the review's list of unique hearts.
     * @param userId Provided in the request header. Is compared to the Review.user.id member for auth reasons.
     * @param reviewId Provided in the query param. The review is identified by its id, not the unique pair <user, performance>
     * @throws UserNotFoundByIdException If a user with the specified ID is not found.
     * @throws ReviewNotFoundException If a review with the specified ID is not found.
     */
    @Transactional
    public void toggleHeartReview(final Long userId, final Long reviewId)
            throws UserNotFoundByIdException, ReviewNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundByIdException(userId));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));

        if (review.getHeartedByUsers().contains(user)) {
            review.getHeartedByUsers().remove(user);
        } else {
            review.getHeartedByUsers().add(user);
        }

        reviewRepository.save(review);
    }

    /**
     * Internal helper method, checks if user has already reviewed a performance.
     * @param userId Provided in the request header.
     * @param performanceId PerformanceId in query param.
     * @return True or False
     */
    private boolean hasUserReviewedPerformance(final Long userId, final Long performanceId) {
        return reviewRepository.existsByUser_IdAndPerformance_Id(userId, performanceId);
    }
}
