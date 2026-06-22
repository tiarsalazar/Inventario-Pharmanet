package com.pharmanet.sucursal_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Comuna DTO", description = "Objeto comuna con la información accesible al usuario")
public class ComunaDTO {
    
    @NotNull(message = "Este campo no puede venir nulo")
    @Schema(description = "Código de la comuna", example = "17")
    private Integer codComuna;

    @NotBlank(message = "Este campo no puede venir nulo")
    @Size(min = 4, max = 30, message = "Se permite de 4 a 30 caracteres")
    @Schema(description = "Descripción de la comuna", example = "Talca")
    private String descripcion;

    @NotBlank(message = "Este campo no puede venir nulo")
    @Schema(description = "Código de la región", example = "RM")
    private String region;
}
