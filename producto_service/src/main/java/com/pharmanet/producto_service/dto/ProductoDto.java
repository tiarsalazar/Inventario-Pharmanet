package com.pharmanet.producto_service.dto;

import java.math.BigDecimal;

import com.pharmanet.producto_service.entity.ClaseReceta;

import io.swagger.v3.oas.annotations.media.Schema;
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

@Schema(name = "Producto DTO", description = "Objeto producto con la información accesible al usuario")
public class ProductoDto {

    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(min = 3, max = 10, message = "Máximo 10 caracteres")
    @Schema(description = "Sku del producto", example = "PR0001")
    private String sku; // CÓDIGO DE PRODUCTO
    
    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(max = 100, message = "Máximo 100 caracteres")
    @Schema(description = "Nombre comercial", example = "Tapsin")
    private String nombreComercial;
    
    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(max =80, message = "Máximo 80 caracteres")
    @Schema(description = "Principio activo", example = "Ibuprofeno")
    private String principioActivo;
    
    @NotNull(message = "Este campo no puede estar vacío")
    @Min(value = 1, message = "El precio no puede ser inferior a $1")
    @Schema(description = "Precio de venta", example = "12000")
    private BigDecimal precioVenta;

    @NotNull(message = "Este campo no puede estar vacío")
    @Schema(description = "Tipo de receta", example = "RECETA_PRESENTADA")
    private ClaseReceta receta;

    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(max = 30, message = "Máximo 30 caracteres")
    @Schema(description = "Concentración y unidad de medida del producto", example = "100MG")
    private String concentracion;
}
