package com.pharmanet.inventario_service.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmanet.inventario_service.client.ProductoClient;
import com.pharmanet.inventario_service.client.SucursalClient;
import com.pharmanet.inventario_service.dto.inventario.InventarioDetailResponse;
import com.pharmanet.inventario_service.dto.inventario.InventarioResponse;
import com.pharmanet.inventario_service.dto.lote.LoteResponse;
import com.pharmanet.inventario_service.dto.movimiento.MovimientoResponse;
import com.pharmanet.inventario_service.dto.recepcion.DetalleRecepcionRequest;
import com.pharmanet.inventario_service.dto.recepcion.RecepcionRequest;
import com.pharmanet.inventario_service.dto.venta.DetalleVentaRequest;
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
    public Page<MovimientoResponse> obtenerMovimientoPorFecha(String codSucursal, LocalDate inicio, LocalDate fin, Pageable pageable){
        log.info("Obteniendo movimientos entre: {} y {}", inicio, fin);
        if (inicio.isAfter(fin)) {
            throw new BusinessException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        if (fin.isAfter(LocalDate.now())) {
            throw new BusinessException("No se pueden consultar movimientos con fechas futuras.");
        }
        return movRepo.findByCodSucursalAndFechaBetween(codSucursal, inicio.atStartOfDay(), fin.atTime(LocalTime.MAX), pageable)
            .map(mapper::toMovimientoResponse);
    }

    @Transactional(readOnly = true)
    public Page<MovimientoResponse> obtenerMovimientoPorSucursal(String codSucursal, Pageable pageable){
        log.info("Obteniendo movimientos de sucursal: {}", codSucursal);
        return movRepo.findByCodSucursal(codSucursal, pageable)
            .map(mapper::toMovimientoResponse);
    }


    // ==== PETICIONES POST ====
    // Recibe peticion de ABASTECIMIENTO para INGRESAR una recepcion.
    public List<LoteResponse> registrarRecepcion(RecepcionRequest request, String runUsuario){
        log.info("Registrando recepcion para sucursal: {}", request.getCodSucursal());

        log.info("Validando sucursal {} por feign",request.getCodSucursal());
        validarSucursal(request.getCodSucursal());
       
        List<LoteResponse> response = new ArrayList<>();
        for (DetalleRecepcionRequest detalleRequest : request.getDetalles()){

            log.info("Validando producto {} por feign",detalleRequest.getSku());
            validarProducto(detalleRequest.getSku());

            Inventario inventario = obtenerOCrearInventario(detalleRequest.getSku(), request.getCodSucursal());

            Lote lote = mapper.toLoteEntity(detalleRequest);
            inventario.addLote(lote);
            inventario.recalcularStock();
            invRepo.save(inventario);
            Lote lotePersistido = inventario.getLotes().stream()
                .filter(l -> l.getCodLote().equals(detalleRequest.getCodLote()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Error al persistir, lote no encontrado"));

            movRepo.save(crearMovimiento(TipoMovimiento.ENTRADA, inventario.getSku(), request.getCodSucursal(),
                detalleRequest.getCantidad(), runUsuario, lotePersistido));

            log.info("Lote {} registrado para sku {}", detalleRequest.getCodLote(), detalleRequest.getSku());

            response.add(mapper.toLoteResponse(lote));
        }
        log.info("Recepcion finalizada con éxito. Total de lotes procesados para la sucursal {}: {}", 
                 request.getCodSucursal(), response.size());
        return response;
    }

    // Recibe peticion de VENTA para rebajar stock.
    public void procesarVenta(VentaRequest ventaRequest){
        log.info("Procesando venta en sucursal: {}, cantidad ítems: {}",
            ventaRequest.getCodSucursal(), ventaRequest.getProductos().size());

        log.info("Validando Sucursal {} por feign",ventaRequest.getCodSucursal());
        validarSucursal(ventaRequest.getCodSucursal());

        for (DetalleVentaRequest detalle : ventaRequest.getProductos()) {
            log.info("Procesando ítem - SKU: {}, Cantidad: {}", detalle.getSku(), detalle.getCantidad());

            log.info("Validando existencia producto {} por feign", detalle.getSku());
            validarProducto(detalle.getSku());

            Inventario inventario = obtenerInventario(detalle.getSku(), ventaRequest.getCodSucursal());
            validarStockDisponible(inventario, detalle.getCantidad());

            descontarStock(inventario, ventaRequest.getCodSucursal(), ventaRequest.getRun(), detalle);
        
            inventario.recalcularStock();
            invRepo.save(inventario);

            log.info("Venta procesada para sku: {}, cantidad: {}, stock restante: {}",
            detalle.getSku(), detalle.getCantidad(), inventario.getStockTotal());
        }
        log.info("Venta finalizada con éxito para la sucursal: {}, items procesados: {}",
        ventaRequest.getCodSucursal(), ventaRequest.getProductos().size());
    }

    // ===== METODOS PUT =====
    // cambia el ESTADO de un LOTE
    public void cambiarEstadoLote(String sku, String codSucursal, String codLote, EstadoLote nuevoEstado){
        log.info("Cambiando estado lote {} de sku {} en sucursal {}", codLote, sku, codSucursal);

        List<Lote> lotes = loteRepo.findByCodLoteAndInventario_SkuAndInventario_CodSucursal(codLote, sku, codSucursal);
        if (lotes.isEmpty()){
            throw new ResourceNotFoundException("Lote no encontrado: "+codLote+" para sku: "+sku+" en sucursal: "+codSucursal);
        }
        if (nuevoEstado == EstadoLote.ACTIVO) {
            boolean tieneLoteVacio = lotes.stream().anyMatch(lote -> lote.getCantidad() == 0);
            if (tieneLoteVacio) {
                log.warn("Intento de activar lotes con cantidad cero para el código: {}", codLote);
                throw new BusinessException("No se puede cambiar el estado a ACTIVO si alguno de los lotes con código " + codLote + " tiene cantidad cero (está agotado).");
            }
        }
        Inventario inventario = lotes.get(0). getInventario();
        lotes.forEach(lote -> lote.setEstado(nuevoEstado));
        inventario.recalcularStock();
        invRepo.save(inventario);
        log.info("{} lotes actualizados a estado {}", lotes.size(), nuevoEstado);
    }

    // METODOS DELETE 
    // ELIMINA un INVENTARIO solo si su STOCK TOTAL es 0.
    public void eliminarInventario(String sku, String codSucursal){
        log.info("Eliminando inventario sku: {} en sucursal: {}", sku, codSucursal);

        Inventario inventario = obtenerInventario(sku, codSucursal);
        if (inventario.getStockTotal() > 0){
            log.warn("Intento de eliminacion fallido. El inventario SKU: {} en sucursal: {} posee stock activo: {} ud.", 
                     sku, codSucursal, inventario.getStockTotal());
            throw new BusinessException("No se puede eliminar un inventario con stock activo: " + inventario.getStockTotal() + " unidades.");
        }
        invRepo.delete(inventario);
        log.info("Inventario sku: {} en sucursal: {} eliminado", sku, codSucursal);
    }

    // ==== METODOS PRIVADOS ====

    // Busca un inventario
    private Inventario obtenerInventario(String sku, String codSucursal){
         return invRepo.findBySkuAndCodSucursal(sku, codSucursal)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Inventario no encontrado para sku:"+ sku +" en sucursal: "+ codSucursal));
    }

    // Busca Inventario. Si no existe lo crea y persiste.
    private Inventario obtenerOCrearInventario(String sku, String codSucursal){
        return invRepo.findBySkuAndCodSucursal(sku, codSucursal)
                .orElseGet(() -> {
                Inventario nuevoInv = new Inventario();
                nuevoInv.setSku(sku);
                nuevoInv.setCodSucursal(codSucursal);
                nuevoInv.setStockTotal(0);
                return invRepo.save(nuevoInv);});
    }

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

    //Valida stock disponible vs stock solicitado
    private void validarStockDisponible(Inventario inventario, int cantidadSolicitada){
        if (inventario.getStockTotal() < cantidadSolicitada){
            log.warn("Stock insuficiente para sku: {}, disponible: {}, solicitado: {}",
            inventario.getSku(), inventario.getStockTotal(), cantidadSolicitada);
            throw new BusinessException("Stock insuficiente para sku: " + inventario.getSku() 
            + ". Disponible: " + inventario.getStockTotal() 
            + ", Solicitado: " + cantidadSolicitada);
        }
    }

    // Procesa el descuento de stock para venta
    private void descontarStock(Inventario inventario, String codSucursal, String runUsuario, DetalleVentaRequest request){
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

            movRepo.save(crearMovimiento(TipoMovimiento.SALIDA,
                 inventario.getSku(), codSucursal, descuento, runUsuario, lote));

            log.info("Lote {} descontado en {} unidades, restantes: {}",
            lote.getCodLote(), descuento, lote.getCantidad());
        }
        if (cantidadPendiente > 0) {
            log.error("Inconsistencia crítica: El stock de los lotes no coincide con el total del inventario para SKU: {}", request.getSku());
            throw new BusinessException("No se pudo procesar la venta por inconsistencia en existencias.");
        }
    }

    // Valida existencia de producto con FEIGN
    private void validarProducto(String sku) {
        try {
            productoClient.buscarPorSku(sku);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Producto no encontrado con sku: " + sku);
        } catch (FeignException e) {
            log.error("Error crítico de Feign al validar Producto SKU: {}. Status HTTP: {}", sku, e.status());
            throw new ServiceCommunicationException("Error al comunicarse con el servicio de productos.");
        }
    }

    // Valida existencia de Sucursal con FEIGN
    private void validarSucursal(String codSucursal) {
        try {
            sucursalClient.buscarSucursal(codSucursal);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Sucursal no encontrada con codigo sucursal: " + codSucursal);
        } catch (FeignException e) {
            log.error("Error crítico de Feign al validar Sucursal Código: {}. Status HTTP: {}", codSucursal, e.status());
            throw new ServiceCommunicationException("Error al comunicarse con el servicio de sucursal.");
        }
    }
}
