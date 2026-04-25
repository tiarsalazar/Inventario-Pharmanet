package com.pharmanet.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pharmanet.dto.SucursalDTO;
import com.pharmanet.dto.SucursalMapper;
import com.pharmanet.entity.Sucursal;
import com.pharmanet.exception.ResourceNotFoundException;
import com.pharmanet.repository.SucursalRepository;

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
        
        if(codInterno == null || codInterno.isEmpty() || codInterno.length() > 10) {
            throw new ResourceNotFoundException("No se encuentra la sucursal");
        }
        Sucursal sucursal = sucursalRepository.findByCodInterno(codInterno)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la sucursal"));

        return SucursalMapper.toDTO(sucursal);
    }

    public List<SucursalDTO> buscarPorRegion(String region) {
        
        List<SucursalDTO> sucursalesEnRegion = sucursalRepository.findByRegionOrderByComunaAscCodInternoAsc(region).stream()
            .map(x -> SucursalMapper.toDTO(x))
            .toList();

        if (listaPorRegion.isEmpty()) {
            throw new ListEmptyException("No hay sucursales disponibles en la región");
        }

        return sucursalesEnRegion;
    }

    public List<SucursalDTO> mostrarTodasLasSucursales() {

        List<SucursalDTO> sucursales = sucursalRepository.findAll().stream()
            .map(x -> SucursalMapper.toDTO(x))
            .sorted(Comparator.comparing(SucursalDTO::getCodInterno))
            .toList();
        
        if (sucursales.isEmpty()) {
            throw new ListEmptyException("No hay sucursales disponibles");
        }

        return sucursales;
    }

    public SucursalDTO actualizarSucursal(SucursalDTO sucursalDTO) {

        Sucursal sucursal = sucursalRepository.findByCodInterno(sucursalDTO.getCodInterno())
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la sucursal"));

        sucursal = SucursalMapper.toModel(sucursalDTO);
        sucursalRepository.save(sucursal);
        
        return SucursalMapper.toDTO(sucursal);
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
