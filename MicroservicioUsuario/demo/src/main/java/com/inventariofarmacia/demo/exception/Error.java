package com.inventariofarmacia.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Error {

    private int codigo;
    private String nombre;
    private String mensaje;
}
