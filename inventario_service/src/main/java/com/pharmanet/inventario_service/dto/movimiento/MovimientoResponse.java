package com.pharmanet.inventario_service.dto.movimiento;

import java.time.LocalDateTime;

import com.pharmanet.inventario_service.enums.TipoMovimiento;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Estructura de respuesta de un movimiento en el Inventario.")
public class MovimientoResponse {
    @Schema(description = "Tipo de movimiento realizado.", example = "ENTRADA")
    private TipoMovimiento tipo;
    @Schema(description = "Codigo interno unico del producto.", example = "PR00001")
    private String sku;
    @Schema(description = "Codigo interno unido de una Sucursal", example = "SU0001")
    private String codSucursal;
    @Schema(description = "Cantidad de stock en movimiento", example = "15")
    private Integer cantidad;
    @Schema(description = "Fecha y hora de creación de movimiento.", example = "2026-06-15T12:00:00")
    private LocalDateTime fecha;
    @Schema(description = "RUN del usuario que operó la transacción", example = "11222333-4")
    private String runUsuario;
    @Schema(description = "Codigo del lote.", example = "LOT-PR00001-001")
    private String codLote;
}
