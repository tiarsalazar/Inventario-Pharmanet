package com.pharmanet.producto_service.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pharmanet.producto_service.dto.ProductoDto;
import com.pharmanet.producto_service.service.ProductoService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
@Slf4j
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<Page<ProductoDto>> mostrarTodos(@PageableDefault (size = 10, sort = "nombreComercial") Pageable pageable) {
        log.info("Se inicia búsqueda de todos los productos");
        Page<ProductoDto> productos = productoService.mostrarTodos(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(productos);
    }

    @GetMapping("/{sku}")
    public ResponseEntity<ProductoDto> buscarPorSku(@PathVariable String sku) {
        ProductoDto productoDto = productoService.buscarPorSku(sku);

        return ResponseEntity.status(HttpStatus.OK).body(productoDto);
    }

    @GetMapping("/principio-activo")
    public ResponseEntity<Page<ProductoDto>> buscarPorPrincipioActivo(@RequestParam String activo,
        @PageableDefault(size = 10, sort = {"nombreComercial", "precioVenta"}) Pageable pageable) {
        Page<ProductoDto> prodPorActivo = productoService.buscarPorPrincipioActivo(activo, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(prodPorActivo);
    }

    @GetMapping("/precio-venta")
    public ResponseEntity<Page<ProductoDto>> buscarPorPrecioVenta(
        @RequestParam @Min(value = 0, message = "El valor no puede ser negativo") int min,
        @RequestParam @Min(value = 0, message = "El valor no puede ser negativo") int max,
        @PageableDefault(size = 10, sort = {"nombreComercial", "precioVenta"}) Pageable pageable) {

        Page<ProductoDto> prodPorPrecioVenta = productoService.buscarPorPrecioVenta(min, max, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(prodPorPrecioVenta);
    }

    @PostMapping("/recetas")
    public ResponseEntity<String> obtenerReceta(@RequestBody List<String> skus) {
        String recetaMayor = productoService.obtenerReceta(skus);
        return ResponseEntity.status(HttpStatus.OK).body(recetaMayor);
    }
    

    @PostMapping("/calcular-total")
    public ResponseEntity<BigDecimal> calcularPrecioVentaTotal(@RequestBody Map<String, Integer> productos) {
        BigDecimal totalVenta = productoService.calcularPrecioVentaTotal(productos);
        return ResponseEntity.status(HttpStatus.OK).body(totalVenta);
    }

    @PostMapping
    public ResponseEntity<ProductoDto> agregarProducto(@Valid @RequestBody ProductoDto productoDto) {
        ProductoDto guardado = productoService.agregarProducto(productoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping
    public ResponseEntity<?> actualizarProducto(@Valid @RequestBody ProductoDto productoDto) {
        productoService.actualizarProducto(productoDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{sku}")
    public ResponseEntity<?> eliminarProducto(@PathVariable String sku) {
        productoService.eliminarProducto(sku);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}