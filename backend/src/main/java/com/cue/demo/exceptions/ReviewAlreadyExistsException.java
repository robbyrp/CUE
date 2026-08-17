package com.cue.demo.exceptions;

public class ReviewAlreadyExistsException extends RuntimeException {
    public ReviewAlreadyExistsException(final Long userId, final Long id) {
        super("User with id " + userId + " already left a review on performance with id " + id);
    }
}
