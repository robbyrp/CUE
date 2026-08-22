package com.cue.demo.exceptions;

public class PerformanceNotInUsersWatchListException extends RuntimeException {
    public PerformanceNotInUsersWatchListException(final Long userId, final Long performanceId) {
        super("Performance with id: " + performanceId +
                "not found in user's" + userId + " WATCH LATER LIST");
    }
}
