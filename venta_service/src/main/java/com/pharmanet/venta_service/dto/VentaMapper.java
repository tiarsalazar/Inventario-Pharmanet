package com.pharmanet.venta_service.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.pharmanet.venta_service.entity.DetalleVenta;
import com.pharmanet.venta_service.entity.Venta;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VentaMapper {

    public static RegistroVenta toRegistroVenta(Venta venta, List<DetalleVenta> lista) {
        log.info("Inicia creación de nueva solicitud de venta");
        log.debug("venta: {}, detalleVentas: {}", venta, lista);

        RegistroVenta dto = new RegistroVenta();
        dto.setCodVenta(venta.getCodVenta());
        dto.setCodSucursal(venta.getCodSucursal());
        dto.setRun(venta.getRun());

        Map<String, Integer> productos = lista.stream()
            .collect(Collectors.toMap(DetalleVenta::getSku, DetalleVenta::getCantidad));
        dto.setProductos(productos);

        dto.setFechaVenta(venta.getFechaVenta());

        dto.setMontoTotal(venta.getMontoTotal());

        return dto;
    }

    public static VentaDto toDto(Venta entidad) {
        log.info("Inicia conversión de dto a entidad");
        log.debug("entidad: {}", entidad);

        return new VentaDto(entidad.getCodVenta(),
            entidad.getCodSucursal(),
            entidad.getRun(),
            entidad.getFechaVenta(),
            entidad.getMontoTotal()
        );
    }

    public static Venta toModel(VentaDto dto) {
        log.info("Inicia conversión de dto a entidad");
        log.debug("dto: {}", dto);

        return new Venta(dto.getCodVenta(),
            dto.getCodSucursal(),
            dto.getRun(),
            (dto.getFechaVenta() == null) ? LocalDate.now() : dto.getFechaVenta(),
            dto.getMontoTotal()
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
