package com.pharmanet.venta_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
@Schema(name = "Registro Venta", description = "Objeto que muestra toda la información de venta disponible para el usuario")
public class RegistroVenta {

    @Schema(description = "Código de la venta")
    @NotNull(message = "Este campo no puede estar vacío.")
    private Long codVenta;

    @Schema(description = "Código de la sucursal")
    @NotBlank(message = "Este campo no puede estar vacío")
    private String codSucursal;

    @Schema(description = "Run del vendedor")
    @NotBlank(message = "Este campo no puede estar vacío")
    @Pattern(regexp = "^[0-9]{7,8}-[0-9kK]$", message = "RUN inválido")
    private String run;

    @Schema(description = "Mapa con el código del producto y la cantidad de productos vendidos")
    @NotEmpty(message = "Este campo no puede estar vacío")
    private Map<String, Integer> productos;

    @Schema(description = "Fecha que se realiza la venta")
    private LocalDate fechaVenta;

    @Schema(description = "Monto total de la venta")
    @Min(value = 1, message = "Mínimo $1")
    @Max(value = 2000000, message = "Debe ser menor o igual a $2.000.000")
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
