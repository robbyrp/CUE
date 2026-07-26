package com.cue.demo.repositories;

import com.cue.demo.entities.WatchLaterPerformanceItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchLaterPerformanceItemRepository extends JpaRepository<WatchLaterPerformanceItem, Long> {
    /**
     * Method that checks if a performance is already in a user's watch later list
     * @param userId primary key of user entity
     * @param performanceId primary key of performance entity
     * @return True if the performance is in the user's watch later list.
     */
    boolean existsByUserIdAndPerformanceId(Long userId, Long performanceId);

    /**
     * Directly deletes performance from a user's watch later list.
     * @param userId primary key of user entity
     * @param performanceId primary key of performance entity
     */
    void deleteByUserIdAndPerformanceId(Long userId, Long performanceId);

    /**
     * Returns a page from the user's watch later set.
     * @param userId primary key of user entity
     * @param pageable pageable object
     * @return A Page of watch later performance item objects.
     */
    Page<WatchLaterPerformanceItem> findByUserId(Long userId, Pageable pageable);
}
