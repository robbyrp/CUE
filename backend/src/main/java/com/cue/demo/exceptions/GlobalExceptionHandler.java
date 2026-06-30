package com.cue.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PerformanceAlreadyExistsException.class)
    public ResponseEntity<String> handlePerformanceAlreadyExistsException(
            final PerformanceAlreadyExistsException e) {
        return  new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }
}
