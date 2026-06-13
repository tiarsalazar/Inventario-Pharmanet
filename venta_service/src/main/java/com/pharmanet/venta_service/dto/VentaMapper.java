package com.pharmanet.venta_service.dto;

import java.time.LocalDate;

import com.pharmanet.venta_service.entity.Venta;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VentaMapper {

    public static VentaDto toDto(Venta entidad) {
        log.info("Inicia conversión de entidad a dto");
        log.debug("entidad: {}", entidad);

        return new VentaDto(entidad.getCodVenta(),
            entidad.getCodSucursal(),
            entidad.getRun(),
            entidad.getFechaVenta()    
        );
    }

    public static Venta toModel(VentaDto dto) {
        log.info("Inicia conversión de dto a entidad");
        log.debug("dto: {}", dto);

        return new Venta(dto.getCodVenta(),
            dto.getCodSucursal(),
            dto.getRun(),
            (dto.getFechaVenta() == null) ? LocalDate.now() : dto.getFechaVenta()
        );
    }

    public static Venta update(Venta actual, VentaDto nueva) {
        log.info("Inicia actualización de venta en el mapper");
        log.debug("actual: {}, nueva: {}", actual, nueva);

        actual.setCodVenta(nueva.getCodVenta());
        actual.setCodSucursal(nueva.getCodSucursal());
        actual.setRun(nueva.getRun());
        actual.setFechaVenta(nueva.getFechaVenta());

        return actual;
    }
}
