package com.cue.demo.repositories;

import com.cue.demo.entities.Performance;
import com.cue.demo.entities.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByUser_IdAndPerformance_Id(final Long userId, final Long performanceId);
    Optional<Review> findByUser_IdAndPerformance_Id(final Long userId, final Long performanceId);
    Page<Review> findByPerformance_Id(final Long performanceId, final Pageable pageable);

    @Query("SELECT r.performance FROM Review r WHERE r.user.id = :userId")
    Page<Performance> findPerformancesReviewedByUserId(Long userId, final Pageable pageable);

    Page<Review> findByUser_Id(final Long userId, final Pageable pageable);
}
