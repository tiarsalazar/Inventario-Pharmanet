package com.pharmanet.inventario_service.dto.movimiento;

import java.time.LocalDateTime;

import com.pharmanet.inventario_service.enums.TipoMovimiento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovimientoResponse {
    private TipoMovimiento tipo;
    private String sku;
    private Integer cantidad;
    private LocalDateTime fecha;
    private String rutUsuario;
    private String codLote;
}
