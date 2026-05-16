package com.pharmanet.inventario_service.exception;

import java.time.LocalDate;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex){
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(404, "Not Found", ex.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex){
        log.warn("Error de negocio: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(400, "Bad Request", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex){
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .findFirst().orElse("Error de validación.");
        log.warn("Error de validacion: {}", mensaje);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(400, "Bad Request", mensaje));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex){
        log.warn("Violacion de integridad: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ErrorResponse(409, "Conflict", "Ya existe un registro con esos datos."));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex){
        log.warn("Error de tipo en parametro: {}", ex.getName());
        String mensaje;
        if (ex.getRequiredType() != null && ex.getRequiredType().equals(LocalDate.class)){
            mensaje = "Formato de fecha inválido para '" + ex.getName() + "', use: yyyy-MM-dd";
        } else {
            mensaje = "Valor inválido para el parámetro '" + ex.getName() + "'";
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(400, "Bad Request", mensaje));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Error inesperado: {}", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse(500, "Internal Server Error", "Error interno del servidor"));
    }

}
