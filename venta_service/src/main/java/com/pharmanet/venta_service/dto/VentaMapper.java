package com.pharmanet.venta_service.dto;

import com.pharmanet.venta_service.entity.Venta;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VentaMapper {

    public static VentaDto toDto(Venta venta) {
        log.info("Inicia conversión de venta a dto");
        log.debug("venta: {}", venta);
        VentaDto ventaDto = new VentaDto(venta.getCodVenta(),
            venta.getSku(),
            venta.getCodSucursal(),
            venta.getRunVendedor(),
            venta.getCantidad(),
            venta.getFechaVenta()    
        );

        return ventaDto;
    }

    public static Venta toModel(VentaDto ventaDto) {
        log.info("Inicia conversión de dto a entidad");
        log.debug("dto: {}", ventaDto);

        Venta venta = new Venta(ventaDto.getCodVenta(),
            ventaDto.getSku(),
            ventaDto.getCodSucursal(),
            ventaDto.getCantidad(),
            ventaDto.getRunVendedor(),
            ventaDto.getFechaVenta()
        );

        return venta;
    }
}
