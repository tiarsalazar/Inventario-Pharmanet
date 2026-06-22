package com.pharmanet.ubicacion_service.dto;

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
public class ComunaDto {

    @NotNull(message = "Este campo no puede venir nulo")
    @Schema(description = "Código de la comuna", example = "12")
    private Integer codComuna;

    @NotBlank(message = "Este campo no puede venir nulo")
    @Size(min = 4, max = 30, message = "Se permite de 4 a 30 caracteres")
    @Schema(description = "Nombre de la comuna", example = "VIÑA DEL MAR")
    private String descripcion;

    @NotBlank(message = "Este campo no puede venir nulo")
    @Schema(description = "Código de la región", example = "5")
    private String region;
}
