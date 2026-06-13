package com.pharmanet.venta_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pharmanet.venta_service.entity.DetalleVenta;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    List<DetalleVenta> findByVenta_CodVenta(Long codVenta);

}
