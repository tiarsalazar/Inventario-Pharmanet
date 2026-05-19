package com.pharmanet.venta_service.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventarioRequest {

    @NotBlank(message = "Este campo no puede ser nulo")
    private String codSucursal;

    @NotBlank(message = "Este campo no puede ser nulo")
    private String sku;

    @NotBlank(message = "Este campo no ser nulo")
    private int cantidad;
}
