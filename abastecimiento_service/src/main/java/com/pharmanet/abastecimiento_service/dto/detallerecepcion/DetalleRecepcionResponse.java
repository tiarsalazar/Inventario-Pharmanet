package com.pharmanet.abastecimiento_service.dto.detallerecepcion;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetalleRecepcionResponse {
    private Long id;
    private String sku;
    private Integer cantidad;
    private String codLote;
    private LocalDate fechaVencimiento;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
