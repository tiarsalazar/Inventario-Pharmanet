package com.pharmanet.venta_service.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VentaDto {

    @NotNull(message = "Este campo no puede estar vacío.")
    private Long id;

    @NotBlank(message = "Este campo no puede estar vacío.")
    private String sku;

    @NotBlank(message = "Este campo no puede estar vacío")
    private String codSucursal;

    @NotBlank(message = "Este campo no puede estar vacío")
    @Pattern(regexp = "^[0-9]{7,8}-[0-9kK]$", message = "RUN inválido")
    private String runEmpleado;

    @NotNull(message = "Este campo no puede estar vacío.")
    @Min(value = 1, message = "Ingrese al menos un producto.")
    private int cantidad;

    @NotNull(message = "Este campo no puede estar vacío.")
    private LocalDate fechaVenta;
}
