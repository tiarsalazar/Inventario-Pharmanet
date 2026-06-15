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
@Schema(description = "Estructura de respuesta con los datos de la recepcion recibida.")
public class RecepcionResponse {
    @Schema(description = "ID unico autogenerado de la recepción", example = "1")
    private Long id;
    @Schema(description = "RUN del usuario que operó la transacción", example = "11222333-4")
    private String runUsuario;
    @Schema(description = "Código de la sucursal donde se ingreso la recepción", example = "SU0001")
    private String codSucursal;
    @Schema(description = "Numero del documento", example = "12345")
    private String numeroDocumento;
    @Schema(description = "Tipo de documento legal adjunto.", example = "GUIA_DESPACHO / FACTURA")
    private TipoDocumento tipoDocumento;
    @Schema(description = "RUT del proveedor.", example = "98.765.432-1")
    private String rutProveedor;
    @Schema(description = "Fecha y hora de la recepción sistemica.", example = "2026-06-15T12:00:00")
    private LocalDateTime fechaIngreso;
    @Schema(description = "Suma total de los subtotales de la recepcion.", example = "75025.50")
    private BigDecimal montoTotal;
    @Schema(description = "Estado actual de la recepcion.", example = "PROCESADA / CANCELADA / MODIFICADA")
    private EstadoRecepcion estado;
    @Schema(description = "Listado con el detalle de productos recibidos.")
    private List<DetalleRecepcionResponse> detalles;
}