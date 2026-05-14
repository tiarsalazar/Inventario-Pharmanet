package com.pharmanet.producto_service.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmanet.producto_service.entity.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findBySku(String sku);
    
    Page<Producto> findByPrincipioActivoContainingIgnoreCase(String principioActivo, Pageable pageable);

    Page<Producto> findByPrecioVentaBetween(int minimo, int maximo, Pageable pageable);
}
