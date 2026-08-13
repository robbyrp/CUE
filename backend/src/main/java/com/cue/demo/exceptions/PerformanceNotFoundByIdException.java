package com.cue.demo.exceptions;

public class PerformanceNotFoundByIdException extends RuntimeException {
    public PerformanceNotFoundByIdException(final Long performanceId) {
        super("Performance with id: " + performanceId + " not found");
    }
}
