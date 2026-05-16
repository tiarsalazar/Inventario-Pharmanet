package com.pharmanet.inventario_service.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmanet.inventario_service.entity.Movimiento;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    Page<Movimiento> findByLoteCodLote(String codLote, Pageable pageable);
    Page<Movimiento> findByRutUsuario(String rutUsuario, Pageable pageable);
    Page<Movimiento> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin, Pageable pageable);
    Page<Movimiento> findByLoteInventarioCodSucursal(String codSucursal, Pageable pageable);
}
