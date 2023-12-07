package com.darglk.socialapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.stream.Collectors;

public class ValidationException extends CustomException {
    private final List<ErrorResponse> errors;

    public ValidationException(Errors errors) {
        super("validation error", HttpStatus.UNPROCESSABLE_ENTITY.value());
        this.errors = errors.getFieldErrors().stream()
                .map(error -> new ErrorResponse(error.getDefaultMessage(), error.getField()))
                .collect(Collectors.toList());
    }

    public ValidationException(List<ErrorResponse> errors) {
        super("validation error", HttpStatus.UNPROCESSABLE_ENTITY.value());
        this.errors = errors;
    }

    @Override
    public List<ErrorResponse> serializeErrors() {
        return errors;
    }
}