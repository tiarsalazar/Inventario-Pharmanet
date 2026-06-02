package com.pharmanet.ubicacion_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pharmanet.ubicacion_service.dto.RegionDto;
import com.pharmanet.ubicacion_service.dto.RegionMapper;
import com.pharmanet.ubicacion_service.entity.Region;
import com.pharmanet.ubicacion_service.exception.ResourceAlreadyExistsException;
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

        log.info("Validación de que región no exista");
        if (regionRepository.findByCodRegion(dto.getCodRegion()).isPresent()) {
            throw new ResourceAlreadyExistsException("La region con el codigo " + dto.getCodRegion() + " ya está registrada.");
        }

        Region region = RegionMapper.toEntity(dto);
        return RegionMapper.toDto(regionRepository.save(region));
    }

    public RegionDto buscarRegion(String codRegion) {
        log.info("Inicia búsqueda de región por código de región.");
        log.debug("codRegion: {}", codRegion);

        log.info("Valida que la región exista.");
        Region region = regionRepository.findByCodRegion(codRegion)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la región con el código: " + codRegion));

        return RegionMapper.toDto(region);
    }
    public Page<RegionDto> mostrarTodasRegiones(Pageable pageable) {
        return regionRepository.findAll(pageable)
                .map(RegionMapper::toDto);
    }

    public void actualizarRegion(RegionDto dto) {
        log.info("Inicia actualización de región");
        log.debug("dto: {}", dto);

        log.info("Valida que la región exista.");
        Region region = regionRepository.findByCodRegion(dto.getCodRegion())
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la región con el código: " + dto.getCodRegion()));

        region = RegionMapper.update(region, dto);

        regionRepository.save(region);
    }

    public void eliminarRegion(String codRegion) {
        log.info("Inicia eliminación de la región");
        log.debug("codRegion: {}", codRegion);

        Region region = regionRepository.findByCodRegion(codRegion)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la región con el código: " + codRegion));

        regionRepository.delete(region);
    }
}
