package com.cue.demo.mapper;

import com.cue.demo.dtos.review.ReviewDTO;
import com.cue.demo.entities.Review;
import org.springframework.stereotype.Component;

@Component
public final class ReviewMapper {

    /**
     * Maps the review entity to the frontend exposed DTO.
     * @param review entity.
     * @return the reviewDTO object.
     */
    public ReviewDTO fromReviewToReviewDTO(final Review review) {
        Integer heartsNumber = review.getHeartedByUsers() != null ? review.getHeartedByUsers().size() : 0;
        return ReviewDTO.builder()
                .authorUsername(review.getUser().getUsername())
                .authorProfilePictureUrl(review.getUser().getProfilePictureUrl())
                .id(review.getId())
                .createdAt(review.getCreatedAt())
                .stars(review.getStars())
                .hearts(heartsNumber)
                .text(review.getText())
                .isSpoiler(review.isSpoiler())
                .build();
    }

}
