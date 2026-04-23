package com.pharmanet.usuario_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Error {
    private int error;
    private String nombre;
    private String mensaje;
}
