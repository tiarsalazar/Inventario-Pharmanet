package com.pharmanet.venta_service.request;

import java.util.List;

import com.pharmanet.venta_service.entity.DetalleVenta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventarioRequest {

    @NotBlank(message = "Este campo no puede ser nulo")
    private String sku;
    
    @NotBlank(message = "Este campo no puede ser nulo")
    private String codSucursal;
    
    @NotBlank(message = "Este campo no puede ser nulo")
    private String run;

    @NotNull(message = "Este campo no ser nulo")
    private List<DetalleVenta> productos;
}
