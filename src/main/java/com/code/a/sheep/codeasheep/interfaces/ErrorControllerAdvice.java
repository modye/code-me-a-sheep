package com.code.a.sheep.codeasheep.interfaces;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * Handles exceptions in REST controllers.
 */
@ControllerAdvice
class ErrorControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleAdxHttpException(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }
}

