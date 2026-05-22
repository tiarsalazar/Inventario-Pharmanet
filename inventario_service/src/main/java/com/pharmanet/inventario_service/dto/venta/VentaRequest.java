package com.pharmanet.inventario_service.dto.venta;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class VentaRequest {
    @NotBlank(message = "Código de producto obligatorio")
    @Size(max = 30, message = "Sku no debe superar 30 caracteres")
    private String sku;
    @NotBlank(message = "Código sucursal obligatorio")
    private String codSucursal; 
    @NotBlank(message = "Run vendedor obligatorio")
    private String runVendedor;
    @NotNull(message = "Cantidad es obligatoria")
    @Min(value = 1, message = "Cantidad debe ser mayor a 0 ")
    private Integer cantidad;
}
