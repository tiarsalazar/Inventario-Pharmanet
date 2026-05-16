package com.pharmanet.inventario_service.dto.lote;

import java.time.LocalDate;

import com.pharmanet.inventario_service.enums.EstadoLote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoteResponse {
    private String lote;
    private Integer cantidad;
    private LocalDate fechaVencimiento;
    private EstadoLote estado;
}
