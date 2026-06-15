package com.pharmanet.abastecimiento_service.exception;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estructura estándar para la devolución de errores de la API")
public record ErrorResponse(
    @Schema(description = "Fecha y hora exacta en la que ocurrió el error", example = "2026-06-15T12:30:00")
    LocalDateTime timestamp,
    @Schema(description = "Código de estado HTTP numérico", example = "400")
    int status,
    @Schema(description = "Nombre estándar del error HTTP", example = "Bad Request")
    String error,
    @Schema(description = "Mensaje explicando la causa de la excepción", example = "Error de validación")
    String message,
    @Schema(description = "URI del endpoint que causó el error", example = "/api/v1/recepciones/sucursales/SU0001/registrar")
    String path,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Mapa con errores específicos por campos (solo aplica en fallas de validación)")
    Map<String, String> errors
) {
    public ErrorResponse(int status, String error, String message, String path){
        this(LocalDateTime.now(), status, error, message, path, null);
    }

    public ErrorResponse(int status, String error, String message, String path, Map<String, String> errors){
        this(LocalDateTime.now(), status, error, message, path, errors);
    }
}
