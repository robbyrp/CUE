package com.cue.demo.controllers;

import com.cue.demo.dtos.review.ReviewDTO;
import com.cue.demo.security.UserSecurityAdapter;
import com.cue.demo.services.ReviewService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService service;
    public ReviewController(ReviewService service) { this.service = service; }

    @PostMapping("/spectacole/{performanceId}")
    public ResponseEntity<ReviewDTO> createReview(@PathVariable Long performanceId,
                                               @AuthenticationPrincipal UserSecurityAdapter principal,
                                               @RequestBody @Valid ReviewDTO reviewDTO) {

        final Long userId = principal.getId();
        ReviewDTO created = service.createReview(userId, performanceId, reviewDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long reviewId,
                                                  @AuthenticationPrincipal UserSecurityAdapter principal,
                                                  @RequestBody @Valid ReviewDTO reviewDTO) {

        final  Long userId = principal.getId();
        ReviewDTO updated = service.updateReview(userId, reviewId, reviewDTO);
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
                                                 @AuthenticationPrincipal UserSecurityAdapter principal) {

        final Long  userId = principal.getId();
        Optional<ReviewDTO> review = service.getMyReview(userId, performanceId);
        return review.map(reviewDTO -> ResponseEntity.ok().body(reviewDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).body(null));
    }

    @PutMapping("/{reviewId}/heart")
    public ResponseEntity<Void> toggleHeartReview (@PathVariable Long reviewId,
                                                   @AuthenticationPrincipal UserSecurityAdapter principal) {

         final Long  userId = principal.getId();
         service.toggleHeartReview(userId, reviewId);
         return ResponseEntity.ok().build();
    }

    @GetMapping("/me/all")
    public ResponseEntity<Page<ReviewDTO>> getMyReviews(@AuthenticationPrincipal UserSecurityAdapter principal,
                                                        final Pageable pageable) {

        final Long userId = principal.getId();
        Page<ReviewDTO> reviewDTOPage = service.getMyReviews(userId, pageable);
        return ResponseEntity.ok().body(reviewDTOPage);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> deleteReview(@PathVariable Long reviewId,
                                                  @AuthenticationPrincipal UserSecurityAdapter principal) {

        final Long userId = principal.getId();
        service.deleteReview(userId, reviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
