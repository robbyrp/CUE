package com.cue.demo.controllers;

import com.cue.demo.dtos.ReviewDTO;
import com.cue.demo.services.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@CrossOrigin(origins = "http://localhost:5174")
public class ReviewController {
    private final ReviewService service;
    public ReviewController(ReviewService service) { this.service = service; }

    @PostMapping("/{performanceId}")
    public ResponseEntity<ReviewDTO> createReview(@PathVariable Long performanceId,
                                               @RequestHeader(value = "X-User-Id") Long userId,
                                               @RequestBody @Valid ReviewDTO reviewDTO) {

        ReviewDTO created = service.createReview(userId, performanceId, reviewDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{performanceId}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long performanceId,
                                                  @RequestHeader(value = "X-User-Id") Long userId,
                                                  @RequestBody @Valid ReviewDTO reviewDTO) {

        ReviewDTO updated = service.updateReview(userId, performanceId, reviewDTO);
        return ResponseEntity.ok().body(updated);
    }
}

