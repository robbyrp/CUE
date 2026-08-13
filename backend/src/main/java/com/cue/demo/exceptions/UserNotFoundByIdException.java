package com.cue.demo.exceptions;

public class UserNotFoundByIdException extends RuntimeException {
    public UserNotFoundByIdException(final Long userId) {
        super("User with id: " + userId + " not found");
    }
}
