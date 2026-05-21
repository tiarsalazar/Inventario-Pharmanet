package com.pharmanet.venta_service.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pharmanet.venta_service.entity.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    Page<Venta> findByBetweenFechaVenta(LocalDate inicio, LocalDate termino, Pageable pageable);

    @Query("SELECT v FROM Venta WHERE v.fechaVenta = :dia")
    Page<Venta> findByDia(LocalDate dia, Pageable pageable);

    @Query("SELECT v.sku, COUNT(*) FROM Venta GROUP BY v.sku")
    Page<Venta> countAllBySku(Pageable pageable);

    @Query("SELECT v.codSucursal, COUNT(*) FROM venta WHERE v.sku = :sku GROUP BY v.codSucursal")
    Page<Venta> countBySkuGroupByCodSucursal(String sku, Pageable pageable);
}
