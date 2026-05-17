package com.pharmanet.abastecimiento_service.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmanet.abastecimiento_service.client.InventarioClient;
import com.pharmanet.abastecimiento_service.dto.inventario.DetalleIngresoInventario;
import com.pharmanet.abastecimiento_service.dto.inventario.IngresoInventario;
import com.pharmanet.abastecimiento_service.dto.recepcion.RecepcionRequest;
import com.pharmanet.abastecimiento_service.dto.recepcion.RecepcionResponse;
import com.pharmanet.abastecimiento_service.entity.Recepcion;
import com.pharmanet.abastecimiento_service.exception.BusinessException;
import com.pharmanet.abastecimiento_service.exception.ServiceCommunicationException;
import com.pharmanet.abastecimiento_service.mapper.AbastecimientoMapper;
import com.pharmanet.abastecimiento_service.repository.RecepcionRepository;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RecepcionService {

    private final RecepcionRepository recepcionRepo;
    private final AbastecimientoMapper aMapper;
    private final InventarioClient inventarioClient;

    public RecepcionResponse registrarRecepcion(RecepcionRequest request, String runUsuario){
        log.info("Iniciando registro de recepcion documento: {}, proveedor: {}",
        request.getNumeroDocumento(), request.getRutProveedor());

        validarDocumentoDuplicado(request);

        Recepcion recepcion = aMapper.toRecepcionEntity(request, runUsuario);
        procesarCalculos(recepcion);

        Recepcion guardada = recepcionRepo.save(recepcion);
        log.info("Recepcion guardada correctamente con ID {}, documento: {}, proveedor: {}",
        guardada.getId(), guardada.getNumeroDocumento(), guardada.getRutProveedor());

        procesarIngresoInventario(guardada, runUsuario);

        return aMapper.toRecepcionDto(guardada);
    }

        // === METODOS PRIVADOS ===
    
    // Valida que Recepcion no este duplicada por rut proveedor, tipo y numero de documento.
    private void validarDocumentoDuplicado(RecepcionRequest request){
        if (recepcionRepo.existsByRutProveedorAndTipoDocumentoAndNumeroDocumento(
            request.getRutProveedor(), request.getTipoDocumento(),request.getNumeroDocumento())){
                log.warn("[VALIDACION] Intento de registro duplicado documento {} proveedor {}",
                request.getNumeroDocumento(), request.getRutProveedor());
                throw new BusinessException("La recepción de este documento ya se encuentra registrada en el sistema.");
        }
    }

    // Calcula y settea monto total y subtotales a una Recepcion.
    private void procesarCalculos(Recepcion recepcion){
        BigDecimal montoTotal = recepcion.getDetalles().stream().map(d -> {
            BigDecimal subtotal = d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad()));
            d.setSubtotal(subtotal);
            return subtotal;
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
        recepcion.setMontoTotal(montoTotal);
    }

    // Crea el DTO para hacer peticion de ingreso de stock a Inventario.
    private IngresoInventario construirIngresoInventario(Recepcion recepcion){
        IngresoInventario dto  = new IngresoInventario(
            recepcion.getCodSucursal(),
            recepcion.getDetalles().stream().map(d -> new DetalleIngresoInventario(
            d.getSku(),
            d.getCantidad(),
            d.getCodLote(),
            d.getFechaVencimiento()))
            .toList());
        return dto;
    }

    private void procesarIngresoInventario(Recepcion recepcion, String runUsuario) {
        IngresoInventario ingresoInventario = construirIngresoInventario(recepcion);
        
        try {
            inventarioClient.registrarStockRecepcion(ingresoInventario, runUsuario);
        } catch (FeignException.NotFound ex) {
            throw new BusinessException("No se pudo registrar la recepción: " + ex.getMessage());
        } catch (FeignException ex) {
            throw new ServiceCommunicationException("Error de comunicación con el servicio de Inventario al actualizar stock.");
        } catch (Exception ex) {
            throw new ServiceCommunicationException("Error inesperado al conectar con inventario.");
        }
    }
}
