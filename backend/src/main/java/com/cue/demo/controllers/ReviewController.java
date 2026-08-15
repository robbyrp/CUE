package com.cue.demo.controllers;

import com.cue.demo.dtos.ReviewDTO;
import com.cue.demo.services.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/spectacole")
@CrossOrigin(origins = "http://localhost:5174")
public class ReviewController {
    private final ReviewService service;
    public ReviewController(ReviewService service) { this.service = service; }

    @PostMapping("/{id}/review")
    public ResponseEntity<Void> createReview(@PathVariable Long id,
                                               @RequestHeader(value = "X-User-Id") Long userId,
                                               @RequestBody @Valid ReviewDTO reviewDTO) {
        service.createReview(userId, id, reviewDTO);
        return ResponseEntity.ok().build();
    }

}
