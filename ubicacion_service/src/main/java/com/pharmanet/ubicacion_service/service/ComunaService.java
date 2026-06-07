package com.pharmanet.ubicacion_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pharmanet.ubicacion_service.dto.ComunaDto;
import com.pharmanet.ubicacion_service.dto.ComunaMapper;
import com.pharmanet.ubicacion_service.entity.Comuna;
import com.pharmanet.ubicacion_service.entity.Region;
import com.pharmanet.ubicacion_service.exception.ResourceAlreadyExistsException;
import com.pharmanet.ubicacion_service.exception.ResourceNotFoundException;
import com.pharmanet.ubicacion_service.repository.ComunaRepository;
import com.pharmanet.ubicacion_service.repository.RegionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class ComunaService {

    private final ComunaRepository comunaRepository;
    private final RegionRepository regionRepository;

    public ComunaDto agregarComuna(ComunaDto comuna) {
        log.info("Inicia agregado de comuna.");
        log.debug("comuna: {}", comuna);

        if (comunaRepository.findByCodComuna(comuna.getCodComuna()).isPresent() || comunaRepository.findByDescripcion(comuna.getDescripcion()).isPresent()) {
            throw new ResourceAlreadyExistsException("La comuna ya se encuentra registrada.");
        }

        Region region = regionRepository.findByCodRegion(comuna.getRegion())
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la región con el código: " + comuna.getRegion()));

        Comuna comu = ComunaMapper.toEntity(comuna, region);

        return ComunaMapper.toDto(comunaRepository.save(comu));
    }

    public ComunaDto buscarComuna(Integer codComuna) {
        log.info("Inicia búsqueda de la comuna por código.");
        log.debug("codComuna: {}", codComuna);

        Comuna comuna = comunaRepository.findByCodComuna(codComuna)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la comuna con el código: " + codComuna));

        return ComunaMapper.toDto(comuna);
    }

    public Page<ComunaDto> mostrarTodasComunas(Pageable pageable) {
        return comunaRepository.findAll(pageable)
            .map(ComunaMapper::toDto);
    }

    public void actualizarComuna(ComunaDto dto) {
        log.info("Inicia actualización de comuna");
        log.debug("dto: {}", dto);

        Comuna comuna = comunaRepository.findByCodComuna(dto.getCodComuna())
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la comuna con el código " + dto.getCodComuna()));

        Region region = (dto.getRegion().isEmpty())
            ? comuna.getRegion()
            : regionRepository.findByCodRegion(dto.getRegion())
                .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la región con el código: " + dto.getRegion()));

        comuna = ComunaMapper.update(comuna, dto, region);

        comunaRepository.save(comuna);
    }

    public void eliminarComuna(Integer codComuna) {
        log.info("Inicia eliminación de la comuna");
        log.debug("codComuna: {}", codComuna);

        Comuna comuna = comunaRepository.findByCodComuna(codComuna)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la comuna con el código: " + codComuna));

        comunaRepository.delete(comuna);
    }

    public ComunaDto validarComuna(Integer comuna, String region) {
        log.info("Inicia búsqueda de comuna");
        log.debug("comunaId: {}, region: {}", comuna, region);

        return ComunaMapper.toDto(
            comunaRepository.findByComunaIdAndRegion_RegionId(comuna, region)
                .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la comuna con el id: " + comuna + " y la región: " + region)));
    }
}
