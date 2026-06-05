package com.pharmanet.sucursal_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pharmanet.sucursal_service.client.UbicacionFeignClient;
import com.pharmanet.sucursal_service.dto.SucursalDTO;
import com.pharmanet.sucursal_service.dto.SucursalMapper;
import com.pharmanet.sucursal_service.entity.Sucursal;
import com.pharmanet.sucursal_service.exception.ResourceAlreadyExistsException;
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

    private final UbicacionFeignClient ubicacionFeignClient;

    public SucursalDTO agregarSucursal(SucursalDTO dto) {
        log.info("Inicia agregado de sucursal.");
        log.debug("dto: {}" + dto);

        log.info("Válida que el código de la sucursal sea único.");
        if(sucursalRepository.findByCodSucursal(dto.getCodSucursal()).isPresent()) {
            throw new ResourceAlreadyExistsException("Ya se encuentra ingresada la sucursal con el código: " + dto.getCodSucursal());
        }

        if (!dto.getCodRegion().startsWith("SU")) {
            if (dto.getCodRegion().length() >= 7) {
                throw new IllegalArgumentException("El código de región debe ingresar con 'SU', de lo contrario ingrese un código menor o igual a 6 carácteres");
            }
            String codigo = dto.getCodRegion();
            dto.setCodRegion("SU" + codigo);
        }

        log.info("Válida que el nombre de la sucursal sea único.");
        if(sucursalRepository.findByNombreSucursal(dto.getNombreSucursal()).isPresent()) {
            throw new ResourceAlreadyExistsException("Ya se encuentra ingresada la sucursal con el nombre: " + dto.getNombreSucursal());
        }

        dto.setCodRegion(dto.getCodRegion().toUpperCase());

        log.info("Verifica que la ubicación ingresada sea válida.");
        log.debug("comuna: {}, region: {}", dto.getCodComuna(), dto.getCodRegion());

        try {
            ubicacionFeignClient.validarUbicacion(dto.getCodComuna(), dto.getCodRegion());
        } catch (FeignException ex) {
            throw ex;
        }

        Sucursal sucursal = SucursalMapper.toEntity(dto);

        log.debug("sucursal: {}", sucursal);
        sucursalRepository.save(sucursal);
        
        return SucursalMapper.toDto(sucursal);
    }

    public Page<SucursalDTO> mostrarTodasLasSucursales(Pageable pageable) {
        return sucursalRepository.findAll(pageable)
            .map(SucursalMapper::toDto);
    }
    
    public SucursalDTO buscarSucursalPorCodSucursal(String codSucursal) {
        log.info("Inicia busqueda de sucursal por código.");
        log.debug("codSucursal: {}", codSucursal);

        Sucursal sucursal = sucursalRepository.findByCodSucursal(codSucursal)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la sucursal con el código: " + codSucursal));
        log.debug("sucursal: {}", sucursal);

        return SucursalMapper.toDto(sucursal);
    }

    public Page<SucursalDTO> buscarPorRegion(String codRegion, Pageable pageable) {
        log.info("Inicia búsqueda de sucursal por región.");
        log.debug("codRegion: {}", codRegion);
        return sucursalRepository.findByCodRegion(codRegion, pageable)
            .map(SucursalMapper::toDto);
    }

    public void actualizarSucursal(SucursalDTO dto) {
        log.info("Inicia actualización de la sucursal.");
        log.debug("dto: {}", dto);

        log.info("Inicia búsqueda de la sucursal");
        Sucursal sucursal = sucursalRepository.findByCodSucursal(dto.getCodSucursal())
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la sucursal con el código: " + dto.getCodSucursal()));

        if (sucursalRepository.findByNombreSucursal(dto.getNombreSucursal()).isPresent()) {
            Sucursal verificar = sucursalRepository.findByNombreSucursal(dto.getNombreSucursal())
                .orElseThrow(() -> new InternalError());
            if (verificar.getCodSucursal() != dto.getNombreSucursal())
                throw new ResourceAlreadyExistsException("Ya existe una sucursal con el nombre ingresado");
        }

        sucursal = SucursalMapper.update(sucursal, dto);
        sucursalRepository.save(sucursal);
    }

    public void eliminarSucursal(String codSucursal) {
        log.info("Inicia eliminación de la sucursal.");
        log.debug("codSucursal: {}", codSucursal);

        log.info("Inicia búsqueda de la sucursal por código.");
        Sucursal sucursal = sucursalRepository.findByCodSucursal(codSucursal)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la sucursal con el código: " + codSucursal));

        sucursalRepository.delete(sucursal);
        log.debug("sucursal eliminada: {}", sucursal);
    }
}