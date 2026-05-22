package com.pharmanet.producto_service.dto;

import java.math.BigDecimal;

import com.pharmanet.producto_service.entity.ClaseReceta;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDto {

    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(max = 30, message = "Máximo 30 caracteres")
    private String sku; // CÓDIGO DE PRODUCTO
    
    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(max = 100, message = "Máximo 100 caracteres")
    private String nombreComercial;
    
    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(max =80, message = "Máximo 80 caracteres")
    private String principioActivo;
    
    @NotNull(message = "Este campo no puede estar vacío")
    @Min(value = 1, message = "El precio no puede ser inferior a $1")
    private BigDecimal precioVenta;

    @NotNull(message = "Este campo no puede estar vacío")
    private ClaseReceta receta;

    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(max = 30, message = "Máximo 30 caracteres")
    private String concentracion;
}
