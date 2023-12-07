package com.darglk.socialapp.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class BadRequestException extends CustomException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST.value());
    }

    @Override
    public List<ErrorResponse> serializeErrors() {
        return List.of(new ErrorResponse(this.getMessage(), null));
    }
}