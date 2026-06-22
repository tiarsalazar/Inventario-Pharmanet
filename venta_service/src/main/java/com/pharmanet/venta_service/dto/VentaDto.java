package com.pharmanet.venta_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Venta DTO", description = "Objeto de venta con la información accesible al usuario")
public class VentaDto {

    @Schema(description = "Codigo de la venta", example = "1")
    @NotNull(message = "Este campo no puede estar vacío.")
    private Long codVenta;
    
    @Schema(description = "Código de la sucursal", example = "SU0001")
    @NotBlank(message = "Este campo no puede estar vacío")
    private String codSucursal;

    @Schema(description = "Run del vendedor", example = "11111111-1")
    @NotBlank(message = "Este campo no puede estar vacío")
    @Pattern(regexp = "^[0-9]{7,8}-[0-9kK]$", message = "RUN inválido")
    private String run;

    @Schema(description = "Fecha que se realiza la venta", example = "2026/01/01")
    @NotNull(message = "Este campo no puede estar vacío")
    @PastOrPresent(message = "La fecha no puede ser posterior a la fecha actual")
    private LocalDate fechaVenta;

    @Schema(description = "Monto total de la venta", example = "10000")
    @NotNull(message = "Esta campo no puede estar vacío")
    @Min(value = 1, message = "Mínimo $1")
    @Max(value = 2000000, message = "Debe ser menor o igual a $2.000.000")
    private BigDecimal montoTotal;
}
