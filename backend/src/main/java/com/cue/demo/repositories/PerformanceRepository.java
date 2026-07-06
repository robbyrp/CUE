package com.cue.demo.repositories;

import com.cue.demo.entities.Performance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    boolean existsByTitleAndDirector(String title, String director);
    Page<Performance> findByDeletedFalse(Pageable pageable);

}
