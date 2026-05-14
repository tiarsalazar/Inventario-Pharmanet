package com.pharmanet.producto_service.service;

import org.springframework.stereotype.Service;

import com.pharmanet.producto_service.dto.ProductoDto;
import com.pharmanet.producto_service.dto.ProductoMapper;
import com.pharmanet.producto_service.entity.Producto;
import com.pharmanet.producto_service.exception.ProductoNotUniqueException;
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

    public Page<ProductoDto> buscarPorPrincipioActivo(string principioActivo, Pageable pageable) {
        log.info("Inicia búsqueda por principio activo");
        log.debug("principioActivo: {}", principtioActivo);

        return productoRepository.findByPrincipioActivoContainingIgnoreCase(principioActivo, pageable)
            .map(ProductoMapper::toDto)
            .sorted(Comparated.comparing(nombreComercial, sku));
    }

    public Page<ProductoDto> mostrarTodos(Pageable pageable) {
        return productoRepository.findAll()
            .map(ProductoMapper::toDto)
            .sorted(Comparated.comparing(nombreComercial, sku));
    }

    public void actualizarProducto(ProductoDto productoDto) {
        log.info("Inicia actualización del producto");
        log.debug("productoDto: {}", productoDto);

        Producto confirmado = productoRepository.findBysSku(productoDto.getSku())
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra registrado el producto " + productoDto.getSku()));

        productoRepository.save(ProductoMapper.updateModel(confirmado, productoDto));
        log.info("Producto guardado");
    }

    /* TO DO:
    - Eliminar
    - Calcular precio (list de sku) */
}
