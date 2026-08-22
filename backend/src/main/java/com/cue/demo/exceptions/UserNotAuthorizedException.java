package com.cue.demo.exceptions;

public class UserNotAuthorizedException extends RuntimeException {
    public UserNotAuthorizedException(final Long userId) {
      super("User " + userId + " is not authorized to this request");
    }
}
