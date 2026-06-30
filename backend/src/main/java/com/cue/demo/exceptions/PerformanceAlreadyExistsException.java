package com.cue.demo.exceptions;

public class PerformanceAlreadyExistsException extends RuntimeException {
    public PerformanceAlreadyExistsException(String message) {
        super(message);
    }
}
