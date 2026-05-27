package com.pharmanet.sucursal_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pharmanet.sucursal_service.dto.SucursalDto;
import com.pharmanet.sucursal_service.dto.SucursalMapper;
import com.pharmanet.sucursal_service.entity.Sucursal;
import com.pharmanet.sucursal_service.exception.NotUniqueSucursalException;
import com.pharmanet.sucursal_service.exception.ResourceNotFoundException;
import com.pharmanet.sucursal_service.repository.SucursalRepository;

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
        log.info("Inicia guardado de sucursal.");
        log.debug("dto: {}" + dto);

        log.info("Búsqueda de posibles duplicados.");
        if(sucursalRepository.findById(dto.getId()).isPresent()) {
            throw new NotUniqueSucursalException("Ya se encuentra ingresada la sucursal: " + dto.getId());
        }

        log.info("Verifica que la dirección ingresada sea válida.");
        log.debug("region: {}, comuna: {}", dto.getRegion(), dto.getComuna());

        try {

        } catch (FeignException ex) {
            throw new FeignException(ex.contentUTF8());
        }

        Sucursal sucursal = SucursalMapper.toEntity(dto);

        log.debug("sucursal: {}", sucursal);
        sucursalRepository.save(sucursal);
        
        return SucursalMapper.toDto(sucursal);
    }

    public SucursalDTO buscarSucursal(String codSucursal) {
        log.info("Inicia busqueda de sucursal por código de la sucursal.");
        log.debug("codInterno: " + codInterno);

        Sucursal sucursal = sucursalRepository.findByCodInterno(codInterno)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la sucursal: " + codInterno));
        log.debug("sucursal: " + sucursal);

        return SucursalMapper.toDTO(sucursal);
    }

    public List<SucursalDTO> buscarPorRegion(String region) {
        log.info("Inicia búsqueda de sucursal por región.");
        log.debug("region: " + region);
        return sucursalRepository.findByRegionOrderByComunaAscCodInternoAsc(region).stream()
            .map(SucursalMapper::toDTO)
            .toList();
    }

    public List<SucursalDTO> mostrarTodasLasSucursales() {

        return sucursalRepository.findAll().stream()
            .map(SucursalMapper::toDTO)
            .toList();
    }

    public void actualizarSucursal(SucursalDTO sucursalDTO) {
        log.info("Inicia actualización de la sucursal.");
        log.debug("sucursalDTO: " + sucursalDTO);

        log.info("Verifica que la sucursal ya exista");
        Sucursal verificarSucursal = sucursalRepository.findByCodInterno(sucursalDTO.getCodInterno())
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la sucursal: " + sucursalDTO.getCodInterno()));

        log.info("Convierte el dto a model");
        Sucursal sucursal = SucursalMapper.toModel(sucursalDTO);
        log.debug("id: " +  verificarSucursal.getId());
        sucursal.setId(verificarSucursal.getId());
        log.debug("estado: " + verificarSucursal.getEstado());
        sucursal.setEstado(verificarSucursal.getEstado());

        log.debug("sucursal: " + sucursal);
        sucursalRepository.save(sucursal);
    }

    public void eliminarSucursal(String codInterno) {
        log.info("Inicia borrado de la sucursal.");
        log.debug("codInterno: " + codInterno);

        log.info("Verifica existencia de la sucursal");
        Sucursal sucursal = sucursalRepository.findByCodInterno(codInterno)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la sucursal: " + codInterno));

        sucursalRepository.delete(sucursal);
        log.debug("sucursal eliminada: " + sucursal);
    }
}