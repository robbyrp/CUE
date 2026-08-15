package com.cue.demo.exceptions;

public class PerformanceIdMismatchException extends RuntimeException {
    public PerformanceIdMismatchException(final Long queryParamId, final Long performanceDtoId) {

        super("ID FROM QUERY PARAMETER(URL): " + queryParamId + " IS IN CONFLICT WITH" +
                " ID FROM REQUEST BODY DTO OBJECT: " + performanceDtoId);
    }
}
