package com.inditex.apps.platform.backend.controllers;


import com.inditex.contexts.shared.domain.AppError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandlerExceptionController {
    private static final Logger logger = LoggerFactory.getLogger(HandlerExceptionController.class);
    @ExceptionHandler()
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> internalException(Exception ex) {
        AppError error = new AppError(
                "Internal Error",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        logger.error(ex.getMessage() + ". error: {}", error);
        return ResponseEntity.internalServerError().body(error.toJson());
    }
}
