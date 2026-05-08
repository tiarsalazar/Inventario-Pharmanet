package com.pharmanet.venta_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.pharmanet.venta_service.entity.Venta;

@Service
public interface VentaService extends JpaRepository<Venta, Long> {

    List<Venta> findByBetweenFecha
}
