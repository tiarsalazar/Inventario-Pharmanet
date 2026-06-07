package com.pharmanet.sucursal_service.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmanet.sucursal_service.entity.Sucursal;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    Optional<Sucursal> findByNombreSucursal(String nombre);

    Optional<Sucursal> findByCodSucursal(String codSucursal);
    Page<Sucursal> findByCodRegion(String codRegion, Pageable pageable);

}