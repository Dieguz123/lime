package com.diego.lime.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleConflict(IllegalStateException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleDataIntegrity(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Data conflict, try again");
    }
}
