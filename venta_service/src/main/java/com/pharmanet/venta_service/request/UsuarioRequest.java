package com.pharmanet.venta_service.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequest {

    private String codSucursal;
    private String runEmpleado;
    private String receta;
}
