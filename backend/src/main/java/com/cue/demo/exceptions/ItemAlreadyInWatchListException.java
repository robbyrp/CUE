package com.cue.demo.exceptions;

public class ItemAlreadyInWatchListException extends RuntimeException {
    public ItemAlreadyInWatchListException(String message) {
        super(message);
    }
}
