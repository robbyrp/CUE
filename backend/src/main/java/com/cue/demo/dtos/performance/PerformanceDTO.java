package com.cue.demo.dtos.performance;

import com.cue.demo.entities.Credit;
import com.cue.demo.entities.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PerformanceDTO(
        @Min(0) Long id,
        @NotBlank(message = "title cannot be empty") String title,
        @NotBlank(message = "director cannot be empty") String director,
        @NotBlank(message = "coverImageURL cannot be empty") String coverImageURL,
        @NotNull(message = "ageLimit cannot be empty") @Min(0) @Max(22) Integer ageLimit,
        @NotNull(message = "duration cannot be empty") @Min(0) @Max(481)Integer duration,
        @NotBlank(message = "location cannot be empty") String location,
        @NotBlank(message = "theaterName cannot be empty") String theaterName,
        @NotNull(message = "startDateTime cannot be empty") LocalDateTime startDateTime,
        @NotBlank(message = "fullCoverImageURL cannot be empty") String fullCoverImageURL,
        @NotBlank(message = "purchaseTicketLink cannot be empty") String purchaseTicketLink,
        @NotBlank(message = "description cannot be empty") String description,
        @NotNull(message = "credits cannot be empty") List<Credit> credits,
        List<Review> reviews
) {}
