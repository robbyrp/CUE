package com.cue.demo.services;

import com.cue.demo.dtos.ReviewDTO;
import com.cue.demo.entities.Performance;
import com.cue.demo.entities.Review;
import com.cue.demo.entities.User;
import com.cue.demo.exceptions.PerformanceNotFoundByIdException;
import com.cue.demo.exceptions.UserNotFoundByIdException;
import com.cue.demo.repositories.PerformanceRepository;
import com.cue.demo.repositories.ReviewRepository;
import com.cue.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    public final ReviewRepository reviewRepository;
    private final PerformanceRepository performanceRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createReview(final Long userId, final Long id, final ReviewDTO reviewDTO) {
        if (hasUserReviewedPerformance(userId, id)) {
            return;
        }
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundByIdException(userId));
        Performance performance = performanceRepository.findById(id)
                .orElseThrow(()-> new PerformanceNotFoundByIdException(id));

        Review review = Review.builder()
                .performance(performance)
                .user(user)
                .createdAt(reviewDTO.createdAt())
                .stars(reviewDTO.stars())
                .hearts(reviewDTO.hearts())
                .text(reviewDTO.text())
                .isSpoiler(reviewDTO.isSpoiler())
                .build();

        reviewRepository.save(review);
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
