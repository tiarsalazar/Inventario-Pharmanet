package com.pharmanet.inventario_service.dto.venta;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Estructura de entrada para Rebaje de stock a Inventario enviado por VENTA")
public class VentaRequest {
    @Schema(description = "Codigo interno unico de una Sucursal", example = "SU0001")
    @NotBlank(message = "Código sucursal obligatorio")
    private String codSucursal;
    @Schema(description = "RUN del usuario que operó la transacción", example = "11222333-4")
    @NotBlank(message = "Run vendedor obligatorio")
    private String run;
    @Schema(description = "Listado con el detalle de productos para el ingreso de stock a Inventarios.")
    @NotNull(message = "Lista de productos es obligatoria")
    @NotEmpty(message = "Debe incluir almenos un producto")
    @Valid
    List<DetalleVentaRequest> productos;
}
