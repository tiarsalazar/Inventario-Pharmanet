package com.pharmanet.ubicacion_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Región DTO", description = "Objeto región con la información accesible al usuario")
public class RegionDto {

    @NotBlank(message = "Este campo no puede venir nulo")
    @Size(max = 2, message = "Se permite de uno a dos caracteres")
    @Schema(description = "Código de la región", example = "RM")
    private String codRegion;

    @NotBlank(message = "Este campo no puede venir nulo")
    @Size(min = 4, max = 60, message = "Se permite de 4 a 60 caracteres.")
    @Schema(description = "Nombre de la región", example = "Región Metropolitana de Santiago")
    private String descripcion;
}
