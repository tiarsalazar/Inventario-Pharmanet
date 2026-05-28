package com.pharmanet.sucursal_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmanet.sucursal_service.entity.Sucursal;
import com.pharmanet.sucursal_service.entity.TipoSucursal;


@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    Page<Sucursal> findByCodRegion(Integer codRegion, Pageable pageable);
    Page<Sucursal> findByTipoSucursal(TipoSucursal tipoSucursal, Pageable pageable);
}