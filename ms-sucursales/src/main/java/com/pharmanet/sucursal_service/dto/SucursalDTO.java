package com.pharmanet.sucursal_service.dto;


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
    @Size(min = 6, max = 10, message = "El campo debe tener entre 6 y 10 caracteres")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Solo mayúsculas y números")
    private String codInterno;

    @Size(min = 5, max = 30, message = "El campo debe tener entre 5 y 30 caracteres")
    private String nombreSucursal;

    @NotBlank(message = "El tipo de sucursal es obligatorio")
    @Pattern(regexp = "(?i)^(farmacia|bodega)$",
        message = "El tipo de sucursal no es válido. Entradas válidas: farmacia, bodega")
    private String tipoSucursal;

    @NotBlank(message = "La región no puede estar vacía")
    @Size(max = 30, message = "Largo máximo 30 caracteres")
    private String region;

    @NotBlank(message = "La comuna no puede estar vacía")
    @Size(max = 40, message = "Largo máximo 40 caracteres")
    private String comuna;

    @NotBlank(message = "La dirección no puede estar vacía")
    @Size(max = 100, message = "Largo máximo 100 carácteres")
    private String direccion;
}
