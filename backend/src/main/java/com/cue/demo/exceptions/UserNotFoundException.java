package com.cue.demo.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("User with name " + username + " not found");
    }
    public UserNotFoundException(Long id) {
        super("User with ID: " + id + " not found");
    }
}
