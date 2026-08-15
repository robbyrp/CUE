package com.cue.demo.dtos;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReviewDTO(
    @Min(0) Long id,
    @NotNull LocalDateTime createdAt,
    @Min(0) @Max(5) Integer stars,
    @Min(0) Integer hearts,
    @NotBlank String text,
    @NotBlank Boolean isSpoiler
) {
}
