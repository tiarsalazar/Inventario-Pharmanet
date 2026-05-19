package com.pharmanet.producto_service.service;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pharmanet.producto_service.dto.ProductoDto;
import com.pharmanet.producto_service.dto.ProductoMapper;
import com.pharmanet.producto_service.entity.Producto;
import com.pharmanet.producto_service.exception.ProductoNotUniqueException;
import com.pharmanet.producto_service.exception.ResourceNotFoundException;
import com.pharmanet.producto_service.repository.ProductoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProductoService {

    final private ProductoRepository productoRepository;

    public ProductoDto agregarProducto(ProductoDto productoDto) {
        log.info("Inicia guardado de producto");
        log.debug("productoDto: {}", productoDto);

        log.info("Valida que no se encuentre en el sistema");
        if(productoRepository.findBySku(productoDto.getSku()).isPresent()) {
            log.warn("El producto {} ya se encuentra registrado en el sistema.", productoDto.getSku());
            throw new ProductoNotUniqueException("El producto " + productoDto.getSku() + " ya se encuentra registrado en el sistema");
        }

        Producto producto = ProductoMapper.toModel(productoDto);

        log.info("Guarda producto");
        log.debug("producto: {}", producto);
        productoRepository.save(producto);

        return ProductoMapper.toDto(producto);
    }

    public ProductoDto buscarPorSku(String sku) {
        log.info("Busca producto por código sku");
        log.debug("sku: {}", sku);

        return productoRepository.findBySku(sku)
            .map(ProductoMapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra el producto: " + sku));
    }

    public Page<ProductoDto> buscarPorPrincipioActivo(String principioActivo, Pageable pageable) {
        log.info("Inicia búsqueda por principio activo");
        log.debug("principioActivo: {}", principioActivo);

        return productoRepository.findByPrincipioActivoContainingIgnoreCase(principioActivo, pageable)
            .map(ProductoMapper::toDto);
    }

    public Page<ProductoDto> buscarPorPrecioVenta(int min, int max, Pageable pageable) {
        log.info("Inicia búsqueda de productos por precio de venta");
        log.debug("min: {} max: {}", min, max);

        if (min > max) {
            log.warn("El valor ingresado como mínimo supera al valor ingresado como máximo");
            throw new IllegalArgumentException("El valor ingresado como mínimo no puede ser superior al valor ingresado como máximo");
        }

        return productoRepository.findByPrecioVentaBetween(min, max, pageable)
            .map(ProductoMapper::toDto);
    }

    public Page<ProductoDto> mostrarTodos(Pageable pageable) {
        return productoRepository.findAll(pageable)
            .map(ProductoMapper::toDto);
    }

    public void actualizarProducto(ProductoDto productoDto) {
        log.info("Inicia actualización del producto");
        log.debug("productoDto: {}", productoDto);

        Producto confirmado = productoRepository.findBySku(productoDto.getSku())
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra registrado el producto " + productoDto.getSku()));

        productoRepository.save(ProductoMapper.updateModel(confirmado, productoDto));
        log.info("Producto guardado");
    }

    public void eliminarProducto(String sku) {
        log.info("Inicia eliminación del producto");
        log.debug("sku: {}", sku);

        Producto producto = productoRepository.findBySku(sku)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra el producto con el sku: " + sku));
        
        productoRepository.delete(producto);
    }

    public BigDecimal calcularPrecioTotalVenta(String sku, int cantidad) {
        log.info("Se inicia procedimiento para calular precio de venta total");
        log.debug("sku: {} cantidad: {}", sku, cantidad);

        Producto producto = productoRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("No se encuentra el producto con el sku: " + sku));

        return producto.getPrecioVenta().multiply(new BigDecimal(cantidad));
    }
}
