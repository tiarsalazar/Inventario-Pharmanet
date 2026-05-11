package com.pharmanet.venta_service.dto;

import com.pharmanet.venta_service.entity.Venta;

public class VentaMapper {

    public static VentaDto toDto(Venta venta) {
        VentaDto ventaDto = new VentaDto(venta.getId(),
            venta.getCodProd(),
            venta.getCodInterno(),
            venta.getCantidad(),
            venta.getRunVendedor(),
            venta.getFechaVenta()    
        );

        return ventaDto;
    }

    public static Venta toModel(VentaDto ventaDto) {
        Venta venta = new Venta(ventaDto.getId(),
            ventaDto.getProd(),
            ventaDto.getCodSucursal(),
            ventaDto.getCantidad(),
            ventaDto.getRunVendedor(),
            ventaDto.getFechaVenta()
        );
    }
}
