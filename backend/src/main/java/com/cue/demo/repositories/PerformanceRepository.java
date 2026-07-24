package com.cue.demo.repositories;

import com.cue.demo.entities.Performance;
import com.cue.demo.dtos.SearchSuggestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    boolean existsByTitleAndDirector(String title, String director);
    List<SearchSuggestion> findTop6ByTitleContainingIgnoreCase(String title);

    @Query("SELECT p from Performance p WHERE "+
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))  OR "+
            "LOWER(p.director) LIKE LOWER(CONCAT('%', :keyword, '%'))  OR "+
            "LOWER(p.location) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Performance> searchProducts(@Param("keyword") String keyword, Pageable pageable);

}
