package com.pharmanet.ubicacion_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmanet.ubicacion_service.entity.Comuna;

@Repository
public interface ComunaRepository extends JpaRepository<Comuna, Integer> {

    Optional<Comuna> findByCodComuna(Integer comu);

    Optional<Comuna> findByDescripcion(String desc);

    Optional<Comuna> findByCodComunaAndRegion_CodRegion(Integer comu, String region);

}
