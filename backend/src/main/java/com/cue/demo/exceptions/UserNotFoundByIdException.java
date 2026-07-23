package com.cue.demo.exceptions;

public class UserNotFoundByIdException extends RuntimeException {
    public UserNotFoundByIdException(final String message) {
        super(message);
    }
}
