package com.pharmanet.venta_service.dto;

import java.time.LocalDate;

import jakarta.validation.constraint.Min;
import jakarta.validation.constraint.NotBlank;
import jakarta.validation.constraint.NotNull;
import lombook.AllArgsConstructor;
import lombook.Data;
import lombook.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VentaDto {

    @NotNull(message = "Este campo no puede estar vacío.")
    private Long id;

    @NotBlank(message = "Este campo no puede estar vacío.")
    private String codProd; // PUEDE EDITARSE EL NOMBRE

    @NotBlank(message = "Este campo no puede estar vacío.")
    private String codInterno;

    @NotNull(message = "Este campo no puede estar vacío.")
    @Min(min = 1, message = "Ingrese al menos un producto.")
    private int cantidad;

    @NotNull(message = "Este campo no puede estar vacío.")
    private LocalDate fechaVenta;
}
