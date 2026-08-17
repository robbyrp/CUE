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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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
     * Internal helper method, checks if user has already reviewed a performance.
     * @param userId Provided in the request header.
     * @param id PerformanceId in query param.
     * @return True or False
     */
    private boolean hasUserReviewedPerformance(final Long userId, final Long id) {
        return reviewRepository.existsByUser_IdAndPerformance_Id(userId, id);
    }
}
