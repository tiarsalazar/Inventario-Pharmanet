package com.pharmanet.inventario_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmanet.inventario_service.entity.Lote;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Long> {
    List<Lote> findByCodLoteAndInventario_SkuAndInventario_CodSucursal(String codLote, String sku, String codSucursal);
    List<Lote> findByInventarioId(Long id);
}
