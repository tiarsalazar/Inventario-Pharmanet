package com.pharmanet.localidad_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pharmanet.localidad_service.entity.Region;
import com.pharmanet.localidad_service.exception.DuplicatedRegionException;
import com.pharmanet.localidad_service.exception.ResourceNotFoundException;
import com.pharmanet.localidad_service.repository.RegionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class RegionService {

    private final RegionRepository regionRepository;

    public Region agregarRegion(Region region) {
        log.info("Inicia agregado de región.");
        log.debug("region: {}", region);

        if (regionRepository.existsById(region.getRegionId())) {
            throw new DuplicatedRegionException("La region con el id " + region.getRegionId() + " ya se encuentra registrada.");
        }

        return regionRepository.save(region);
    }

    public Page<Region> mostrarTodasRegiones(Pageable pageable) {
        return regionRepository.findAll(pageable);
    }

    public void actualizarRegion(Region region) {
        log.info("Inicia actualización de región");
        log.debug("region: {}", region);

        if (!regionRepository.existsById(region.getRegionId())) {
            throw new ResourceNotFoundException("No se encuentra la sucursal con el ID: " + region.getRegionId());
        }

        regionRepository.save(region);
    }

    public void eliminarRegion(Integer id) {
        log.info("Inicia eliminación de la región");
        log.debug()
    }
}
