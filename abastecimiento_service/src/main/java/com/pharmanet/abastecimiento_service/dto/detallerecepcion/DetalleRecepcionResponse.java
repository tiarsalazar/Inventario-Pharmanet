package com.pharmanet.abastecimiento_service.dto.detallerecepcion;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Estructura de respuesta para el detalle del producto")
public class DetalleRecepcionResponse {
    @Schema(description = "Código único de producto (SKU)", example = "PR00001")
    private String sku;
    @Schema(description = "Cantidad de unidades", example = "50")
    private Integer cantidad;
    @Schema(description = "Código del lote asignado", example = "LOT-2026-A")
    private String codLote;
    @Schema(description = "Fecha de vencimiento", example = "2027-12-31")
    private LocalDate fechaVencimiento;
    @Schema(description = "Precio unitario", example = "1500.50")
    private BigDecimal precioUnitario;
    @Schema(description = "Resultado multiplicativo de cantidad por precio unitario", example = "75025.00")
    private BigDecimal subtotal;
}
