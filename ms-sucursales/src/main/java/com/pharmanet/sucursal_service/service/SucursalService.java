package com.pharmanet.sucursal_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pharmanet.sucursal_service.dto.SucursalDTO;
import com.pharmanet.sucursal_service.dto.SucursalMapper;
import com.pharmanet.sucursal_service.entity.Sucursal;
import com.pharmanet.sucursal_service.exception.ResourceNotFoundException;
import com.pharmanet.sucursal_service.repository.SucursalRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SucursalService {

    private final SucursalRepository sucursalRepository;

    public SucursalDTO agregarSucursal(SucursalDTO sucursalDTO) {

        Sucursal sucursal = SucursalMapper.toModel(sucursalDTO);
        sucursalRepository.save(sucursal);
        
        return SucursalMapper.toDTO(sucursal);
    }

    public SucursalDTO buscarSucursal(String codInterno) {

        Sucursal sucursal = sucursalRepository.findByCodInterno(codInterno)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la sucursal"));

        return SucursalMapper.toDTO(sucursal);
    }

    public List<SucursalDTO> buscarPorRegion(String region) {
        
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

        // Verifica la existencia de la sucursal
        Sucursal verificarSucursal = sucursalRepository.findByCodInterno(sucursalDTO.getCodInterno())
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la sucursal"));

        // Convierte la sucursalDTO en sucursal
        Sucursal sucursal = SucursalMapper.toModel(sucursalDTO);
        sucursal.setId(verificarSucursal.getId());
        sucursal.setEstado(verificarSucursal.getEstado());

        // Guarda la entidad en la BD
        sucursalRepository.save(sucursal);
    }

    public void eliminarSucursal(String codInterno) {
        
        if(codInterno == null || codInterno.isEmpty() || codInterno.length() > 10) {
            throw new ResourceNotFoundException("No se encuentra la sucursal");
        }

        Sucursal sucursal = sucursalRepository.findByCodInterno(codInterno)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la sucursal"));

        sucursalRepository.delete(sucursal);
    }
}
