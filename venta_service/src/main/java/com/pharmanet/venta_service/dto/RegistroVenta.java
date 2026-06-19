package com.pharmanet.venta_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroVenta {

    @NotNull(message = "Este campo no puede estar vacío.")
    private Long codVenta;

    @NotBlank(message = "Este campo no puede estar vacío")
    private String codSucursal;

    @NotBlank(message = "Este campo no puede estar vacío")
    @Pattern(regexp = "^[0-9]{7,8}-[0-9kK]$", message = "RUN inválido")
    private String run;

    @NotEmpty(message = "Este campo no puede estar vacío")
    private Map<String, Integer> productos;

    private LocalDate fechaVenta;

    private BigDecimal montoTotal;

    public RegistroVenta(Long codVenta, String codSucursal, String run, Map<String, Integer> productos) {
        this.codVenta = codVenta;
        this.codSucursal = codSucursal;
        this.run = run;
        this.productos = productos;
        this.fechaVenta = LocalDate.now();
    }

    public RegistroVenta(Long codVenta, String codSucursal, String run, Map<String, Integer> productos, BigDecimal montoTotal) {
        this.codVenta = codVenta;
        this.codSucursal = codSucursal;
        this.run = run;
        this.productos = productos;
        this.fechaVenta = LocalDate.now();
    }
}
