package com.pharmanet.dto;

import com.pharmanet.entity.Sucursal;

public class SucursalMapper {

    public static SucursalDTO toDTO(Sucursal sucursal) {
        return new SucursalDTO(sucursal.getCodInterno(),
            sucursal.getNombreSucursal(),
            sucursal.getTipoSucursal(),
            sucursal.getRegion(),
            sucursal.getComuna(),
            sucursal.getDireccion()
        );
    }

    public static Sucursal toModel(SucursalDTO sucursalDTO) {
        return new Sucursal(sucursalDTO.getCodInterno(),
            sucursalDTO.getNombreSucursal(),
            sucursalDTO.getTipoSucursal(),
            sucursalDTO.getRegion(),
            sucursalDTO.getComuna(),
            sucursalDTO.getDireccion()
        );
    }
}
