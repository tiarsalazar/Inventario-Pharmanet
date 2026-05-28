package com.pharmanet.sucursal_service.dto;

import com.pharmanet.sucursal_service.entity.Sucursal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SucursalMapper {

    public static SucursalDto toDto(Sucursal entity) {

        log.info("Inicia cambio de entidad sucursal a objeto dto");
        log.debug("entity: {}", entity);

        return new SucursalDto(entity.getId(),
            entity.getNombreSucursal(),
            entity.getTipoSucursal(),
            entity.getRegionId(),
            entity.getComunaId(),
            entity.getDireccion()
        );
    }

    public static Sucursal toEntity(SucursalDto dto) {

        log.info("Inicia cambio de objeto dto a entidad sucursal.");
        log.debug("dto: {}", dto);
        
        return new Sucursal(dto.getId(),
            dto.getNombreSucursal(),
            dto.getTipoSucursal(),
            dto.getRegionId(),
            dto.getComunaId(),
            dto.getDireccion()
        );
    }

    public static Sucursal update(Sucursal actual, SucursalDto nueva) {
        log.info("Inicia actualización de sucursal.");
        log.debug("actual: {}, nueva: {}", actual, nueva);

        actual.setNombreSucursal(nueva.getNombreSucursal());
        actual.setRegionId(nueva.getRegionId());
        actual.setComunaId(nueva.getComunaId());
        actual.setDireccion(nueva.getDireccion());

        return actual;
    }
}