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
public class SucursalDto {

    @NotNull(message = "Campo obligatorio")
    private Long id;

    @Size(min = 5, max = 30, message = "El campo debe tener entre 5 y 30 caracteres")
    private String nombreSucursal;

    @NotBlank(message = "Campo obligatorio")
    @Pattern(regexp = "(?i)^(farmacia|bodega)$",
        message = "El tipo de sucursal no es válido. Entradas válidas: farmacia, bodega")
    private String tipoSucursal;

    @NotNull(message = "Campo obligatorio")
    private String region;

    @NotNull(message = "Campo obligatorio")
    private String comuna;

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 100, message = "Largo máximo 100 carácteres")
    private String direccion;
}