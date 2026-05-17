package com.pharmanet.abastecimiento_service.dto.inventario;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetalleIngresoInventario {
    private String sku;
    private Integer cantidad;
    private String codLote;
    private LocalDate fechaVencimiento;
}
