package com.cue.demo.repositories;

import com.cue.demo.entities.Review;
import com.cue.demo.entities.ReviewHeartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewHeartItemRepository extends JpaRepository<ReviewHeartItem, Long> {
    List<ReviewHeartItem> review(Review review);
    Optional<ReviewHeartItem> findByUser_IdAndReview_Id(final Long userId, final Long reviewId);
}

