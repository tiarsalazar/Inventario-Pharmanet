package com.pharmanet.inventario_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmanet.inventario_service.entity.Inventario;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    Optional<Inventario> findBySkyAndCodSucursal(String sku, String codSucursal);
}
