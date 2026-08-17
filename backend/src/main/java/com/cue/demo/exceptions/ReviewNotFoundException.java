package com.cue.demo.exceptions;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(final Long userId, final Long performanceId) {
        super("Review not found by unique pair: userId=" + userId + ", performance id=" + performanceId);
    }
}
