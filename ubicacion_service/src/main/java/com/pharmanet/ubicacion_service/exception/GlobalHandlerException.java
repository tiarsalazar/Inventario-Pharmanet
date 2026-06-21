package com.pharmanet.ubicacion_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestControllerAdvice
@Tag(name = "Global Exception Handler", description = "Operaciones relacionadas a las excepciones")
public class GlobalHandlerException {

    @ExceptionHandler(ResourceNotFoundException.class)
    @Operation(summary = "Recurso no encontrado", description = "No encuentra el recurso")
    @ApiResponse(responseCode = "404")
    public ResponseEntity<ErrorResponse> handlerResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(404, "Not found", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @Operation(summary = "El recurso existe actualmente", description = "Ya existe un recurso con ese valor")
    @ApiResponse(responseCode = "409")
    public ResponseEntity<ErrorResponse> handlerResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse(409, "Conflict", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @Operation(summary = "El argumento no es válido", description = "El argumento ingresado no es válido de acuerdo a los parámetros valid")
    @ApiResponse(responseCode = "400")
    public ResponseEntity<ErrorResponse> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> err.getField() + ": " + err.getDefaultMessage())
            .findFirst()
            .orElse("Error de validación");
        
        ErrorResponse error = new ErrorResponse(400, "Bad request", mensaje);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @Operation(summary = "Illegal Argument Exception", description = "El argumento entregado es inválido")
    @ApiResponse(responseCode = "400")
    public ResponseEntity<ErrorResponse> handlerIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(400, "Bad request", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    @Operation(summary = "Exception", description = "Error del servidor")
    @ApiResponse(responseCode = "500")
    public ResponseEntity<ErrorResponse> handlerException(Exception ex) {
        ErrorResponse error = new ErrorResponse(500, "Internal server error", "Ha ocurrido un problema del servidor. Si el problema continúa, contáctase con el administrador");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
