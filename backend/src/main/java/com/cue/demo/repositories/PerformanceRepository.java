package com.cue.demo.repositories;

import com.cue.demo.entities.Performance;
import com.cue.demo.dtos.search.SearchSuggestion;
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

    @Query(nativeQuery = true,
            value= "SELECT p.id AS id, p.title AS title from spectacol p WHERE " +
            "LOWER(unaccent(p.title)) LIKE LOWER(unaccent(CONCAT('%', :keyword, '%')))  OR " +
            "LOWER(unaccent(p.director)) LIKE LOWER(unaccent(CONCAT('%', :keyword, '%')))" +
                    "LIMIT 6" )
    List<SearchSuggestion> searchTitleCompletionSuggestions(String keyword);

    @Query(nativeQuery = true,
            value="SELECT p from spectacol p WHERE "+
            "LOWER(unaccent(p.title)) LIKE LOWER(CONCAT('%', :keyword, '%'))  OR "+
            "LOWER(unaccent(p.director)) LIKE LOWER(CONCAT('%', :keyword, '%'))  OR "+
            "LOWER(unaccent(p.location)) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Performance> searchPerformances(@Param("keyword") String keyword, Pageable pageable);

}
