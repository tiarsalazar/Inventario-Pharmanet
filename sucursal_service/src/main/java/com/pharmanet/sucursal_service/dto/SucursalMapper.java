package com.pharmanet.sucursal_service.dto;

import com.pharmanet.sucursal_service.entity.Sucursal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SucursalMapper {

    public static SucursalDTO toDto(Sucursal entity) {

        log.info("Inicia cambio de entidad sucursal a objeto dto");
        log.debug("entity: {}", entity);

        return new SucursalDTO(entity.getCodSucursal(),
            entity.getNombreSucursal(),
            entity.getCodRegion(),
            entity.getCodComuna(),
            entity.getDireccion()
        );
    }

    public static Sucursal toEntity(SucursalDTO dto) {

        log.info("Inicia cambio de objeto dto a entidad sucursal.");
        log.debug("dto: {}", dto);
        
        return new Sucursal(dto.getCodSucursal(),
            dto.getNombreSucursal(),
            dto.getCodRegion(),
            dto.getCodComuna(),
            dto.getDireccion()
        );
    }

    public static Sucursal update(Sucursal actual, SucursalDTO nueva) {
        log.info("Inicia actualización de sucursal.");
        log.debug("actual: {}, nueva: {}", actual, nueva);

        actual.setNombreSucursal(nueva.getNombreSucursal());
        actual.setCodRegion(nueva.getCodRegion());
        actual.setCodComuna(nueva.getCodComuna());
        actual.setDireccion(nueva.getDireccion());

        return actual;
    }
}