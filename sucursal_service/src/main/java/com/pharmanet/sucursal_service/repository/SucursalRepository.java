package com.pharmanet.sucursal_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmanet.sucursal_service.entity.Sucursal;


@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    Optional<Sucursal> findByCodInterno(String codInterno);

    List<Sucursal> findByRegionOrderByComunaAscCodInternoAsc(String region);
}