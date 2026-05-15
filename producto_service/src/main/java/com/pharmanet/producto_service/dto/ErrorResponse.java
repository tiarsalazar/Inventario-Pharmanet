package com.pharmanet.producto_service.dto;

import java.util.List;

import com.pharmanet.producto_service.exception.ValidationError;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private int status;
    private String error;
    private String mensaje;
    private List<ValidationError> errors;

    public ErrorResponse(int status, String error, String mensaje) {
        this.status = status;
        this.error = error;
        this.mensaje = mensaje;
    }
}
