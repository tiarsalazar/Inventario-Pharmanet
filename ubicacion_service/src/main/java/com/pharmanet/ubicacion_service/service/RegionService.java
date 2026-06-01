package com.pharmanet.ubicacion_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pharmanet.ubicacion_service.dto.RegionDto;
import com.pharmanet.ubicacion_service.entity.Region;
import com.pharmanet.ubicacion_service.exception.DuplicatedResourceException;
import com.pharmanet.ubicacion_service.exception.ResourceNotFoundException;
import com.pharmanet.ubicacion_service.repository.RegionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class RegionService {

    private final RegionRepository regionRepository;

    public RegionDto agregarRegion(RegionDto dto) {
        log.info("Inicia agregado de región.");
        log.debug("dto: {}", dto);

        if (regionRepository.findByCodRegion(dto.getCodRegion()).isPresent()) {
            throw new DuplicatedResourceException("La region con el codigo " + dto.getCodRegion() + " ya está registrada.");
        }

        Region region = 
        return regionRepository.save(region);
    }

    public Region buscarRegion(Integer id) {
        log.info("Inicia búsqueda de región por ID");
        log.debug("id: {}", id);

        Region region = regionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la reguón con el ID: " + id));

        return region;
    }
    public Page<Region> mostrarTodasRegiones(Pageable pageable) {
        return regionRepository.findAll(pageable);
    }

    public void actualizarRegion(Region region) {
        log.info("Inicia actualización de región");
        log.debug("region: {}", region);

        if (!regionRepository.existsById(region.getRegionId())) {
            throw new ResourceNotFoundException("No se encuentra la región con el ID: " + region.getRegionId());
        }

        regionRepository.save(region);
    }

    public void eliminarRegion(Integer id) {
        log.info("Inicia eliminación de la región");
        log.debug("id: {}", id);

        Region region = regionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la región con el ID: " + id));

        regionRepository.delete(region);
    }
}
