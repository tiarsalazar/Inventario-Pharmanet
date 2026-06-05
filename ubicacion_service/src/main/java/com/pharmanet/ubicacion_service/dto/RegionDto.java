package com.pharmanet.ubicacion_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(descripcion = "Representa una región")

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionDto {

    @Schema(descripcion = "Código de la región", example = "5")
    @NotBlank(message = "Este campo no puede venir nulo")
    @Size(max = 2, message = "Se permite de uno a dos caracteres")
    private String codRegion;

    @Schema(descripcion = "Nombre de la región", example = "Región de Valparaíso")
    @NotBlank(message = "Este campo no puede venir nulo")
    @Size(min = 4, max = 60, message = "Se permite de 4 a 60 caracteres.")
    private String descripcion;
}
