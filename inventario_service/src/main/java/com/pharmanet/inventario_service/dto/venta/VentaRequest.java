package com.pharmanet.inventario_service.dto.venta;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VentaRequest {
    @NotBlank(message = "Código sucursal obligatorio")
    private String codSucursal; 
    @NotBlank(message = "Run vendedor obligatorio")
    private String run;
    @NotNull(message = "Lista de productos es obligatoria")
    @NotEmpty(message = "Debe incluir almenos un producto")
    @Valid
    List<DetalleVentaRequest> productos;
}
