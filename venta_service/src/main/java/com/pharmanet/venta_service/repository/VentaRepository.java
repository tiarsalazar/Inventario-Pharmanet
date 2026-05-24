package com.pharmanet.venta_service.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmanet.venta_service.entity.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    Optional<Venta> findByCodVenta(Long codVenta);
    
    Page<Venta> findByFechaVentaBetween(LocalDate inicio, LocalDate termino, Pageable pageable);

    Page<Venta> findByFechaVenta(LocalDate dia, Pageable pageable);

}
