package com.pharmanet.abastecimiento_service.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.pharmanet.abastecimiento_service.dto.detallerecepcion.DetalleRecepcionRequest;
import com.pharmanet.abastecimiento_service.dto.detallerecepcion.DetalleRecepcionResponse;
import com.pharmanet.abastecimiento_service.dto.recepcion.RecepcionRequest;
import com.pharmanet.abastecimiento_service.dto.recepcion.RecepcionResponse;
import com.pharmanet.abastecimiento_service.entity.DetalleRecepcion;
import com.pharmanet.abastecimiento_service.entity.Recepcion;

@Component
public class AbastecimientoMapper {

    public Recepcion toRecepcionEntity(RecepcionRequest request){
        Recepcion recepcion = new Recepcion();
        recepcion.setOrdenCompra(request.getOrdenCompra());
        recepcion.setCodSucursal(request.getCodSucursal());
        recepcion.setNumeroDocumento(request.getNumeroDocumento());
        recepcion.setTipoDocumento(request.getTipoDocumento());
        recepcion.setRutProveedor(request.getRutProveedor());
        recepcion.setNombreProveedor(request.getNombreProveedor());
        recepcion.setObservaciones(request.getObservaciones());

        request.getDetalles().stream()
        .map(this::toDetalleEntity)
        .forEach(recepcion::addDetalle);

        return recepcion;
    }

    public DetalleRecepcion toDetalleEntity(DetalleRecepcionRequest request){
        DetalleRecepcion detalle = new DetalleRecepcion();
        detalle.setSku(request.getSku());
        detalle.setCantidad(request.getCantidad());
        detalle.setCodLote(request.getCodLote());
        detalle.setFechaVencimiento(request.getFechaVencimiento());
        detalle.setPrecioUnitario(request.getPrecioUnitario());
        return detalle;
    }

    public RecepcionResponse toRecepcionDto(Recepcion recepcion){
        RecepcionResponse dto = new RecepcionResponse();
        dto.setOrdenCompra(recepcion.getOrdenCompra());
        dto.setCodSucursal(recepcion.getCodSucursal());
        dto.setNumeroDocumento(recepcion.getNumeroDocumento());
        dto.setTipoDocumento(recepcion.getTipoDocumento());
        dto.setRutProveedor(recepcion.getRutProveedor());
        dto.setNombreProveedor(recepcion.getNombreProveedor());
        dto.setFechaIngreso(recepcion.getFechaIngreso());
        dto.setObservaciones(recepcion.getObservaciones());
        dto.setMontoTotal(recepcion.getMontoTotal());

        List<DetalleRecepcionResponse> detalles = recepcion.getDetalles().stream()
        .map(this::toDetalleDto)
        .toList();
        dto.setDetalles(detalles);
        return dto;
    }

    public DetalleRecepcionResponse toDetalleDto(DetalleRecepcion detalle){
        DetalleRecepcionResponse dto = new DetalleRecepcionResponse();
        dto.setSku(detalle.getSku());
        dto.setCodLote(detalle.getCodLote());
        dto.setFechaVencimiento(detalle.getFechaVencimiento());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());
        return dto;
    }
}
