package com.cue.demo.repositories;

import com.cue.demo.entities.WatchLaterPerformanceItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchLaterPerformanceItemRepository extends JpaRepository<WatchLaterPerformanceItem, Long> {
    /**
     * Method that checks if a performance is already in a user's watch later list
     * @param userId
     * @param performanceId
     * @return
     */
    boolean existsByUserIdAndPerformanceId(Long userId, Long performanceId);

    /**
     * Directly deletes performance from a user's watch later list.
     * @param userId
     * @param performanceId
     */
    void deleteByUserIdAndPerformanceId(Long userId, Long performanceId);
}
