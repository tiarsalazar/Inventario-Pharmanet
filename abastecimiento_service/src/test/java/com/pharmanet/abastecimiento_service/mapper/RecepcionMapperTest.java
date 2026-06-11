package com.pharmanet.abastecimiento_service.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.pharmanet.abastecimiento_service.dto.detallerecepcion.DetalleRecepcionRequest;
import com.pharmanet.abastecimiento_service.dto.detallerecepcion.DetalleRecepcionResponse;
import com.pharmanet.abastecimiento_service.dto.recepcion.RecepcionRequest;
import com.pharmanet.abastecimiento_service.dto.recepcion.RecepcionResponse;
import com.pharmanet.abastecimiento_service.entity.DetalleRecepcion;
import com.pharmanet.abastecimiento_service.entity.Recepcion;
import com.pharmanet.abastecimiento_service.enums.EstadoRecepcion;

class RecepcionMapperTest {

    private final RecepcionMapper mapper = new RecepcionMapper();

    @Test
    @DisplayName("Deberia mapear RecepcionRequest a entidad Recepcion con sus detalles")
    void deberiaMapearRecepcionRequestAEntidadRecepcion() {
        // GIVEN
        String runUsuario = "11222333-4";
        String codSucursal = "SU0001";

        DetalleRecepcionRequest detalleRequest = new DetalleRecepcionRequest();
        detalleRequest.setSku("SKU123");
        detalleRequest.setCantidad(BigDecimal.valueOf(10));
        detalleRequest.setCodLote("LOTE-A");
        detalleRequest.setFechaVencimiento(LocalDate.of(2026, 12, 31));
        detalleRequest.setPrecioUnitario(BigDecimal.valueOf(1500));

        RecepcionRequest request = new RecepcionRequest();
        request.setNumeroDocumento("DOC-999");
        request.setRutProveedor("77666555-4");
        request.setDetalles(List.of(detalleRequest));

        // WHEN
        Recepcion resultado = mapper.toRecepcionEntity(request, runUsuario, codSucursal);

        // THEN
        assertNotNull(resultado);
        assertEquals(runUsuario, resultado.getRunUsuario());
        assertEquals(codSucursal, resultado.getCodSucursal());
        assertEquals("DOC-999", resultado.getNumeroDocumento());
        assertEquals(EstadoRecepcion.PROCESADA, resultado.getEstado());
    }

    @Test
    @DisplayName("Deberia mapear DetalleRecepcionRequest a entidad DetalleRecepcion")
    void deberiaMapearDetalleRequestAEntidadDetalle() {
        // GIVEN
        DetalleRecepcionRequest request = new DetalleRecepcionRequest();
        request.setSku("SKU789");
        request.setCantidad(BigDecimal.valueOf(5));
        request.setCodLote("LOTE-B");
        request.setFechaVencimiento(LocalDate.of(2027, 5, 20));
        request.setPrecioUnitario(BigDecimal.valueOf(3000));

        // WHEN
        DetalleRecepcion resultado = mapper.toDetalleEntity(request);

        // THEN
        assertNotNull(resultado);
        assertEquals("SKU789", resultado.getSku());
        assertEquals(5, resultado.getCantidad());
        assertEquals("LOTE-B", resultado.getCodLote());
        assertEquals(LocalDate.of(2027, 5, 20), resultado.getFechaVencimiento());
        assertEquals(0, BigDecimal.valueOf(3000).compareTo(resultado.getPrecioUnitario()));
    }

    @Test
    @DisplayName("Deberia mapear entidad Recepcion a RecepcionResponse con sus detalles")
    void deberiaMapearEntidadRecepcionAResponse() {
        // GIVEN
        Recepcion entidad = new Recepcion();
        entidad.setId(1L);
        entidad.setRunUsuario("11222333-4");
        entidad.setCodSucursal("SU0001");
        entidad.setNumeroDocumento("DOC-999");
        entidad.setMontoTotal(BigDecimal.valueOf(15000));
        entidad.setEstado(EstadoRecepcion.PROCESADA);

        DetalleRecepcion detalleEntidad = new DetalleRecepcion();
        detalleEntidad.setSku("SKU123");
        detalleEntidad.setCantidad(5);
        detalleEntidad.setPrecioUnitario(BigDecimal.valueOf(3000));
        
        entidad.setDetalles(new ArrayList<>(List.of(detalleEntidad)));

        // WHEN
        RecepcionResponse response = mapper.toRecepcionResponse(entidad);

        // THEN
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("DOC-999", response.getNumeroDocumento());
        assertEquals(EstadoRecepcion.PROCESADA, response.getEstado());
        assertEquals(0, BigDecimal.valueOf(15000).compareTo(response.getMontoTotal()));
        
        assertEquals(1, response.getDetalles().size());
        assertEquals("SKU123", response.getDetalles().get(0).getSku());
    }

    @Test
    @DisplayName("Deberia mapear entidad DetalleRecepcion a DetalleRecepcionResponse")
    void deberiaMapearEntidadDetalleAResponse() {
        // GIVEN
        DetalleRecepcion detalle = new DetalleRecepcion();
        detalle.setSku("SKU456");
        detalle.setCodLote("LOTE-C");
        detalle.setFechaVencimiento(LocalDate.of(2026, 8, 15));
        detalle.setCantidad(20);
        detalle.setPrecioUnitario(BigDecimal.valueOf(500));

        // WHEN
        DetalleRecepcionResponse response = mapper.toDetalleResponse(detalle);

        // THEN
        assertNotNull(response);
        assertEquals("SKU456", response.getSku());
        assertEquals("LOTE-C", response.getCodLote());
        assertEquals(LocalDate.of(2026, 8, 15), response.getFechaVencimiento());
        assertEquals(20, response.getCantidad());
        assertEquals(0, BigDecimal.valueOf(500).compareTo(response.getPrecioUnitario()));
    }
}