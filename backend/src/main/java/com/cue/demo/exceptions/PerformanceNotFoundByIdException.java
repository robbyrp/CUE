package com.cue.demo.exceptions;

public class PerformanceNotFoundByIdException extends RuntimeException {
    public PerformanceNotFoundByIdException(final String message) {
        super(message);
    }
}
