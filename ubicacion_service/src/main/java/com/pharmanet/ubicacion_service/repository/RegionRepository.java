package com.pharmanet.ubicacion_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pharmanet.ubicacion_service.entity.Region;

public interface RegionRepository extends JpaRepository<Region, Integer> {

    Optional<Region> findByCodRegion(String codRegion);

    Optional<Region> findByDescripcion(String desc);
}
