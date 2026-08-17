package com.cue.demo.repositories;

import com.cue.demo.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByUser_IdAndPerformance_Id(final Long userId, final Long performanceId);
    Optional<Review> findByUser_IdAndPerformance_Id(final Long userId, final Long performanceId);
}
