package com.darglk.socialapp.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestControllerAdvice {
    @ExceptionHandler(value = { CustomException.class })
    public ResponseEntity<?> handleCustomException(CustomException ex) {
        return CustomException.handleCustomException(ex);
    }
}
