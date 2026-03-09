package com.whosup.dto.response;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        int status,
        String error,
        String message,
        Instant timestamp,
        String path,
        List<FieldError> fieldErrors
) {
    public record FieldError(String field, String message) {}

    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(status, error, message, Instant.now(), path, List.of());
    }

    public static ErrorResponse withFieldErrors(int status, String error, String message,
                                                 String path, List<FieldError> fieldErrors) {
        return new ErrorResponse(status, error, message, Instant.now(), path, fieldErrors);
    }
}
