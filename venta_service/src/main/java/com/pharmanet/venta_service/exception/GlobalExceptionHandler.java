package com.pharmanet.venta_service.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pharmanet.venta_service.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(404, "Not Found", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(VentaNotUniqueException.class)
    public ResponseEntity<ErrorResponse> handlerVentaNotUniqueException(VentaNotUniqueException ex) {
        ErrorResponse error = new ErrorResponse(409, "Conflict", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handlerIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(400, "Bad Request", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ValidationError> errores = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> new ValidationError(
                err.getField(),
                err.getDefaultMessage()))
            .toList();

        ErrorResponse error = new ErrorResponse(500, "Bad Request", "Se encontraron el/los siguiente(s) error(es):", errores);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);    }

    @ExceptionHandler(VentaInvalida.class)
    public ResponseEntity<ErrorResponse> handlerVentaInvalida(VentaInvalida ex) {
        ErrorResponse error = new ErrorResponse(400, "Bad Request", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handlerException(Exception ex) {
        ErrorResponse error = new ErrorResponse(500, "Internar Server Error", "Ha ocurrido un error en el sistema. Si el problema persiste, comuníquese con el administrador.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
