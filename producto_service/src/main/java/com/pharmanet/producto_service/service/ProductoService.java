package com.pharmanet.producto_service.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pharmanet.producto_service.dto.ProductoDto;
import com.pharmanet.producto_service.dto.ProductoMapper;
import com.pharmanet.producto_service.entity.ClaseReceta;
import com.pharmanet.producto_service.entity.Producto;
import com.pharmanet.producto_service.exception.ResourceAlreadyExistsException;
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

    // ===================================================
    // MÉTODOS INTERNOS
    // ===================================================

    public ProductoDto agregarProducto(ProductoDto dto) {
        log.info("Inicia guardado de producto");
        log.debug("dto: {}", dto);

        dto.setSku(procesarSku(dto.getSku()));

        log.info("Valida que no se encuentre en el sistema");
        if(productoRepository.findBySku(dto.getSku()).isPresent()) {
            log.warn("El producto {} ya se encuentra registrado en el sistema.", dto.getSku());
            throw new ResourceAlreadyExistsException("Ya existe un producto con el sku: " + dto.getSku());
        }

        Producto entidad = ProductoMapper.toModel(dto);

        log.info("Guarda producto");
        log.debug("entidad: {}", entidad);
        productoRepository.save(entidad);

        return ProductoMapper.toDto(entidad);
    }

    public ProductoDto buscarPorSku(String sku) {
        log.info("Busca producto por código sku");
        log.debug("sku: {}", sku);

        return productoRepository.findBySku(sku)
            .map(ProductoMapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra el producto con el sku: " + sku));
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
            throw new IllegalArgumentException("El mínimo excede al máximo");
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
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra el producto con el sku: " + productoDto.getSku()));

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

    public String procesarSku(String sku) {
        log.info("Inicia el proceso del sku del producto");
        log.debug("sku: {}", sku);

        sku = sku.toUpperCase();

        if (!sku.startsWith("PR")) {
            if (sku.length() > 8) {
                throw new IllegalArgumentException("Sku inválido. Ingrese con un sku menor o igual a 8 carácteres o que empiece 'PR'");
            }
            sku = "PR" + sku;
        }

        return sku;
    }

    // ===================================================
    // MÉTODOS EXTERNOS
    // ===================================================
    
    public String obtenerReceta(List<String> skus) {
        log.info("Inicia búsqueda de tipo de receta de mayor jerarquía");
        log.debug("skus: {}", skus);

        List<Producto> productos = skus.stream()
            .map(p -> productoRepository.findBySku(p)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra el producto con el sku: " + p)))
            .toList();

        String receta = "SIN_RECETA";

        for (Producto p : productos) {
            if (p.getReceta().equals(ClaseReceta.RECETA_PRESENTADA)) {
                receta = "RECETA_PRESENTADA";
            } else if (p.getReceta().equals(ClaseReceta.RECETA_RETENIDA)) {
                receta = "RECETA_RETENIDA";
                break;
            }
        }
        
        return receta;
    }

    public BigDecimal calcularPrecioVentaTotal(Map<String, Integer> productos) {
        log.info("Se inicia procedimiento para calular precio de venta total");
        log.debug("productos: {}", productos);

        BigDecimal total = new BigDecimal(0);

        // Obtiene el precio de la venta de cada producto y lo multiplica por la cantidad
        for (Map.Entry<String, Integer> p : productos.entrySet()) {
            total = total.add(productoRepository.findBySku(p.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No se encuentra el producto con el sku: " + p.getKey()))
                .getPrecioVenta().multiply(new BigDecimal(p.getValue())));
        }

        return total;
    }


}
