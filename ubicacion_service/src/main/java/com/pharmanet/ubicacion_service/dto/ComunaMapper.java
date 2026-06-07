package com.pharmanet.ubicacion_service.dto;

import com.pharmanet.ubicacion_service.entity.Comuna;
import com.pharmanet.ubicacion_service.entity.Region;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ComunaMapper {

    public static ComunaDto toDto(Comuna entidad) {
        log.info("Inicia conversión a de entidad a objeto dto");
        log.debug("entidad: {}", entidad);

        ComunaDto dto = new ComunaDto();
        dto.setCodComuna(entidad.getCodComuna());
        dto.setDescripcion(entidad.getDescripcion());
        dto.setRegion(entidad.getRegion().getCodRegion());

        return dto;
    }

    public static Comuna toEntity(ComunaDto dto, Region region) {
        log.info("Inicia conversión de objeto dto a entidad");
        log.debug("dto: {}", dto);

        Comuna entidad = new Comuna();
        entidad.setCodComuna(dto.getCodComuna());
        entidad.setDescripcion(dto.getDescripcion());
        entidad.setRegion(region);

        return entidad;
    }

    public static Comuna update(Comuna actual, ComunaDto nueva, Region region) {
        log.info("Inicia actualización de comuna actual por datos nuevos");
        log.debug("actual: {}, nueva: {}", actual, nueva);

        actual.setCodComuna(nueva.getCodComuna());
        actual.setDescripcion(nueva.getDescripcion());
        actual.setRegion(region);

        return actual;
    }
}
