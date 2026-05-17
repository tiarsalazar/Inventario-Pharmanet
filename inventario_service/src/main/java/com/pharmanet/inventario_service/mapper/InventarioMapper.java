package com.pharmanet.inventario_service.mapper;

import org.springframework.stereotype.Component;

import com.pharmanet.inventario_service.dto.inventario.InventarioDetailResponse;
import com.pharmanet.inventario_service.dto.inventario.InventarioResponse;
import com.pharmanet.inventario_service.dto.lote.LoteResponse;
import com.pharmanet.inventario_service.dto.movimiento.MovimientoResponse;
import com.pharmanet.inventario_service.dto.recepcion.DetalleRecepcionRequest;
import com.pharmanet.inventario_service.entity.Inventario;
import com.pharmanet.inventario_service.entity.Lote;
import com.pharmanet.inventario_service.entity.Movimiento;

@Component
public class InventarioMapper {

    public LoteResponse toLoteResponse(Lote lote){
        return new LoteResponse(
            lote.getCodLote(),
            lote.getCantidad(),
            lote.getFechaVencimiento(),
            lote.getEstado());
    }

    public Lote toLoteEntity(DetalleRecepcionRequest request){
        Lote lote = new Lote();
        lote.setCodLote(request.getCodLote());
        lote.setCantidad(request.getCantidad());
        lote.setFechaVencimiento(request.getFechaVencimiento());
        return lote;
    }

    public InventarioDetailResponse toInventarioDetailResponse(Inventario inventario){
        return new InventarioDetailResponse(
            inventario.getSku(),
            inventario.getCodSucursal(),
            inventario.getStockTotal(),
            inventario.getLotes().stream()
                .map(this::toLoteResponse)
                .toList());
    }

    public InventarioResponse toInventarioResponse(Inventario inventario){
        return new InventarioResponse(
            inventario.getSku(),
            inventario.getCodSucursal(),
            inventario.getStockTotal());
    }

    public MovimientoResponse toMovimientoResponse(Movimiento movimiento){
        return new MovimientoResponse(
            movimiento.getTipo(),
            movimiento.getSku(),
            movimiento.getCantidad(),
            movimiento.getFecha(),
            movimiento.getRutUsuario(),
            movimiento.getCodLote());
    }
}
