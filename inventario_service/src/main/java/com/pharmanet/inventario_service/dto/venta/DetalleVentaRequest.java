package com.pharmanet.inventario_service.dto.venta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
public class DetalleVentaRequest {
    @NotBlank(message = "Código de producto obligatorio")
    @Size(max = 30, message = "Sku no debe superar 30 caracteres")
    private String sku;
    @NotNull(message = "Cantidad es obligatoria")
    @Min(value = 1, message = "Cantidad debe ser mayor a 0 ")
    private Integer cantidad;
}
