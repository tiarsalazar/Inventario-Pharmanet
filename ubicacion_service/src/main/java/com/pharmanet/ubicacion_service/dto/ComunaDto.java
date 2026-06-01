package com.pharmanet.ubicacion_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComunaDto {

    @NotNull(message = "Este campo no puede venir nulo")
    private Integer codComuna;

    @NotBlank(message = "Este campo no puede venir nulo")
    @Size(min = 4, max = 30, message = "Se permite de 4 a 30 caracteres")
    private String descripcion;

    @NotBlank(message = "Este campo no puede venir nulo")
    private String region;
}
