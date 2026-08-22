package com.cue.demo.exceptions;

public class PerformanceAlreadyExistsException extends RuntimeException {
    public PerformanceAlreadyExistsException(String title, String director) {
        super("Performance already exists in the database:" +
                " criteria is title: " + title + " AND director: " + director);
    }
}
