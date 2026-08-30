package com.cue.demo.exceptions;

public class UsernameAlreadyInUseException extends RuntimeException {
    public UsernameAlreadyInUseException(String username) {
        super("Username " + username + " is already in use. For registration, use a new one");
    }
}
