package com.pharmanet.inventario_service.dto.recepcion;

import java.util.List;

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
@Schema(description = "Estructura de entrada para Ingreso de stock a Inventario enviado por RECEPCION")
public class RecepcionRequest {
    @Schema(description = "Codigo interno unico de una Sucursal", example = "SU0001")
    @NotBlank(message = "Código sucursal es obligatorio")
    private String codSucursal;
    @Schema(description = "Listado con el detalle para el ingreso de stock a Inventarios.")
    @NotNull(message = "Lista de detalles es obligatoria")
    @NotEmpty(message = "Debe incluir almenos un detalle")
    @Valid
    private List<DetalleRecepcionRequest> detalles;
}
