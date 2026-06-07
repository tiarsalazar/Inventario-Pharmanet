package com.pharmanet.abastecimiento_service.dto.detallerecepcion;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de entrada de detalles para recepcion.")
public class DetalleRecepcionRequest {
    @NotBlank(message = "SKU es obligatorio")
    @Size(max = 30, message = "SKU no debe superar 30 caracteres")
    private String sku;
    @NotNull(message = "Cantidad es obligatoria")
    @Min(value = 1, message = "Cantidad debe ser mayor a 0")
    @Digits(integer = 6, fraction = 0, message = "La cantidad debe ser un número entero sin decimales")
    private BigDecimal cantidad;
    @NotBlank(message = "Lote es obligatorio")
    @Size(max = 30, message = "Lote no puede superar 30 caracteres")
    private String codLote;
    @NotNull(message = "Fecha de vencimiento es obligatoria")
    @Future(message = "Fecha de vencimiento debe ser futura")
    private LocalDate fechaVencimiento;
    @NotNull(message = "Precio unitario es obligatorio")
    @DecimalMin(value = "0.01", message = "Precio unitario debe ser mayor a 0")
    private BigDecimal precioUnitario;
}
