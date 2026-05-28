package com.pharmanet.sucursal_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pharmanet.sucursal_service.dto.SucursalDto;
import com.pharmanet.sucursal_service.dto.SucursalMapper;
import com.pharmanet.sucursal_service.entity.Sucursal;
import com.pharmanet.sucursal_service.entity.TipoSucursal;
import com.pharmanet.sucursal_service.exception.NotUniqueSucursalException;
import com.pharmanet.sucursal_service.exception.ResourceNotFoundException;
import com.pharmanet.sucursal_service.repository.SucursalRepository;

import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SucursalService {

    private final SucursalRepository sucursalRepository;

    public SucursalDto agregarSucursal(SucursalDto dto) {
        log.info("Inicia agregado de sucursal.");
        log.debug("sucursalDto: {}" + dto);

        log.info("Búsqueda de posibles duplicados.");
        if(sucursalRepository.findById(dto.getId()).isPresent()) {
            throw new NotUniqueSucursalException("Ya se encuentra ingresada la sucursal: " + dto.getId());
        }

        log.info("Verifica que la dirección ingresada sea válida.");
        log.debug("regionId: {}, comunaId: {}", dto.getRegionId(), dto.getComunaId());

        try {

        } catch (FeignException ex) {
            throw ex;
        }

        Sucursal sucursal = SucursalMapper.toEntity(dto);

        log.debug("sucursal: {}", sucursal);
        sucursalRepository.save(sucursal);
        
        return SucursalMapper.toDto(sucursal);
    }

    public Page<SucursalDto> mostrarTodasLasSucursales(Pageable pageable) {
        return sucursalRepository.findAll(pageable)
            .map(SucursalMapper::toDto);
    }
    
    public SucursalDto buscarSucursalPorId(Long id) {
        log.info("Inicia busqueda de sucursal por ID.");
        log.debug("id: {}", id);

        Sucursal sucursal = sucursalRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la sucursal con el ID: " + id));
        log.debug("sucursal: {}", sucursal);

        return SucursalMapper.toDto(sucursal);
    }

    public Page<SucursalDto> buscarPorRegion(Integer codRegion, Pageable pageable) {
        log.info("Inicia búsqueda de sucursal por región.");
        log.debug("codRegion: {}", codRegion);
        return sucursalRepository.findByCodRegion(codRegion, pageable)
            .map(SucursalMapper::toDto);
    }

    public Page<SucursalDto> buscarPorTipoSucursal(TipoSucursal tipoSucursal, Pageable pageable) {
        log.info("Inicia búsqueda de sucursal por tipo sucursal.");
        log.debug("tipoSucursal: {}", tipoSucursal);
        return sucursalRepository.findByTipoSucursal(tipoSucursal, pageable)
            .map(SucursalMapper::toDto);
    }

    public void actualizarSucursal(SucursalDto dto) {
        log.info("Inicia actualización de la sucursal.");
        log.debug("sucursalDto: {}", dto);

        log.info("Inicia búsqueda de la sucursal");
        Sucursal sucursal = sucursalRepository.findById(dto.getId())
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la sucursal con el ID: " + dto.getId()));

        sucursal = SucursalMapper.update(sucursal, dto);
        sucursalRepository.save(sucursal);
    }

    public void eliminarSucursal(Long id) {
        log.info("Inicia eliminación de la sucursal.");
        log.debug("id: {}", id);

        log.info("Inicia búsqueda de la sucursal por ID.");
        Sucursal sucursal = sucursalRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la sucursal con el ID: " + id));

        sucursalRepository.delete(sucursal);
        log.debug("sucursal eliminada: {}", sucursal);
    }
}