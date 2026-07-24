package com.cue.demo.dtos;

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
public record PerformanceMobileDTO(
        @Min(0) Long id,
        @NotBlank String title,
        @NotBlank String director,
        @NotBlank String coverImageURL,
        @NotNull @Min(0) @Max(22) Integer ageLimit,
        @NotNull @Min(0) @Max(481)Integer duration,
        @NotBlank String location,
        @NotBlank String theaterName,
        @NotNull LocalDateTime startDateTime,
        @NotBlank String fullCoverImageURL,
        @NotBlank String purchaseTicketLink,
        @NotBlank String description,
        @NotNull List<Credit> credits,
        List<Review> reviews
) {
}
