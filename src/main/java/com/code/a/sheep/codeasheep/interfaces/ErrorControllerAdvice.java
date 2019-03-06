package com.code.a.sheep.codeasheep.interfaces;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * Handles exceptions in REST controllers.
 */
@ControllerAdvice
@Slf4j
class ErrorControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleAdxHttpException(RuntimeException exception) {
        LOGGER.error("Ooops", exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }
}

