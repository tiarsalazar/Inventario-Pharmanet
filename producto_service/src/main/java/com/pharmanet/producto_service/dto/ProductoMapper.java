package com.pharmanet.producto_service.dto;

import com.pharmanet.producto_service.entity.Producto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductoMapper {

    public static ProductoDto toDto(Producto producto) {
        log.info(("Convierte producto model a producto dto"));
        log.debug("producto: {}", producto);

        ProductoDto productoDto = new ProductoDto(producto.getSku(),
            producto.getNombreComercial(),
            producto.getPrincipioActivo(),
            producto.getPrecioVenta(),
            producto.getReceta(),
            producto.getConcentracion()
        );

        return productoDto;
    }

    public static Producto toModel(ProductoDto productoDto) {
        log.info(("Convierte producto dto a producto model"));
        log.debug("productoDto: {}", productoDto);

        Producto producto = new Producto(productoDto.getSku(),
            productoDto.getNombreComercial(),
            productoDto.getPrincipioActivo(),
            productoDto.getPrecioVenta(),
            productoDto.getReceta(),
            productoDto.getConcentracion()
        );

        return producto;
    }

    public static Producto updateModel(Producto producto, ProductoDto dto) {
        producto.setNombreComercial(dto.getNombreComercial());
        producto.setPrincipioActivo(dto.getPrincipioActivo());
        producto.setPrecioVenta(dto.getPrecioVenta());
        producto.setReceta(dto.getReceta());
        producto.setConcentracion(dto.getConcentracion());

        return producto;
    }
}
