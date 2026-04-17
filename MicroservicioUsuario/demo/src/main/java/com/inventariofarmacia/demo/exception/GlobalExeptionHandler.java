package com.inventariofarmacia.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExeptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Error> UserNotFound(UserNotFoundException ex){
        Error error = new Error(404, "NotFound", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

}
