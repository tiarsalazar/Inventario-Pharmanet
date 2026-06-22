package com.pharmanet.usuario_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "Sucursal DTO", description = "Objeto sucursal con la información accesible al usuario")
public class SucursalDTO {

    @NotBlank(message = "Campo obligatorio")
    @Size(min = 4, max = 10, message = "El campo debe tener entre 4 y 8 caracteres")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Solo mayúsculas y números")
    @Schema(description = "Código de la sucursal", example = "SU0001")
    private String codSucursal;

    @Size(min = 5, max = 30, message = "El campo debe tener entre 5 y 30 caracteres")
    @Schema(description = "Nombre de la sucursal", example = "FARMACIA SAN MIGUEL")
    private String nombreSucursal;

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 2, message = "Largo máximo 2 caracteres")
    @Schema(description = "Código de la región", example = "5")
    private String codRegion;

    @NotNull(message = "Campo obligatorio")
    @Schema(description = "Código de la sucursal", example = "12")
    private Integer codComuna;

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 100, message = "Largo máximo 100 carácteres")
    @Schema(description = "Dirección de la sucursal", example = "Avenida Libertad 633")
    private String direccion;
}