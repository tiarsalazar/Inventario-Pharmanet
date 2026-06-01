package com.pharmanet.ubicacion_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmanet.ubicacion_service.dto.RegionDto;
import com.pharmanet.ubicacion_service.entity.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {

    Optional<RegionDto> findByCodRegion(String codRegion);
}
