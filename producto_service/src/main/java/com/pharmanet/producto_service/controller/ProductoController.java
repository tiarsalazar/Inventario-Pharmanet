package com.pharmanet.producto_service.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Producto", description = "Operaciones relacionadas con los productos")
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    @Operation(summary = "Devuelve todas los productos", description = "Devuelve un pageable de todos los productos")
    @ApiResponse(responseCode = "200", description = "Devuelve un pageable de todas los productos correctamente")
    public ResponseEntity<Page<ProductoDto>> mostrarTodos(@PageableDefault (size = 10, sort = "nombreComercial") @ParameterObject Pageable pageable) {
        log.info("Se inicia búsqueda de todos los productos");
        Page<ProductoDto> productos = productoService.mostrarTodos(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(productos);
    }

    @GetMapping("/{sku}")
    @Operation(summary = "Busca un producto", description = "Busca un producto por su sku")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Busca un producto exitosamente"),
        @ApiResponse(responseCode = "404", description = "No encuentra el producto")
    })
    public ResponseEntity<ProductoDto> buscarPorSku(@PathVariable String sku) {
        ProductoDto productoDto = productoService.buscarPorSku(sku);

        return ResponseEntity.status(HttpStatus.OK).body(productoDto);
    }

    @GetMapping("/principio-activo")
    @Operation(summary = "Busca productos por principio activo", description = "Devuelve un pageable con todos los productos por principio activo")
    @ApiResponse(responseCode = "200", description = "Retorna un pageable exitosamente")
    public ResponseEntity<Page<ProductoDto>> buscarPorPrincipioActivo(@RequestParam String activo,
        @PageableDefault(size = 10, sort = {"nombreComercial", "precioVenta"}) @ParameterObject Pageable pageable) {
        Page<ProductoDto> prodPorActivo = productoService.buscarPorPrincipioActivo(activo, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(prodPorActivo);
    }

    @GetMapping("/precio-venta")
    @Operation(summary = "Busca productos por precio", description = "Devuelve un pageable con productos entre un precio mínimo y un precio máximo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Devuelve un pageable exitosamente"),
        @ApiResponse(responseCode = "400", description = "Fallo en las validaciones")
    })
    public ResponseEntity<Page<ProductoDto>> buscarPorPrecioVenta(
        @RequestParam @Min(value = 0, message = "El valor no puede ser negativo") int min,
        @RequestParam @Min(value = 0, message = "El valor no puede ser negativo") int max,
        @PageableDefault(size = 10, sort = {"nombreComercial", "precioVenta"}) @ParameterObject Pageable pageable) {

        Page<ProductoDto> prodPorPrecioVenta = productoService.buscarPorPrecioVenta(min, max, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(prodPorPrecioVenta);
    }

    @PostMapping("/recetas")
    @Operation(summary = "Obtiene la receta de mayor jerarquía", description = "Obtiene la receta de mayor jerarquía de una lista de productos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Devuelve la respuesta exitosamente"),
        @ApiResponse(responseCode = "404", description = "No encuentra uno de los producto")
    })
    public ResponseEntity<String> obtenerReceta(@RequestBody List<String> skus) {
        String recetaMayor = productoService.obtenerReceta(skus);
        return ResponseEntity.status(HttpStatus.OK).body(recetaMayor);
    }
    

    @PostMapping("/calcular-total")
    @Operation(summary = "Calcula el precio total", description = "Calcula el precio total de una venta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Devuelve el precio exitosamente"),
        @ApiResponse(responseCode = "404", description = "No encuentra uno de los producto")
    })
    public ResponseEntity<BigDecimal> calcularPrecioVentaTotal(@RequestBody Map<String, Integer> productos) {
        BigDecimal totalVenta = productoService.calcularPrecioVentaTotal(productos);
        return ResponseEntity.status(HttpStatus.OK).body(totalVenta);
    }

    @PostMapping
    @Operation(summary = "Agrega un producto", description = "Agrega un producto nuevo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Agrega un producto exitosamente"),
        @ApiResponse(responseCode = "409", description = "Ya existe un producto con ese código"),
        @ApiResponse(responseCode = "400", description = "Fallo en las validaciones")
    })
    public ResponseEntity<ProductoDto> agregarProducto(@Valid @RequestBody ProductoDto productoDto) {
        ProductoDto guardado = productoService.agregarProducto(productoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping
    @Operation(summary = "Actualiza un producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Actualiza un producto exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encuentra producto"),
        @ApiResponse(responseCode = "400", description = "Fallo en las validaciones")
    })
    public ResponseEntity<?> actualizarProducto(@Valid @RequestBody ProductoDto productoDto) {
        productoService.actualizarProducto(productoDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{sku}")
    @Operation(summary = "Elimina un producto", description = "Elimina un producto por su código")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Elimina producto exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encuentra producto")
    })
    public ResponseEntity<?> eliminarProducto(@PathVariable String sku) {
        productoService.eliminarProducto(sku);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}