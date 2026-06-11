package com.pharmanet.inventario_service.exception;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ErrorResponse(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Map<String, String> errors
) {
    public ErrorResponse(int status, String error, String message, String path){
        this(LocalDateTime.now(), status, error, message, path, null);
    }

    public ErrorResponse(int status, String error, String message, String path, Map<String, String> errors){
        this(LocalDateTime.now(), status, error, message, path, errors);
    }
}
