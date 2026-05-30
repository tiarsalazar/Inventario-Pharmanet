package com.pharmanet.ubicacion_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pharmanet.ubicacion_service.entity.Comuna;
import com.pharmanet.ubicacion_service.exception.DuplicatedResourceException;
import com.pharmanet.ubicacion_service.exception.ResourceNotFoundException;
import com.pharmanet.ubicacion_service.repository.ComunaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class ComunaService {

    private final ComunaRepository comunaRepository;

    public Comuna agregarComuna(Comuna comuna) {
        log.info("Inicia agregado de comuna.");
        log.debug("comuna: {}", comuna);

        if (comunaRepository.existsById(comuna.getComunaId())) {
            throw new DuplicatedResourceException("La comuna con el id " + comuna.getComunaId() + " ya se encuentra registrada.");
        }

        return comunaRepository.save(comuna);
    }

    public Comuna buscarComuna(Integer id) {
        log.info("Inicia búsqueda de la comuna por ID");
        log.debug("id: {}", id);

        Comuna comuna = comunaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la comuna con el ID: " + id));

        return comuna;
    }

    public Page<Comuna> mostrarTodasComunas(Pageable pageable) {
        return comunaRepository.findAll(pageable);
    }

    public void actualizarComuna(Comuna comuna) {
        log.info("Inicia actualización de comuna");
        log.debug("comuna: {}", comuna);

        if (!comunaRepository.existsById(comuna.getComunaId())) {
            throw new ResourceNotFoundException("No se encuentra la comuna con el ID: " + comuna.getComunaId());
        }

        comunaRepository.save(comuna);
    }

    public void eliminarComuna(Integer id) {
        log.info("Inicia eliminación de la comuna");
        log.debug("id: {}", id);

        Comuna comuna = comunaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la comuna con el ID: " + id));

        comunaRepository.delete(comuna);
    }

    public Comuna validarComuna(Integer comuna, Integer region) {
        log.info("Inicia búsqueda de comuna");
        log.debug("comunaId: {}, region: {}", comuna, region);

        return comunaRepository.findByComunaIdAndRegion_RegionId(comuna, region)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la comuna con el id: " + comuna + " y la región: " + region));
    }
}
