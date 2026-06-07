package com.pharmanet.abastecimiento_service.dto.recepcion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.pharmanet.abastecimiento_service.dto.detallerecepcion.DetalleRecepcionResponse;
import com.pharmanet.abastecimiento_service.enums.EstadoRecepcion;
import com.pharmanet.abastecimiento_service.enums.TipoDocumento;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de respuesta para recepcion.")
public class RecepcionResponse {
    private Long id;
    private String runUsuario;
    private String codSucursal;
    private String numeroDocumento;
    private TipoDocumento tipoDocumento;
    private String rutProveedor;
    private LocalDateTime fechaIngreso;
    private BigDecimal montoTotal;
    private EstadoRecepcion estado;
    private List<DetalleRecepcionResponse> detalles;
}