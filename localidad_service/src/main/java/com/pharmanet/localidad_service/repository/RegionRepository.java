package com.pharmanet.localidad_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmanet.localidad_service.entity.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {

}
