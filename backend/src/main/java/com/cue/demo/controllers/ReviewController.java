package com.cue.demo.controllers;

import com.cue.demo.dtos.ReviewDTO;
import com.cue.demo.services.ReviewService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:5174")
public class ReviewController {
    private final ReviewService service;
    public ReviewController(ReviewService service) { this.service = service; }

    @PostMapping("/spectacole/{performanceId}")
    public ResponseEntity<ReviewDTO> createReview(@PathVariable Long performanceId,
                                               @RequestHeader(value = "X-User-Id") Long userId,
                                               @RequestBody @Valid ReviewDTO reviewDTO) {

        ReviewDTO created = service.createReview(userId, performanceId, reviewDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/spectacole/{performanceId}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long performanceId,
                                                  @RequestHeader(value = "X-User-Id") Long userId,
                                                  @RequestBody @Valid ReviewDTO reviewDTO) {

        ReviewDTO updated = service.updateReview(userId, performanceId, reviewDTO);
        return ResponseEntity.ok().body(updated);
    }

    @GetMapping("/spectacole/{performanceId}")
    public ResponseEntity<Page<ReviewDTO>> getPerformanceReviews(@PathVariable Long performanceId,
                                                                 Pageable pageable) {

        Page<ReviewDTO> reviews = service.getPerformanceReviews(performanceId, pageable);
        return ResponseEntity.ok().body(reviews);
    }

    @GetMapping("/spectacole/{performanceId}/me")
    public ResponseEntity<ReviewDTO> getMyReview(@PathVariable Long performanceId,
                                                 @RequestHeader(value = "X-User-Id") Long userId) {

        Optional<ReviewDTO> review = service.getMyReview(userId, performanceId);
        return review.map(reviewDTO -> ResponseEntity.ok().body(reviewDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).body(null));
    }

    @PutMapping("/heart/{reviewId}")
    public ResponseEntity<Void> toggleHeartReview (@PathVariable Long reviewId,
                                                   @RequestHeader(value = "X-User-Id") Long userId) {

         service.toggleHeartReview(userId, reviewId);
         return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> deleteReview(@PathVariable Long reviewId,
                                                  @RequestHeader(value = "X-User-Id") Long userId) {

        service.deleteReview(userId, reviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
