package com.cue.demo.dtos.performance;

import jakarta.validation.constraints.Min;

public record WatchPerformanceItemDTO(
        @Min(0) Long userId,
        @Min(0) Long performanceId
) {
}
