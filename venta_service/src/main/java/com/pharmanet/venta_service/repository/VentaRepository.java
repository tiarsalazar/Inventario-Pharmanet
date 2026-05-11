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

    @Query("SELECT v FROM Venta WHERE v.fechaVenta = :dia AND v.estadoPago = 'APROBADO'")
    Page<Venta> findByDia(LocalDate dia, Pageable pageable);

    @Query("SELECT v.codProd, COUNT(*) FROM Venta WHERE v.estadoPago = 'APROBADO' GROUP BY v.codProd")
    Page<Venta> countAllByCodProd(Pageable pageable);

    @Query("SELECT v.codInterno, COUNT(*) FROM venta WHERE v.codProd = :codProd AND v.estadoPago = 'APROBADO' GROUP BY v.codInterno")
    Page<Venta> countByCodProdGroupByCodInterno(String codProd, Pageable pageable);
}
