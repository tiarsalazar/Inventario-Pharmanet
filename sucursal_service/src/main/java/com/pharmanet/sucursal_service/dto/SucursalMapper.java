package com.pharmanet.sucursal_service.dto;

import com.pharmanet.sucursal_service.entity.Sucursal;
import com.pharmanet.sucursal_service.exception.ResourceNotFoundException;

@Slf4j
public class SucursalMapper {

    public static SucursalDto toDTO(Sucursal entity) {

        log.info("Inicia cambio de entidad sucursal a objeto dto");
        log.debug("entity: {}", entity);

        return new SucursalDto(entity.getCodSucursal(),
            entity.getNombreSucursal(),
            entity.getTipoSucursal(),
            entity.getRegion(),
            entity.getComuna(),
            entity.getDireccion()
        );
    }

    public static Sucursal toEntity(SucursalDto dto) {

        log.info("Inicia cambio de objeto dto a entidad sucursal");
        log.debug("dto: {}", dto);
        
        return new Sucursal(dto.getCodSucursal(),
            dto.getNombreSucursal(),
            dto.getTipoSucursal(),
            dto.getRegion(),
            dto.getComuna(),
            dto.getDireccion()
        );
    }

    public static Sucursal update(Sucursal actual, SucursalDto nueva) {
        log.info("Inicia actualización de sucursal");
        log.debug("actual: {}, nueva: {}", actual, nueva);

        actual.setNombreSucursal(nueva.getNombreSucursal());
        actual.setRegion(nueva.getRegion());
        actual.setComuna(nueva.getComuna());
        actual.getDireccion(nueva.getDireccion());

        return actual;
    }
}