package com.cue.demo.mapper;

import com.cue.demo.dtos.ReviewDTO;
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
        return ReviewDTO.builder()
                .id(review.getId())
                .createdAt(review.getCreatedAt())
                .stars(review.getStars())
                .hearts(review.getHearts())
                .text(review.getText())
                .isSpoiler(review.isSpoiler())
                .build();
    }

}
