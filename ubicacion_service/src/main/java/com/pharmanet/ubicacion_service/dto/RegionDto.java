package com.pharmanet.ubicacion_service.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionDto {

    @NotBlank(message = "Este campo no puede venir nulo")
    @Size(max = 2, message = "Se permite de uno a dos caracteres")
    private String codRegion;

    @NotBlank(message = "Este campo no puede venir nulo")
    @Size(min = 4, max = 60, message = "Se permite de 4 a 60 caracteres.")
    private String descripcion;
    
    private List<ComunaDto> comunas;
}
