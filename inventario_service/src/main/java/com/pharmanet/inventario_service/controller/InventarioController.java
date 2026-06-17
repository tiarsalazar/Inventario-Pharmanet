package com.pharmanet.inventario_service.controller;

import java.time.LocalDate;
import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmanet.inventario_service.dto.inventario.InventarioDetailResponse;
import com.pharmanet.inventario_service.dto.inventario.InventarioResponse;
import com.pharmanet.inventario_service.dto.lote.LoteResponse;
import com.pharmanet.inventario_service.dto.movimiento.MovimientoResponse;
import com.pharmanet.inventario_service.dto.recepcion.RecepcionRequest;
import com.pharmanet.inventario_service.dto.venta.VentaRequest;
import com.pharmanet.inventario_service.enums.EstadoLote;
import com.pharmanet.inventario_service.service.InventarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "Inventarios", description = "Operaciones relacionadas a gestion de Inventarios")
@RestController
@RequestMapping("/api/v1/inventarios")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService invServ;

    // ===== PETICIONES GET =====
    // INVENTARIO 
    @Operation(summary = "Busca Inventario por sku", description = "Busca los datos de un Inventario por su sku y codigo de sucursal.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inventario encontrada correctamente."),
        @ApiResponse(responseCode = "404", description = "Inventario solicitada no encontrada."),
    })
    @GetMapping("/sucursales/{codSucursal}/productos/{sku}")
    public ResponseEntity<InventarioResponse> obtenerInventarioPorSku(
        @Parameter(description = "Código único de la sucursal", example = "SU0001") @PathVariable String codSucursal,
        @Parameter(description = "Código único del producto", example = "PR00001") @PathVariable String sku){
        return ResponseEntity.ok(invServ.obtenerInventarioPorSku(sku, codSucursal));
    }

    @Operation(summary = "Busca Inventario por sku con detalles", description = "Busca los datos de un Inventario y sus lotes por su sku y codigo de sucursal.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inventario encontrada correctamente."),
        @ApiResponse(responseCode = "404", description = "Inventario solicitada no encontrada."),
    })
    @GetMapping("/sucursales/{codSucursal}/productos/{sku}/detalles")
    public ResponseEntity<InventarioDetailResponse> obtenerInventarioDetailPorSku(
        @Parameter(description = "Código único de la sucursal", example = "SU0001") @PathVariable String codSucursal,
        @Parameter(description = "Código único del producto", example = "PR00001") @PathVariable String sku){
        return ResponseEntity.ok(invServ.obtenerInventarioDetailPorSku(sku, codSucursal));
    }

    @Operation(summary = "Listar Inventarios por sucursal paginado", description = "Obtiene todos los Inventarios asociadas a un código de sucursal.")
    @ApiResponse(responseCode = "200", description = "Listado paginado retornado exitosamente.")
    @GetMapping("/sucursales/{codSucursal}")
    public ResponseEntity<Page<InventarioResponse>> obtenerInventarioPorSucursal(
        @Parameter(description = "Código único de la sucursal", example = "SU0001") @PathVariable String codSucursal,
        @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(invServ.obtenerInventarioPorSucursal(codSucursal, pageable));
    }

    // MOVIMIENTOS

    @Operation(summary = "Listar Movimientos por sku y sucursal paginado", description = "Obtiene todos los Movimientos asociadas a un sku y código de sucursal.")
    @ApiResponse(responseCode = "200", description = "Listado paginado retornado exitosamente.")
    @GetMapping("/sucursales/{codSucursal}/productos/{sku}/movimientos")
    public ResponseEntity<Page<MovimientoResponse>> obtenerMovimientosPorSku(
            @Parameter(description = "Código único de la sucursal", example = "SU0001") @PathVariable String codSucursal,
            @Parameter(description = "Código único del producto", example = "PR00001") @PathVariable String sku,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(invServ.obtenerMovimientoPorSku(sku, codSucursal, pageable));
    }

    @Operation(summary = "Listar Movimientos por RUN y Sucursal paginado", description = "Obtiene todos los Movimientos asociadas a un RUN de usuario y código de sucursal.")
    @ApiResponse(responseCode = "200", description = "Listado paginado retornado exitosamente.")
    @GetMapping("/sucursales/{codSucursal}/usuarios/{runUsuario}/movimientos")
    public ResponseEntity<Page<MovimientoResponse>> obtenerMovimientosPorRutUsuario(
            @Parameter(description = "Código único de la sucursal", example = "SU0001") @PathVariable String codSucursal,
            @Parameter(description = "Código único de la sucursal", example = "11222333-4") @PathVariable String runUsuario,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(invServ.obtenerMovimientoPorUsuario(runUsuario, codSucursal, pageable));
    }

    @Operation(summary = "Listar Movimientos fecha y Sucursal paginado", description = "Obtiene todos los Movimientos entre un rango de fechas y código de sucursal.")
    @ApiResponse(responseCode = "200", description = "Listado paginado retornado exitosamente.")
    @GetMapping("/sucursales/{codSucursal}/movimientos/fecha")
    public ResponseEntity<Page<MovimientoResponse>> obtenerMovimientosPorFecha(
            @Parameter(description = "Código único de la sucursal", example = "SU0001") @PathVariable String codSucursal,
            @Parameter(description = "Fecha inicial del rango de búsqueda (yyyy-MM-dd)", example = "2026-06-01") @RequestParam LocalDate inicio,
            @Parameter(description = "Fecha final del rango de búsqueda (yyyy-MM-dd)", example = "2026-06-15") @RequestParam LocalDate fin,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(invServ.obtenerMovimientoPorFecha(
            codSucursal, inicio, fin, pageable));
    }

    @Operation(summary = "Listar Movimientos de Sucursal", description = "Obtiene todos los Movimientos de sucursal por codigo.")
    @ApiResponse(responseCode = "200", description = "Listado paginado retornado exitosamente.")
    @GetMapping("/sucursales/{codSucursal}/movimientos")
    public ResponseEntity<Page<MovimientoResponse>> obtenerMovimientosPorSucursal(
            @Parameter(description = "Código único de la sucursal", example = "SU0001") @PathVariable String codSucursal,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(invServ.obtenerMovimientoPorSucursal(codSucursal, pageable));
    }

    // ==== PETICIONES POST ====

    @Operation(summary = "Procesa ingreso de stock", description = "Procesa una recepcion sumando los lotes a cada inventario y guardando el movimiento.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Movimiento procesado y stock actualizado con éxito."),
        @ApiResponse(responseCode = "400", description = "Datos de entrada con errores de validación o fallas lógicas de negocio."),
    })
    @PostMapping("/recepciones")
    public ResponseEntity<List<LoteResponse>> registrarRecepcion(
            @RequestBody @Valid RecepcionRequest request,
            @Parameter(description = "RUN del usuario autenticado que realiza la recepción", example = "12345678-9", required = true)
            @RequestHeader("X-Run-Usuario") String runUsuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(invServ.registrarRecepcion(request, runUsuario));
    }

    @Operation(summary = "Procesa salida de stock", description = "Procesa una venta rebajando stock correspondiente a cada inventario y guardando el movimiento.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Movimiento procesado y stock actualizado con éxito."),
        @ApiResponse(responseCode = "400", description = "Datos de entrada con errores de validación o fallas lógicas de negocio."),
    })
    @PostMapping("/ventas")
    public ResponseEntity<Void> procesarVenta(@RequestBody @Valid VentaRequest request) {
        invServ.procesarVenta(request);
        return ResponseEntity.noContent().build();
    }

    // ==== PETICIONES PUT/PATCH ====

    @Operation(summary = "Cambiar estado lote", description = "Cambia el estado de un lote por SKU, codigo lote  y sucursal correspondientes.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Estado modificado correctamente, no retorna contenido."),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado.")
    })
    @PutMapping("/sucursales/{codSucursal}/productos/{sku}/lotes/{codLote}/estado")
    public ResponseEntity<Void> cambiarEstadoLote(
            @Parameter(description = "Código único de la sucursal", example = "SU0001") @PathVariable String codSucursal,
            @Parameter(description = "Código único del producto", example = "PR00001") @PathVariable String sku,
            @Parameter(description = "Código del lote", example = "LOT-PR00001-001") @PathVariable String codLote,
            @Parameter(description = "Nuevo estado del lote", example = "DEFECTUOSO") @RequestParam EstadoLote nuevoEstado) {
        invServ.cambiarEstadoLote(sku, codSucursal, codLote, nuevoEstado);
        return ResponseEntity.noContent().build();
    }

    // ==== PETICIONES DELETE ====

    @Operation(summary = "Eliminar físicamente una Inventario", description = "Remueve permanentemente el registro del Inventario de la base de datos.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Eliminado físicamente de manera correcta."),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado.")
    })
    @DeleteMapping("/sucursales/{codSucursal}/productos/{sku}")
    public ResponseEntity<Void> eliminarInventario(
        @Parameter(description = "Código único de la sucursal", example = "SU0001") @PathVariable String codSucursal,
        @Parameter(description = "Código único del producto", example = "PR00001") @PathVariable String sku){
        invServ.eliminarInventario(sku, codSucursal);
        return ResponseEntity.noContent().build();
    }
}
