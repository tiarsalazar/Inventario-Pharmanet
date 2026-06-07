package com.pharmanet.sucursal_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ComunaDTO {
    
    @NotNull(message = "Este campo no puede venir nulo")
    private Integer codComuna;

    @NotBlank(message = "Este campo no puede venir nulo")
    @Size(min = 4, max = 30, message = "Se permite de 4 a 30 caracteres")
    private String descripcion;

    @NotBlank(message = "Este campo no puede venir nulo")
    private String region;
}
