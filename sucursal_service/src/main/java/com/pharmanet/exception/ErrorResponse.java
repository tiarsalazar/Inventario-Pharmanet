package com.pharmanet.exception;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorResponse {

    private LocalDate fecha = LocalDate.now();
    private int estado;
    private String error;
    private String mensaje;

    public ErrorResponse(int estado, String error, String mensaje) {
        this.estado = estado;
        this.error = error;
        this.mensaje = mensaje;
    }
}
