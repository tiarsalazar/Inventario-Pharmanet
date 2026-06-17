package com.pharmanet.inventario_service.dto.venta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Estructura de entrada de producto para Rebaje de stock a Inventario")
public class DetalleVentaRequest {
    @Schema(description = "Codigo interno unico del producto.", example = "PR00001")
    @NotBlank(message = "Código de producto obligatorio")
    @Size(max = 30, message = "Sku no debe superar 30 caracteres")
    private String sku;
    @Schema(description = "Cantidad a rebajar del producto.", example = "15")
    @NotNull(message = "Cantidad es obligatoria")
    @Min(value = 1, message = "Cantidad debe ser mayor a 0 ")
    private Integer cantidad;
}
