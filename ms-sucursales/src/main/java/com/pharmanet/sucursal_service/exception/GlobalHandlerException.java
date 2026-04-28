package com.pharmanet.sucursal_service.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.pharmanet.sucursal_service.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalHandlerException {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(404, "Not Found", ex.getMessage());
        log.warn("Objeto no encontrado: {}", ex.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(NotUniqueSucursalException.class)
    public ResponseEntity<ErrorResponse> handlerNotUniqueSucursalException(NotUniqueSucursalException ex) {
        ErrorResponse error = new ErrorResponse(409, "Conflict", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // Captura duplicados en atributos únicos
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handlerDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ErrorResponse error = new ErrorResponse(400, "Bad Request", "Ya existe un registro con ese código o nombre");
        log.warn("Error de propiedad única: {}", ex.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> err.getField() + ": "  + err.getDefaultMessage())
            .findFirst()
            .orElse("Error de Validación");
        ErrorResponse error = new ErrorResponse(400, "Bad Request", mensaje);
        log.warn("Error de validación: {}", ex.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handlerException(Exception ex) {
        ErrorResponse error = new ErrorResponse(500,
            "Internal Server Error",
            "Ha ocurrido un error del servidor. Si el problema persiste comuníquese con el administrador");
        log.error("Error: {}", ex.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
