package com.pharmanet.venta_service.dto;

import com.pharmanet.venta_service.entity.Venta;

public class VentaMapper {

    public static VentaDto toDto(Venta venta) {
        VentaDto ventaDto = new VentaDto(venta.getId(),
            venta.getCodProd(),
            venta.getCodInventario(),
            venta.getRunVendedor(),
            venta.getCantidad(),
            venta.getFechaVenta()    
        );

        return ventaDto;
    }

    public static Venta toModel(VentaDto ventaDto) {
        Venta venta = new Venta(ventaDto.getId(),
            ventaDto.getCodProd(),
            ventaDto.getCodInventario(),
            ventaDto.getRunEmpleado(),
            ventaDto.getCantidad(),
            ventaDto.getFechaVenta()
        );

        return venta;
    }
}
