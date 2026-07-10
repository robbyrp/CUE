package com.cue.demo.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PerformanceCardDTO(
    @Min(0) Long id,
    @NotBlank String title,
    @NotBlank String director,
    @NotNull String coverImageUrl,
    @Min(0) @Max(22) Integer ageLimit,
    @NotNull @Min(0) @Max(481)Integer duration
) {}
