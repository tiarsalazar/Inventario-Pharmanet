package com.pharmanet.inventario_service.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.pharmanet.inventario_service.dto.inventario.InventarioDetailResponse;
import com.pharmanet.inventario_service.dto.inventario.InventarioResponse;
import com.pharmanet.inventario_service.dto.lote.LoteResponse;
import com.pharmanet.inventario_service.dto.movimiento.MovimientoResponse;
import com.pharmanet.inventario_service.dto.recepcion.DetalleRecepcionRequest;
import com.pharmanet.inventario_service.entity.Inventario;
import com.pharmanet.inventario_service.entity.Lote;
import com.pharmanet.inventario_service.entity.Movimiento;
import com.pharmanet.inventario_service.enums.EstadoLote;
import com.pharmanet.inventario_service.enums.TipoMovimiento;

public class InventarioMapperTest {

    private final InventarioMapper mapper = new InventarioMapper();

    @Test
    @DisplayName("Deberia mappear entidad Lote a LoteResponse")
    void deberiaMappearLoteALoteResponse(){
        // GIVEN
        Lote lote = new Lote();
            lote.setCodLote("LO0001");
            lote.setFechaVencimiento(LocalDate.of(2030, 12, 31));
            lote.setCantidad(10);
            lote.setEstado(EstadoLote.ACTIVO);

        // WHEN
        LoteResponse loteResponse = mapper.toLoteResponse(lote);

        // THEN
         assertNotNull(loteResponse);
         assertEquals(lote.getCodLote(), loteResponse.getCodLote());
         assertEquals(lote.getFechaVencimiento(), loteResponse.getFechaVencimiento());
         assertEquals(lote.getCantidad(), loteResponse.getCantidad());
         assertEquals(lote.getEstado(), loteResponse.getEstado());
    }

    @Test
    @DisplayName("Deberia mappear un detalleRecepcion a una entidad Lote")
    void deberiaMappeardetalleRecepcionAEntidadLote(){
        // GIVEN
        DetalleRecepcionRequest request = new DetalleRecepcionRequest();
        request.setCodLote("LO0001");
        request.setCantidad(10);
        request.setFechaVencimiento(LocalDate.of(2030, 12, 31));

        // WHEN
        Lote lote = mapper.toLoteEntity(request);

        // THEN
         assertNotNull(lote);
         assertEquals(request.getCodLote(), lote.getCodLote());
         assertEquals(request.getFechaVencimiento(), lote.getFechaVencimiento());
         assertEquals(request.getCantidad(), lote.getCantidad());
    }

    @Test
    @DisplayName("Deberia mappear un Inventario a un InventarioDetailResponse")
    void deberiaMappearInventarioAInventarioDetailResponse(){
        // GIVEN
        Lote lote = new Lote();
            lote.setCodLote("LO0001");
            lote.setFechaVencimiento(LocalDate.of(2030, 12, 31));
            lote.setCantidad(10);
            lote.setEstado(EstadoLote.ACTIVO);

        Inventario inventario = new Inventario();
            inventario.setSku("SKU0001");
            inventario.setCodSucursal("SU0001");
            inventario.setStockTotal(10);
            inventario.setLotes(List.of(lote));

        //WHEN 
        InventarioDetailResponse resultado = mapper.toInventarioDetailResponse(inventario);

        //THEN
        assertNotNull(resultado);
        assertEquals(inventario.getSku(), resultado.getSku());
        assertEquals(inventario.getCodSucursal(), resultado.getCodSucursal());
        assertEquals(inventario.getStockTotal(), resultado.getStockTotal());
        assertEquals(inventario.getLotes().size(), resultado.getLotes().size());
    }

    @Test
    @DisplayName("Deberia mappear un Inventario a InventarioResponse sin detalle de lotes")
    void deberiaMappearInventarioAInventarioResponse(){
        // GIVEN
        Inventario inventario = new Inventario();
            inventario.setSku("SKU0001");
            inventario.setCodSucursal("SU0001");
            inventario.setStockTotal(10);

        // WHEN
        InventarioResponse resultado = mapper.toInventarioResponse(inventario);

        //THEN
        assertNotNull(resultado);
        assertEquals(inventario.getSku(), resultado.getSku());
        assertEquals(inventario.getCodSucursal(), resultado.getCodSucursal());
        assertEquals(inventario.getStockTotal(), resultado.getStockTotal());
    }

    @Test
    @DisplayName("Deberia mappear Movimiento a movimientoResponse")
    void deberiaMappearMovimientoAMovimientoResponse(){
        // GIVEN
        Movimiento movimiento = new Movimiento();
        movimiento.setTipo(TipoMovimiento.ENTRADA);
        movimiento.setSku("SKU0001");
        movimiento.setCodSucursal("SU0001");
        movimiento.setCodLote("LO0001");
        movimiento.setFecha(LocalDateTime.of(2026, 06, 22, 15, 30));
        movimiento.setCantidad(10);
        movimiento.setRunUsuario("11222333-4");

        // WHEN
        MovimientoResponse resultado = mapper.toMovimientoResponse(movimiento);

        //THEN
        assertNotNull(resultado);
        assertEquals(movimiento.getCantidad(), resultado.getCantidad());
        assertEquals(movimiento.getCodLote(), resultado.getCodLote());
        assertEquals(movimiento.getCodSucursal(), resultado.getCodSucursal());
        assertEquals(movimiento.getFecha(), resultado.getFecha());
        assertEquals(movimiento.getRunUsuario(), resultado.getRunUsuario());
        assertEquals(movimiento.getSku(), resultado.getSku());
        assertEquals(movimiento.getTipo(), resultado.getTipo());
    }

}
