package com.pharmanet.venta_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmanet.venta_service.dto.RegistroVenta;
import com.pharmanet.venta_service.dto.VentaDto;
import com.pharmanet.venta_service.service.VentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/ventas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Ventas", description = "Operaciones relacionadas con las ventas.")
public class VentaController {

    private final VentaService ventaService;

    @GetMapping
    @Operation(summary = "Obtiene todas las ventas", description = "Obtiene un pageable de todas las ventas")
    @ApiResponse(responseCode = "200", description = "Obtiene un pageable de todas las ventas")
    public ResponseEntity<Page<VentaDto>> mostrarTodos(@PageableDefault(size = 10, sort = {"fechaVenta", "codVenta"}) @ParameterObject Pageable pageable) {
        log.info("Inicia búsqueda de todas las ventas");
        Page<VentaDto> ventas = ventaService.mostrarTodos(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ventas);
    }
    

    @GetMapping("/{codVenta}")
    @Operation(summary = "Busca una venta", description = "Busca una venta por su código")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Obtiene una venta que incluye el detalle de la compra"),
    @ApiResponse(responseCode = "404", description = "No encuentra la venta")
    })
    public ResponseEntity<RegistroVenta> buscarPorCodVenta(@PathVariable Long codVenta) {
        RegistroVenta venta = ventaService.buscarPorCodVenta(codVenta);
        return ResponseEntity.status(HttpStatus.OK).body(venta);
    }

    @GetMapping("/entre-fechas")
    @Operation(summary = "Busca ventas entre dos fechas", description = "Obtiene un pageable de las ventas entre la fecha de inicio y de término")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Obtiene un pageable de las ventas entre dos fechas"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    })
    public ResponseEntity<Page<VentaDto>> buscarPorFechas(
        @Valid @RequestParam LocalDate inicio,
        @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate termino,
        @PageableDefault (size = 10, sort = {"fechaVenta", "codVenta"}) Pageable pageable) {
        Page<VentaDto> resultado = ventaService.buscarPorFechas(inicio, termino, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resultado);
    }
    
    @GetMapping("/dia/{dia}")
    @Operation(summary = "Busca ventas entre dos fechas", description = "Obtiene un pageable de las ventas entre la fecha de inicio y de término")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Obtiene un pageable de las ventas entre dos fechas"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    })
    public ResponseEntity<Page<VentaDto>> buscarPorDia(@PathVariable @DateTimeFormat (iso = ISO.DATE) LocalDate dia,
        @PageableDefault(size = 10, sort= "codVenta") Pageable pageable) {
        Page<VentaDto> ventasPorDia = ventaService.buscarPorDia(dia, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ventasPorDia);
    }

    @PostMapping
    @Operation(summary = "Agregar una venta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Agrega una venta exitosamente"),
        @ApiResponse(responseCode = "400", description = "Fallo en las validaciones"),
        @ApiResponse(responseCode = "409", description = "Ya existe una venta con ese código")
    })
    public ResponseEntity<RegistroVenta> agregarVenta(@Valid @RequestBody RegistroVenta venta) {
        RegistroVenta resultado = ventaService.agregarVenta(venta);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @Operation(summary = "Actualiza una venta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Actualiza una venta exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encuentra la venta a actualizar"),
        @ApiResponse(responseCode = "400", description = "Fallo en las validaciones")
    })
    @PutMapping
    public ResponseEntity<?> actualizarVenta(@Valid @RequestBody VentaDto venta) {
        ventaService.actualizarVenta(venta);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Elimina una venta", description = "Elimina una venta por código ingresado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Se elimina una venta exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encuentra la venta")
    })
    @DeleteMapping("/{codVenta}")
    public ResponseEntity<?> eliminarVenta(@Parameter(name = "codVenta", description = "Código de venta") @PathVariable Long codVenta) {
        ventaService.eliminarVenta(codVenta);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
