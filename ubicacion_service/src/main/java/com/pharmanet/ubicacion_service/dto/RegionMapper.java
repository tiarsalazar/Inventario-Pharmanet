package com.pharmanet.ubicacion_service.dto;

import com.pharmanet.ubicacion_service.entity.Region;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class RegionMapper {

    public static RegionDto toDto(Region entidad) {
        log.info("Inicia conversión de entidad a objeto dto.");
        log.debug("entidad: {}", entidad);
        
        RegionDto dto = new RegionDto();
        dto.setCodRegion(entidad.getCodRegion());
        dto.setDescripcion(entidad.getDescripcion());

        return dto;
    }

    public static Region toEntity(RegionDto dto) {
        log.info("Inicia conversión de objeto dto a región.");
        log.debug("dto: {}", dto);

        Region entidad = new Region();
        entidad.setCodRegion(dto.getCodRegion());
        entidad.setDescripcion(dto.getDescripcion());

        return entidad;
    }

    public static Region update(Region actual, RegionDto nueva) {
        log.info("Inicia actualización de región actual por la región nueva.");
        log.debug("actual: {}, nueva: {}", actual, nueva);

        actual.setCodRegion(nueva.getCodRegion());
        actual.setDescripcion(nueva.getDescripcion());

        return actual;
    }
}
