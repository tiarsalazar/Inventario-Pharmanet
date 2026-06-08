package com.pharmanet.sucursal_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SucursalDTO {

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 8, message = "Largo máximo 8 caracteres")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Solo mayúsculas y números")
    private String codSucursal;

    @Size(min = 5, max = 30, message = "El campo debe tener entre 5 y 30 caracteres")
    private String nombreSucursal;

    @NotBlank(message = "Campo obligatorio")
    private String codRegion;

    @NotNull(message = "Campo obligatorio")
    private Integer codComuna;

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 100, message = "Largo máximo 100 carácteres")
    private String direccion;
}