package com.pharmanet.inventario_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pharmanet.inventario_service.entity.Inventario;
import com.pharmanet.inventario_service.entity.Lote;
import com.pharmanet.inventario_service.enums.EstadoLote;

public interface LoteRepository extends JpaRepository<Lote, Long> {
    List<Lote> findByCodLoteAndInventario_SkuAndInventario_CodSucursal(
        String codLote, String sku, String codSucursal);
    List<Lote> findByInventarioId(Long id);
    List<Lote> findByInventarioAndEstadoAndCantidadGreaterThanOrderByFechaVencimientoAsc(
        Inventario inventario, EstadoLote estado, Integer cantidad);
}
