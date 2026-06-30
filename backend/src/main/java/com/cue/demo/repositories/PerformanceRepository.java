package com.cue.demo.repositories;

import com.cue.demo.entities.Performance;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PerformanceRepository extends CrudRepository<Performance,Long> {
    boolean existsByTitleAndDirector(String title, String director);
}
