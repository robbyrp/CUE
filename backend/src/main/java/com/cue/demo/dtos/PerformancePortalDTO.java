package com.cue.demo.dtos;

import com.cue.demo.entities.Credit;
import com.cue.demo.entities.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;
import java.util.List;

public record PerformancePortalDTO(
        @NotBlank String coverImageURL,
        @NotBlank String title,
        @NotBlank String director,
        @NotBlank String theaterName,
        @NotNull ZonedDateTime startDateTime,
        @NotNull @Min(0) @Max(22) Integer ageLimit,
        @NotNull @Min(0) @Max(481)Integer duration,
        @NotBlank String fullCoverImageURL,
        @NotBlank String purchaseTicketLink,
        @NotBlank String description,
        @NotNull List<Credit> credits,
        @NotNull List<Review> reviews
) {}
