package com.pharmanet.inventario_service.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pharmanet.inventario_service.entity.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    Optional<Inventario> findBySkuAndCodSucursal(String sku, String codSucursal);
    Page<Inventario> findByCodSucursal(String codSucursal, Pageable pageable); 
}
