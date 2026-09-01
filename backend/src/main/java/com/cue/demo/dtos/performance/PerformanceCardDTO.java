package com.cue.demo.dtos.performance;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PerformanceCardDTO(
    @Min(0) Long id,
    @NotBlank(message = "title cannot be empty") String title,
    @NotBlank(message = "director cannot be empty") String director,
    @NotNull(message = "coverImageUrl cannot be empty") String coverImageUrl,
    @Min(0) @Max(22) Integer ageLimit,
    @NotNull(message = "duration cannot be empty") @Min(0) @Max(481)Integer duration
) {}
