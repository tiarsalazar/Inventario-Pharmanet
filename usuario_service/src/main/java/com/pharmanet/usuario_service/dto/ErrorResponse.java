package com.pharmanet.usuario_service.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorResponse {

    private LocalDateTime fecha = LocalDateTime.now();
    private int estado;
    private String error;
    private String mensaje;

    public ErrorResponse(int estado, String error, String mensaje) {
        this.estado = estado;
        this.error = error;
        this.mensaje = mensaje;
    }
}