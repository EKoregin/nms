package com.ekoregin.nms.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNotFound(NoSuchElementException exception) {
        System.out.println("Exception No sucj element");
        return ResponseEntity.badRequest().body(
                exception.getMessage());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handlePersistenceExceptions(ResponseStatusException exception) {
        return ResponseEntity.badRequest().body(
                exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentExceptions(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(
                exception.getMessage());
    }
}