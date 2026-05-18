package com.pharmanet.abastecimiento_service.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.pharmanet.abastecimiento_service.mapper.RecepcionMapper;
import com.pharmanet.abastecimiento_service.repository.RecepcionRepository;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RecepcionService {

    private final RecepcionRepository recepRepo;
    private final RecepcionMapper recepMapper;
    private final InventarioClient inventarioClient;

    // ==== CONSULTAS GET ====

    @Transactional(readOnly = true)
    public Page<RecepcionResponse> buscarRecepcionPorSucursal(String codSucursal, Pageable pageable){
        log.info("Obteniendo historial de recepciones de sucursal: {}", codSucursal);
        return recepRepo.findByCodSucursal(codSucursal, pageable)
            .map(recepMapper::toRecepcionResponse);
    }

    @Transactional(readOnly = true)
    public Page<RecepcionResponse> buscarRecepcionPorProveedor(String rutProveedor, String codSucursal, Pageable pageable){
        log.info("Obteniendo historial de recepciones por proveedor: {}", rutProveedor);
        return recepRepo.findByRutProveedorAndCodSucursal(rutProveedor, codSucursal, pageable)
            .map(recepMapper::toRecepcionResponse);
    }

    @Transactional(readOnly = true)
    public Page<RecepcionResponse> buscarPorOrdenCompra(String ordenCompra, String codSucursal, Pageable pageable){
        log.info("Obteniendo historial de recepciones por orden de compra {}", ordenCompra);
        return recepRepo.findByOrdenCompraAndCodSucursal(ordenCompra, codSucursal, pageable)
            .map(recepMapper::toRecepcionResponse);
    }

    @Transactional(readOnly = true)
    public Page<RecepcionResponse> buscarPorFecha(String codSucursal, LocalDate inicio, LocalDate fin, Pageable pageable){
        log.info("Obteniendo historial de recepciones por rango de fechas: {} - {}", inicio, fin);
        return recepRepo.findByCodSucursalAndFechaIngresoBetween(codSucursal, inicio.atStartOfDay(), fin.atTime(LocalTime.MAX), pageable)
            .map(recepMapper::toRecepcionResponse);
    }

    @Transactional(readOnly = true)
    public Page<RecepcionResponse> buscarPorUsuario(String runUsuario, String codSucursal, Pageable pageable){
        log.info("Obteniendo historial de recepciones de usuario run: {}", runUsuario);
        return recepRepo.findByRunUsuarioAndCodSucursal(runUsuario, codSucursal, pageable)
        .map(recepMapper::toRecepcionResponse);
    }


    // ==== PETICIONES POST ====

    public RecepcionResponse registrarRecepcion(RecepcionRequest request, String runUsuario){
        log.info("Iniciando registro de recepcion documento: {}, proveedor: {}",
        request.getNumeroDocumento(), request.getRutProveedor());

        validarDocumentoDuplicado(request);

        Recepcion recepcion = recepMapper.toRecepcionEntity(request, runUsuario);
        procesarCalculos(recepcion);

        Recepcion guardada = recepRepo.save(recepcion);
        log.info("Recepcion guardada correctamente con ID {}, documento: {}, proveedor: {}",
        guardada.getId(), guardada.getNumeroDocumento(), guardada.getRutProveedor());

        procesarIngresoInventario(guardada, runUsuario);

        return recepMapper.toRecepcionResponse(guardada);
    }

    

        // === METODOS PRIVADOS ===
    
    // Valida que Recepcion no este duplicada por rut proveedor, tipo y numero de documento.
    private void validarDocumentoDuplicado(RecepcionRequest request){
        if (recepRepo.existsByRutProveedorAndTipoDocumentoAndNumeroDocumento(
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
