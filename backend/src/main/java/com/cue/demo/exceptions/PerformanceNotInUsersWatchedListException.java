package com.cue.demo.exceptions;

public class PerformanceNotInUsersWatchedListException extends RuntimeException {
    public PerformanceNotInUsersWatchedListException(final Long userId, final Long performanceId) {
        super("Performance with id: " + performanceId +
                "not found in user's" + userId + " WATCHED LIST");
    }
}
