package com.pharmanet.inventario_service.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmanet.inventario_service.entity.Movimiento;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    Page<Movimiento> findByRutUsuarioAndCodSucursal(String rutUsuario, String codSucursal, Pageable pageable);
    Page<Movimiento> findByCodSucursalAndFechaBetween(String codSucursal, LocalDateTime inicio, LocalDateTime fin, Pageable pageable);
    Page<Movimiento> findByCodSucursal(String codSucursal, Pageable pageable);
    Page<Movimiento> findBySkuAndCodSucursal(String sku, String codSucursal, Pageable pageable);
}
