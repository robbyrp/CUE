package com.cue.demo.dtos.review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReviewDTO(
    @Min(0) Long id,
    LocalDateTime createdAt,
    @Min(0) @Max(5) Integer stars,
    @Min(0) Integer hearts,
    @NotBlank(message = "text cannot be empty") String text,
    @NotNull(message = "isSpoiler cannot be empty") Boolean isSpoiler
) {
}
