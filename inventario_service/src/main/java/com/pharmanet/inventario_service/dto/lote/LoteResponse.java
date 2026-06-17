package com.pharmanet.inventario_service.dto.lote;

import java.time.LocalDate;

import com.pharmanet.inventario_service.enums.EstadoLote;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Estructura de respuesta para el listado de lotes del Inventario.")
public class LoteResponse {
    @Schema(description = "Codigo del lote.", example = "LOT-PR00001-001")
    private String codLote;
    @Schema(description = "Cantidad activa del lote", example = "15")
    private Integer cantidad;
    @Schema(description = "Fecha de vencimiento", example = "2027-12-31")
    private LocalDate fechaVencimiento;
    @Schema(description = "Estado actual del lote.", example = "ACTIVO")
    private EstadoLote estado;
}
