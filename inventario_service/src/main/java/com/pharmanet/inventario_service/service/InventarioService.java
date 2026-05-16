package com.pharmanet.inventario_service.service;

import java.time.LocalDateTime;
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
import com.pharmanet.inventario_service.dto.lote.LoteRequest;
import com.pharmanet.inventario_service.dto.lote.LoteResponse;
import com.pharmanet.inventario_service.dto.lote.RecepcionRequest;
import com.pharmanet.inventario_service.dto.movimiento.MovimientoResponse;
import com.pharmanet.inventario_service.entity.Inventario;
import com.pharmanet.inventario_service.entity.Lote;
import com.pharmanet.inventario_service.entity.Movimiento;
import com.pharmanet.inventario_service.enums.EstadoLote;
import com.pharmanet.inventario_service.enums.TipoMovimiento;
import com.pharmanet.inventario_service.exception.BusinessException;
import com.pharmanet.inventario_service.exception.ResourceNotFoundException;
import com.pharmanet.inventario_service.mapper.InventarioMapper;
import com.pharmanet.inventario_service.repository.InventarioRepository;
import com.pharmanet.inventario_service.repository.LoteRepository;
import com.pharmanet.inventario_service.repository.MovimientoRepository;

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

    public InventarioDetailResponse obtenerInventarioPorSku(String sku, String codSucursal){
        log.info("Obteniendo inventario por sku: {} en sucursal: {}", sku, codSucursal);
        return invRepo.findBySkuAndCodSucursal(sku, codSucursal)
        .map(mapper::toInventarioDetailResponse)
        .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado para sku: "+sku));
    }

    public Page<InventarioResponse> obtenerInventarioPorSucursal(String codSucursal, Pageable pageable){
        log.info("Obteniendo inventarios de sucursal: {}",codSucursal);
        return invRepo.findByCodSucursal(codSucursal, pageable)
            .map(mapper::toInventarioResponse);
    }

    // ==== CONSULTAS MOVIMIENTOS ====

    public Page<MovimientoResponse> obtenerMovimientoPorLote(String codLote, Pageable pageable){
        log.info("Obteniendo movimientos del lote: {}", codLote);
        return movRepo.findByLoteCodLote(codLote, pageable)
            .map(mapper::toMovimientoResponse);
    }

    public Page<MovimientoResponse> obtenerMovimientoPorUsuario(String rutUsuario, Pageable pageable){
        log.info("Obteniendo movimientos por usuario rut: {}", rutUsuario);
        return movRepo.findByRutUsuario(rutUsuario, pageable).map(mapper::toMovimientoResponse);
    }

    public Page<MovimientoResponse> obtenerMovimientosPorfecha(LocalDateTime inicio, LocalDateTime fin, Pageable pageable){
        log.info("Obteniendo movimientos entre: {} y {}", inicio, fin);
        return movRepo.findByFechaBetween(inicio, fin, pageable)
            .map(mapper::toMovimientoResponse);
    }

    public Page<MovimientoResponse> obtenerMovimientoPorSucursal(String codSucursal, Pageable pageable){
        log.info("Obteniendo movimientos de sucursal: {}", codSucursal);
        return movRepo.findByLoteInventarioCodSucursal(codSucursal, pageable)
            .map(mapper::toMovimientoResponse);
    }


    // ==== PETICIONES ====
    // Recibe peticion de ABASTECIMIENTO para INGRESAR una recepcion.
    public List<LoteResponse> registrarRecepcion(RecepcionRequest request, String rutUsuario){
        log.info("Registrando recepcion para sucursal: {}", request.getCodSucursal());

        // Validar que la sucursal existe
        try {
            sucursalClient.buscarSucursal(request.getCodSucursal());
        } catch (Exception e) {
            throw new ResourceNotFoundException("Sucursal no encontrada: " + request.getCodSucursal());
        }

        List<LoteResponse> response = new ArrayList<>();
        for (LoteRequest loteRequest : request.getLotes()){

            // Validar que el producto existe
            try {
                productoClient.buscarPorSku(loteRequest.getSku());
            } catch (Exception e) {
                throw new ResourceNotFoundException("Producto no encontrado con sku: " + loteRequest.getSku());
            }

            // Busca Inventario. Si no existe lo crea y persiste.
            Inventario inventario = invRepo.findBySkuAndCodSucursal(loteRequest.getSku(), request.getCodSucursal())
                .orElseGet(() -> {
                Inventario nuevo = new Inventario();
                nuevo.setSku(loteRequest.getSku());
                nuevo.setCodSucursal(request.getCodSucursal());
                nuevo.setStockTotal(0);
                return invRepo.save(nuevo);});

            Lote lote = mapper.toLoteEntity(loteRequest);
            inventario.addLote(lote);
            inventario.recalcularStock();
            invRepo.saveAndFlush(inventario);
            Lote lotePersistido = inventario.getLotes().stream()
                .filter(l -> l.getCodLote()
                .equals(loteRequest.getCodLote()))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Error al persistir lote"));
            
            movRepo.save(crearMovimiento(TipoMovimiento.ENTRADA,inventario.getSku(), loteRequest.getCantidad(), rutUsuario, lotePersistido));

            log.info("Lote {} registrado para sku {}", loteRequest.getCodLote(), loteRequest.getSku());

            response.add(mapper.toLoteResponse(lote));
        }
        return response;
    }

    // Recibe peticion de VENTA para rebajar stock.
    // SE DEBE CREAR METODO DE SALIDA DE STOCK VINCULADO A VENTA 
    // METODO DEBE RECIBIR CODIGO SUCURSAL, RUT USUARIO (HEADER), LISTA CON SKU Y CANTIDAD VENDIDA DE CADA PRODUCTO.
        

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
    private Movimiento crearMovimiento(TipoMovimiento tipo,String sku, Integer cantidad, String rutUsuario, Lote lote ){
        Movimiento movimiento = new Movimiento();
        movimiento.setTipo(tipo);
        movimiento.setSku(sku);
        movimiento.setCodLote(lote.getCodLote());
        movimiento.setCantidad(cantidad);
        movimiento.setRutUsuario(rutUsuario);
        movimiento.setLote(lote);
        return movimiento;
    }
}
