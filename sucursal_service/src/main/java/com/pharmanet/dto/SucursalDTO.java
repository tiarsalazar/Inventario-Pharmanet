package com.pharmanet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SucursalDTO {

    @NotBlank(message = "El código interno no puede estar vacío")
    @Size(max = 10, message = "Largo máximo de código interno 10 caracteres")
    private String codInterno;

    @Size(max = 30, message = "Largo máximo de nombre de la sucursal 30 caracteres")
    private String nombreSucursal;

    @NotBlank(message = "El tipo de sucursal no puede estar vacío")
    @Pattern(
        regexp = "(?i)farmacia|bodega",
        message = "El tipo de sucursal no es válido. Entradas válidas: Farmacia, Bodega")
    private String tipoSucursal;

    @NotBlank(message = "La región no puede estar vacía")
    @Size(max = 20, message = "Largo máximo de la región 20 caracteres")
    private String region;

    @NotBlank(message = "La comuna no puede estar vacía")
    @Size(max = 25, message = "Largo máximo de la comuna 25 caracteres")
    private String comuna;

    @NotBlank(message = "La dirección no puede estar vacía")
    @Size(max = 100, message = "Largo máximo de la dirección 100 carácteres")
    private String direccion;
}
