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
@Schema(description = "Modelo de respuesta para detalles de recepcion.")
public class DetalleRecepcionResponse {
    private String sku;
    private Integer cantidad;
    private String codLote;
    private LocalDate fechaVencimiento;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
