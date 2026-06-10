package com.pharmanet.abastecimiento_service.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmanet.abastecimiento_service.client.InventarioClient;
import com.pharmanet.abastecimiento_service.client.UsuarioClient;
import com.pharmanet.abastecimiento_service.dto.inventario.DetalleIngresoInventario;
import com.pharmanet.abastecimiento_service.dto.inventario.IngresoInventario;
import com.pharmanet.abastecimiento_service.dto.recepcion.RecepcionRequest;
import com.pharmanet.abastecimiento_service.dto.recepcion.RecepcionResponse;
import com.pharmanet.abastecimiento_service.entity.Recepcion;
import com.pharmanet.abastecimiento_service.enums.EstadoRecepcion;
import com.pharmanet.abastecimiento_service.exception.BusinessException;
import com.pharmanet.abastecimiento_service.exception.ResourceAlreadyExistsException;
import com.pharmanet.abastecimiento_service.exception.ResourceNotFoundException;
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
    private final UsuarioClient usuarioClient;

    // ==== CONSULTAS GET ====

    @Transactional(readOnly = true)
    public RecepcionResponse buscarPorId(Long id, String codSucursal){
        log.info("Buscando recepcion por id: {}", id);
        return recepRepo.findByIdAndCodSucursal(id, codSucursal).map(recepMapper::toRecepcionResponse)
        .orElseThrow(() -> new ResourceNotFoundException("Recepcion no encontrada."));
    }

    @Transactional(readOnly = true)
    public Page<RecepcionResponse> buscarRecepcionPorSucursal(String codSucursal, Pageable pageable){
        log.info("Obteniendo historial de recepciones de sucursal: {}", codSucursal);
        return recepRepo.findByCodSucursal(codSucursal, pageable)
            .map(recepMapper::toRecepcionResponse);
    }

    @Transactional(readOnly = true)
    public Page<RecepcionResponse> buscarPorFecha(String codSucursal, LocalDate inicio, LocalDate fin, Pageable pageable){
        log.info("Obteniendo historial de recepciones por rango de fechas: {} - {}", inicio, fin);
        if (inicio.isAfter(fin)){
            throw new BusinessException("La fecha de inicio no puede ser posterior a fecha de termino.");
        }
        return recepRepo.findByCodSucursalAndFechaIngresoBetween(codSucursal, inicio.atStartOfDay(), fin.atTime(LocalTime.MAX), pageable)
            .map(recepMapper::toRecepcionResponse);
    }

    @Transactional(readOnly = true)
    public Page<RecepcionResponse> buscarPorUsuario(String runUsuario, String codSucursal, Pageable pageable){
        log.info("Obteniendo historial de recepciones de usuario run: {}", runUsuario);
        validarExistenciaUsuario(runUsuario);
        return recepRepo.findByRunUsuarioAndCodSucursal(runUsuario, codSucursal, pageable)
        .map(recepMapper::toRecepcionResponse);
    }


    // ==== PETICIONES POST ====

    public RecepcionResponse registrarRecepcion(RecepcionRequest request, String runUsuario, String codSucursal){
        log.info("Iniciando registro de recepcion documento: {}, proveedor: {}",
        request.getNumeroDocumento(), request.getRutProveedor());

        log.info("Validacion de usuario {} con feign", runUsuario);
        validarExistenciaUsuario(runUsuario);
        validarDocumentoDuplicado(request);

        Recepcion recepcion = recepMapper.toRecepcionEntity(request, runUsuario, codSucursal);
        procesarCalculos(recepcion);

        Recepcion guardada = recepRepo.save(recepcion);
        log.info("Recepcion guardada correctamente con ID {}, documento: {}, proveedor: {}",
        guardada.getId(), guardada.getNumeroDocumento(), guardada.getRutProveedor());

        log.info("Enviando stock de recepcion ID {} a servicio Inventario", guardada.getId());
        procesarIngresoInventario(guardada, runUsuario);

        return recepMapper.toRecepcionResponse(guardada);
    }


    // ==== PETICIONES PUT ====

    public void cancelarRecepcionPorId(Long id, String codSucursal){
        log.info("Cancelando recepcion ID {}", id);
        Recepcion recepcion = recepRepo.findByIdAndCodSucursal(id, codSucursal)
        .orElseThrow(() -> new ResourceNotFoundException("Recepcion no encontrada."));
        //Aqui deberia llamar a inventario para quitar el stock.
        recepcion.setEstado(EstadoRecepcion.CANCELADA);
        recepRepo.save(recepcion);
        log.info("Recepcion ID {} cambiada a estado CANCELADA", id);
    }

    // ==== PETICIONES DELETE ==== 

    public void eliminarRecepcionPorId(Long id, String codSucursal){
        log.info("Eliminando recepcion ID {}", id);
        Recepcion recepcion = recepRepo.findByIdAndCodSucursal(id, codSucursal)
        .orElseThrow(() -> new ResourceNotFoundException("Recepcion no encontrada."));
        //Aqui deberia llamar a inventario para quitar el stock.
        recepRepo.delete(recepcion);
        log.info("Recepcion ID {} eliminada exitosamente", id);
    }

        // === METODOS PRIVADOS ===
    
    // Valida que Recepcion no este duplicada por rut proveedor, tipo y numero de documento.
    private void validarDocumentoDuplicado(RecepcionRequest request){
        if (recepRepo.existsByRutProveedorAndTipoDocumentoAndNumeroDocumento(
            request.getRutProveedor(), request.getTipoDocumento(),request.getNumeroDocumento())){
                log.warn("[VALIDACION] Intento de registro duplicado documento {} proveedor {}",
                request.getNumeroDocumento(), request.getRutProveedor());
                throw new ResourceAlreadyExistsException("La recepción de este documento ya se encuentra registrada en el sistema.");
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
            log.warn("Inventario retorno 404 para recepcion ID {}: {}", recepcion.getId(), ex.getMessage());
            throw new BusinessException("No se pudo registrar la recepción: " + ex.getMessage());
        } catch (FeignException ex) {
            log.error("Error de Feign al comunicar con Inventario.");
            throw new ServiceCommunicationException("Error de comunicación con el servicio de Inventario al actualizar stock.");
        } catch (Exception ex) {
            log.error("Error inesperado no controlado al conectar con inventario", ex);
            throw new ServiceCommunicationException("Error inesperado al conectar con inventario.");
        }
    }

    // Valida existencia de Usuario con FEIGN
    private void validarExistenciaUsuario(String run) {
        try {
            usuarioClient.buscarPorRun(run);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Usuario no encontrado con RUN: " + run);
        } catch (FeignException e) {
            log.error("Error de Feign al comunicar con el servicio de Usuarios.");
            throw new ServiceCommunicationException("Error al comunicarse con el servicio de Usuario.");
        }
    }
}
