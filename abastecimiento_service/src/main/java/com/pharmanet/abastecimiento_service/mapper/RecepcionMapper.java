package com.pharmanet.abastecimiento_service.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.pharmanet.abastecimiento_service.dto.detallerecepcion.DetalleRecepcionRequest;
import com.pharmanet.abastecimiento_service.dto.detallerecepcion.DetalleRecepcionResponse;
import com.pharmanet.abastecimiento_service.dto.recepcion.RecepcionRequest;
import com.pharmanet.abastecimiento_service.dto.recepcion.RecepcionResponse;
import com.pharmanet.abastecimiento_service.entity.DetalleRecepcion;
import com.pharmanet.abastecimiento_service.entity.Recepcion;
import com.pharmanet.abastecimiento_service.enums.EstadoRecepcion;

@Component
public class RecepcionMapper {

    public Recepcion toRecepcionEntity(RecepcionRequest request, String runUsuario, String codSucursal){
        Recepcion recepcion = new Recepcion();
        recepcion.setRunUsuario(runUsuario);
        recepcion.setCodSucursal(codSucursal);
        recepcion.setNumeroDocumento(request.getNumeroDocumento());
        recepcion.setTipoDocumento(request.getTipoDocumento());
        recepcion.setRutProveedor(request.getRutProveedor());
        recepcion.setEstado(EstadoRecepcion.PROCESADA);

        request.getDetalles().stream()
        .map(this::toDetalleEntity)
        .forEach(recepcion::addDetalle);

        return recepcion;
    }

    public DetalleRecepcion toDetalleEntity(DetalleRecepcionRequest request){
        DetalleRecepcion detalle = new DetalleRecepcion();
        detalle.setSku(request.getSku());
        detalle.setCantidad(request.getCantidad().intValueExact());
        detalle.setCodLote(request.getCodLote());
        detalle.setFechaVencimiento(request.getFechaVencimiento());
        detalle.setPrecioUnitario(request.getPrecioUnitario());
        return detalle;
    }

    public RecepcionResponse toRecepcionResponse(Recepcion recepcion){
        RecepcionResponse response = new RecepcionResponse();
        response.setId(recepcion.getId());
        response.setRunUsuario(recepcion.getRunUsuario());
        response.setCodSucursal(recepcion.getCodSucursal());
        response.setNumeroDocumento(recepcion.getNumeroDocumento());
        response.setTipoDocumento(recepcion.getTipoDocumento());
        response.setRutProveedor(recepcion.getRutProveedor());
        response.setFechaIngreso(recepcion.getFechaIngreso());
        response.setMontoTotal(recepcion.getMontoTotal());
        response.setEstado(recepcion.getEstado());

        List<DetalleRecepcionResponse> detalles = recepcion.getDetalles().stream()
        .map(this::toDetalleResponse)
        .toList();
        response.setDetalles(detalles);
        return response;
    }

    public DetalleRecepcionResponse toDetalleResponse(DetalleRecepcion detalle){
        DetalleRecepcionResponse response = new DetalleRecepcionResponse();
        response.setSku(detalle.getSku());
        response.setCodLote(detalle.getCodLote());
        response.setFechaVencimiento(detalle.getFechaVencimiento());
        response.setCantidad(detalle.getCantidad());
        response.setPrecioUnitario(detalle.getPrecioUnitario());
        response.setSubtotal(detalle.getSubtotal());
        return response;
    }
}
