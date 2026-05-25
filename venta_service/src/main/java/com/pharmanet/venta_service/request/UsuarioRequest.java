package com.pharmanet.venta_service.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequest {

    private String runVendedor;
    private String codSucursal;
    private String receta;
}
