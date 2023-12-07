package com.darglk.socialapp.exception;

import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@Getter
public abstract class CustomException extends RuntimeException {
    private final int statusCode;

    public CustomException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public abstract List<ErrorResponse> serializeErrors();

    public static ResponseEntity<?> handleCustomException(CustomException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(Map.of("errors", ex.serializeErrors()));
    }
}