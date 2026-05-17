package com.pharmanet.abastecimiento_service.dto.recepcion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.pharmanet.abastecimiento_service.dto.detallerecepcion.DetalleRecepcionResponse;
import com.pharmanet.abastecimiento_service.enums.TipoDocumento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecepcionResponse {
    private String ordenCompra;
    private String codSucursal;
    private String numeroDocumento;
    private TipoDocumento tipoDocumento;
    private String rutProveedor;
    private String nombreProveedor;
    private LocalDate fechaIngreso;
    private String observaciones;
    private BigDecimal montoTotal;
    private List<DetalleRecepcionResponse> detalles;
}