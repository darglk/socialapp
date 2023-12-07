package com.darglk.socialapp.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class NotFoundException extends CustomException {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }

    @Override
    public List<ErrorResponse> serializeErrors() {
        return List.of(new ErrorResponse(getMessage(), null));
    }
}