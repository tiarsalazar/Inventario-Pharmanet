package com.pharmanet.sucursal_service.dto;

import com.pharmanet.sucursal_service.entity.Sucursal;
import com.pharmanet.sucursal_service.exception.ResourceNotFoundException;

public class SucursalMapper {

    public static SucursalDTO toDTO(Sucursal sucursal) {

        if (sucursal == null) {
            throw new ResourceNotFoundException("La sucursal viene null");
        }

        return new SucursalDTO(sucursal.getCodInterno(),
            sucursal.getNombreSucursal(),
            sucursal.getTipoSucursal(),
            sucursal.getRegion(),
            sucursal.getComuna(),
            sucursal.getDireccion()
        );
    }

    public static Sucursal toModel(SucursalDTO sucursalDTO) {

        if (sucursalDTO == null) {
            throw new ResourceNotFoundException("La sucursalDTO viene null");
        }
        
        return new Sucursal(sucursalDTO.getCodInterno(),
            sucursalDTO.getNombreSucursal(),
            sucursalDTO.getTipoSucursal(),
            sucursalDTO.getRegion(),
            sucursalDTO.getComuna(),
            sucursalDTO.getDireccion()
        );
    }
}
