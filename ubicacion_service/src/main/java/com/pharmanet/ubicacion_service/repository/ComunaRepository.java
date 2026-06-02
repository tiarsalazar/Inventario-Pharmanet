package com.pharmanet.ubicacion_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmanet.ubicacion_service.entity.Comuna;

@Repository
public interface ComunaRepository extends JpaRepository<Comuna, Integer> {

    Optional<Comuna> findByCodComuna(Integer comu);

    Optional<Comuna> findByComunaIdAndRegion_RegionId(Integer comu, String region);
}
