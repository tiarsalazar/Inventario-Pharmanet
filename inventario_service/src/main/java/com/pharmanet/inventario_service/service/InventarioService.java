package com.pharmanet.inventario_service.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmanet.inventario_service.client.producto.ProductoClient;
import com.pharmanet.inventario_service.client.sucursal.SucursalClient;
import com.pharmanet.inventario_service.dto.inventario.InventarioDetailResponse;
import com.pharmanet.inventario_service.dto.inventario.InventarioResponse;
import com.pharmanet.inventario_service.dto.lote.LoteResponse;
import com.pharmanet.inventario_service.dto.movimiento.MovimientoResponse;
import com.pharmanet.inventario_service.dto.recepcion.DetalleRecepcionRequest;
import com.pharmanet.inventario_service.dto.recepcion.RecepcionRequest;
import com.pharmanet.inventario_service.dto.venta.VentaRequest;
import com.pharmanet.inventario_service.entity.Inventario;
import com.pharmanet.inventario_service.entity.Lote;
import com.pharmanet.inventario_service.entity.Movimiento;
import com.pharmanet.inventario_service.enums.EstadoLote;
import com.pharmanet.inventario_service.enums.TipoMovimiento;
import com.pharmanet.inventario_service.exception.BusinessException;
import com.pharmanet.inventario_service.exception.ResourceNotFoundException;
import com.pharmanet.inventario_service.exception.ServiceCommunicationException;
import com.pharmanet.inventario_service.mapper.InventarioMapper;
import com.pharmanet.inventario_service.repository.InventarioRepository;
import com.pharmanet.inventario_service.repository.LoteRepository;
import com.pharmanet.inventario_service.repository.MovimientoRepository;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventarioService {

    private final InventarioMapper mapper;
    private final InventarioRepository invRepo;
    private final MovimientoRepository movRepo;
    private final LoteRepository loteRepo;
    private final ProductoClient productoClient;
    private final SucursalClient sucursalClient;

    // ==== CONSULTAS INVENTARIO =====

    @Transactional(readOnly = true)
    public InventarioResponse obtenerInventarioPorSku(String sku, String codSucursal){
        log.info("Obteniendo inventario por sku: {} en sucursal: {}", sku, codSucursal);
        return invRepo.findBySkuAndCodSucursal(sku, codSucursal)
        .map(mapper::toInventarioResponse)
        .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado para sku: "+sku));
    }

    @Transactional(readOnly = true)
    public InventarioDetailResponse obtenerInventarioDetailPorSku(String sku, String codSucursal){
        log.info("Obteniendo inventario por sku: {} en sucursal: {}", sku, codSucursal);
        return invRepo.findBySkuAndCodSucursal(sku, codSucursal)
        .map(mapper::toInventarioDetailResponse)
        .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado para sku: "+sku));
    }

    @Transactional(readOnly = true)
    public Page<InventarioResponse> obtenerInventarioPorSucursal(String codSucursal, Pageable pageable){
        log.info("Obteniendo inventarios de sucursal: {}",codSucursal);
        return invRepo.findByCodSucursal(codSucursal, pageable)
            .map(mapper::toInventarioResponse);
    }

    // ==== CONSULTAS MOVIMIENTOS ====

    @Transactional(readOnly = true)
    public Page<MovimientoResponse> obtenerMovimientoPorSku(String sku, String codSucursal, Pageable pageable){
        log.info("Obteniendo movimientos del lote: {}", sku);
        return movRepo.findBySkuAndCodSucursal(sku, codSucursal, pageable)
            .map(mapper::toMovimientoResponse);
    }

    @Transactional(readOnly = true)
    public Page<MovimientoResponse> obtenerMovimientoPorUsuario(String runUsuario, String codSucursal, Pageable pageable){
        log.info("Obteniendo movimientos por usuario run: {}", runUsuario);
        return movRepo.findByRunUsuarioAndCodSucursal(runUsuario, codSucursal, pageable).map(mapper::toMovimientoResponse);
    }

    @Transactional(readOnly = true)
    public Page<MovimientoResponse> obtenerMovimientosPorfecha(String codSucursal, LocalDateTime inicio, LocalDateTime fin, Pageable pageable){
        log.info("Obteniendo movimientos entre: {} y {}", inicio, fin);
        return movRepo.findByCodSucursalAndFechaBetween(codSucursal, inicio, fin, pageable)
            .map(mapper::toMovimientoResponse);
    }

    @Transactional(readOnly = true)
    public Page<MovimientoResponse> obtenerMovimientoPorSucursal(String codSucursal, Pageable pageable){
        log.info("Obteniendo movimientos de sucursal: {}", codSucursal);
        return movRepo.findByCodSucursal(codSucursal, pageable)
            .map(mapper::toMovimientoResponse);
    }


    // ==== PETICIONES ====
    // Recibe peticion de ABASTECIMIENTO para INGRESAR una recepcion.
    public List<LoteResponse> registrarRecepcion(RecepcionRequest request, String runUsuario){
        log.info("Registrando recepcion para sucursal: {}", request.getCodSucursal());

        validarSucursal(request.getCodSucursal());
       
        List<LoteResponse> response = new ArrayList<>();
        for (DetalleRecepcionRequest detalleRequest : request.getDetalles()){

            validarProducto(detalleRequest.getSku());

            // Busca Inventario. Si no existe lo crea y persiste.
            Inventario inventario = invRepo.findBySkuAndCodSucursal(detalleRequest.getSku(), request.getCodSucursal())
                .orElseGet(() -> {
                Inventario nuevoInv = new Inventario();
                nuevoInv.setSku(detalleRequest.getSku());
                nuevoInv.setCodSucursal(request.getCodSucursal());
                nuevoInv.setStockTotal(0);
                return invRepo.save(nuevoInv);});

            Lote lote = mapper.toLoteEntity(detalleRequest);
            inventario.addLote(lote);
            inventario.recalcularStock();
            invRepo.saveAndFlush(inventario);
            Lote lotePersistido = inventario.getLotes().stream()
                .filter(l -> l.getCodLote()
                .equals(detalleRequest.getCodLote()))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Error al persistir lote"));
            
            movRepo.save(crearMovimiento(TipoMovimiento.ENTRADA,inventario.getSku(),request.getCodSucursal(), detalleRequest.getCantidad(), runUsuario, lotePersistido));

            log.info("Lote {} registrado para sku {}", detalleRequest.getCodLote(), detalleRequest.getSku());

            response.add(mapper.toLoteResponse(lotePersistido));
        }
        return response;
    }

    // Recibe peticion de VENTA para rebajar stock.
    public void procesarVenta(VentaRequest request){
        log.info("Procesando venta de sku: {}, en sucursal: {}, cantidad: {}",
            request.getSku(), request.getCodSucursal(), request.getCantidad());

        validarSucursal(request.getCodSucursal());

        validarProducto(request.getSku());

        Inventario inventario = invRepo.findBySkuAndCodSucursal(request.getSku(), request.getCodSucursal())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Inventario no encontrado para sku: " + request.getSku()));

        if (inventario.getStockTotal() < request.getCantidad()){
            log.warn("Stock insuficiente para sku: {}, disponible: {}, solicitado: {}",
            request.getSku(), inventario.getStockTotal(), request.getCantidad());
            throw new BusinessException("Stock insuficiente para sku: " + request.getSku() 
            + ". Disponible: " + inventario.getStockTotal() 
            + ", Solicitado: " + request.getCantidad());
        }

        List<Lote> lotesDisponibles = loteRepo
        .findByInventarioAndEstadoAndCantidadGreaterThanOrderByFechaVencimientoAsc(
            inventario, EstadoLote.ACTIVO, 0);

        int cantidadPendiente = request.getCantidad();

        for (Lote lote : lotesDisponibles){
            if (cantidadPendiente <= 0) break;

            int descuento = Math.min(lote.getCantidad(), cantidadPendiente);
            lote.setCantidad(lote.getCantidad() - descuento);
            cantidadPendiente -= descuento;

            if (lote.getCantidad() == 0) lote.setEstado(EstadoLote.AGOTADO);

            Lote guardado = loteRepo.save(lote);

            movRepo.save(crearMovimiento(TipoMovimiento.SALIDA,
                 inventario.getSku(), request.getCodSucursal(), descuento, request.getRunVendedor(), guardado));

            log.info(("Lote {} descontado en {} unidades, restantes: {}"),
            lote.getCodLote(), descuento, lote.getCantidad());
        }

        if (cantidadPendiente > 0) {
            log.error("Inconsistencia crítica: El stock de los lotes no coincide con el total del inventario para SKU: {}", request.getSku());
            throw new BusinessException("No se pudo procesar la venta por inconsistencia en existencias.");
        }
        
        inventario.recalcularStock();
        invRepo.save(inventario);

        log.info("Venta procesada para sku: {}, cantidad: {}, stock restante: {}",
        request.getSku(), request.getCantidad(), inventario.getStockTotal());
    }
        

    // ===== METODOS PUT =====
    // cambia el ESTADO de un LOTE
    public void cambiarEstadoLote(String sku, String codSucursal, String codLote, EstadoLote nuevoEstado){
        log.info("Cambiando estado lote {} de sku {} en sucursal {}", codLote, sku, codSucursal);

        List<Lote> lotes = loteRepo.findByCodLoteAndInventario_SkuAndInventario_CodSucursal(codLote, sku, codSucursal);
        if (lotes.isEmpty()){
            throw new ResourceNotFoundException("Lote no encontrado: "+codLote+" para sku: "+sku+" en sucursal: "+codSucursal);
        } 
        lotes.forEach(lote -> {
            lote.setEstado(nuevoEstado);
            loteRepo.save(lote);
            lote.getInventario().recalcularStock();
            invRepo.save(lote.getInventario());
        });
        log.info("{} lotes actualizados a estado {}", lotes.size(), nuevoEstado);
    }

    // METODOS DELETE 
    // ELIMINA un INVENTARIO solo si su STOCK TOTAL es 0.
    public void eliminarInventario(String sku, String codSucursal){
        log.info("Eliminando inventario sku: {} en sucursal: {}", sku, codSucursal);

        Inventario inventario = invRepo.findBySkuAndCodSucursal(sku, codSucursal)
            .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado para sku:"+ sku +" en sucursal: "+ codSucursal));
        if (inventario.getStockTotal() > 0){
            throw new BusinessException("No se puede eliminar un inventario con stock activo: " + inventario.getStockTotal() + " unidades.");
        }
        invRepo.delete(inventario);
        log.info("Inventario sku: {} en sucursal: {} eliminado", sku, codSucursal);
    }

    // ==== PRIVADOS ====
    // CREA un MOVIMIENTO 
    private Movimiento crearMovimiento(TipoMovimiento tipo,String sku, String codSucursal, Integer cantidad, String runUsuario, Lote lote ){
        Movimiento movimiento = new Movimiento();
        movimiento.setTipo(tipo);
        movimiento.setSku(sku);
        movimiento.setCodSucursal(codSucursal);
        movimiento.setCodLote(lote.getCodLote());
        movimiento.setCantidad(cantidad);
        movimiento.setRunUsuario(runUsuario);
        movimiento.setLote(lote);
        return movimiento;
    }

    private void validarProducto(String sku) {
        try {
            productoClient.buscarPorSku(sku);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Producto no encontrado con sku: " + sku);
        } catch (FeignException e) {
            throw new ServiceCommunicationException("Error al comunicarse con el servicio de productos.");
        } catch (Exception e) {
            throw new ServiceCommunicationException("Error inesperado al conectar con productos.");
        }
    }

    private void validarSucursal(String codSucursal) {
        try {
            sucursalClient.buscarSucursal(codSucursal);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Sucursal no encontrada con codigo sucursal: " + codSucursal);
        } catch (FeignException e) {
            throw new ServiceCommunicationException("Error al comunicarse con el servicio de sucursal.");
        } catch (Exception e) {
            throw new ServiceCommunicationException("Error inesperado al conectar con sucursal.");
        }
    }
}
