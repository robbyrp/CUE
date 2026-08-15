package com.cue.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PerformanceAlreadyExistsException.class)
    public ResponseEntity<String> handlePerformanceAlreadyExistsException(
            final PerformanceAlreadyExistsException e) {
        return  new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PerformanceNotFoundByIdException.class)
    public ResponseEntity<String> handlePerformanceNotFoundByIdException(final PerformanceNotFoundByIdException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PerformanceNotInUsersWatchedListException.class)
    public ResponseEntity<String> handlePerformanceNotInUsersWatchedListException(final PerformanceNotFoundByIdException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PerformanceNotInUsersWatchListException.class)
    public ResponseEntity<String> handlePerformanceNotInUsersWatchListException(final PerformanceNotFoundByIdException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundByIdException.class)
    public ResponseEntity<String> handleUserNotFoundByIdException(final PerformanceNotFoundByIdException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<String> handleMissingHeader(MissingRequestHeaderException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(PerformanceIdMismatchException.class)
    public ResponseEntity<String> handlePerformanceIdMismatchException(final PerformanceIdMismatchException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
