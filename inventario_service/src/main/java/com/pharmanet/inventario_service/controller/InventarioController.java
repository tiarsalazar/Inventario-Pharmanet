package com.pharmanet.inventario_service.controller;

import java.time.LocalDate;
import java.util.List;

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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/inventarios")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService invServ;

    // ===== PETICIONES GET =====
    // INVENTARIO 

    @GetMapping("/{codSucursal}/productos/{sku}")
    public ResponseEntity<InventarioResponse> obtenerInventarioPorSku(
        @PathVariable String codSucursal, @PathVariable String sku){
        return ResponseEntity.ok(invServ.obtenerInventarioPorSku(sku, codSucursal));
    }

    @GetMapping("/{codSucursal}/productos/{sku}/detalles")
    public ResponseEntity<InventarioDetailResponse> obtenerInventarioDetailPorSku(
        @PathVariable String codSucursal, @PathVariable String sku){
        return ResponseEntity.ok(invServ.obtenerInventarioDetailPorSku(sku, codSucursal));
    }

    @GetMapping("/{codSucursal}")
    public ResponseEntity<Page<InventarioResponse>> obtenerInventarioPorSucursal(
        @PathVariable String codSucursal, Pageable pageable) {
        return ResponseEntity.ok(invServ.obtenerInventarioPorSucursal(codSucursal, pageable));
    }

    // MOVIMIENTOS

    @GetMapping("/{codSucursal}/productos/{sku}/movimientos")
    public ResponseEntity<Page<MovimientoResponse>> obtenerMovimientosPorSku(
            @PathVariable String codSucursal, @PathVariable String sku, Pageable pageable) {
        return ResponseEntity.ok(invServ.obtenerMovimientoPorSku(sku, codSucursal, pageable));
    }

    @GetMapping("/{codSucursal}/usuarios/{runUsuario}/movimientos")
    public ResponseEntity<Page<MovimientoResponse>> obtenerMovimientosPorRutUsuario(
            @PathVariable String codSucursal, @PathVariable String runUsuario, Pageable pageable) {
        return ResponseEntity.ok(invServ.obtenerMovimientoPorUsuario(runUsuario, codSucursal, pageable));
    }

    @GetMapping("/{codSucursal}/movimientos/fecha")
    public ResponseEntity<Page<MovimientoResponse>> obtenerMovimientosPorFecha(
            @PathVariable String codSucursal,
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fin, Pageable pageable) {
        return ResponseEntity.ok(invServ.obtenerMovimientoPorFecha(
            codSucursal, inicio, fin, pageable));
    }

    @GetMapping("/{codSucursal}/movimientos")
    public ResponseEntity<Page<MovimientoResponse>> obtenerMovimientosPorSucursal(
            @PathVariable String codSucursal, Pageable pageable) {
        return ResponseEntity.ok(invServ.obtenerMovimientoPorSucursal(codSucursal, pageable));
    }

    // ==== PETICIONES POST ====

    @PostMapping("/recepciones")
    public ResponseEntity<List<LoteResponse>> registrarRecepcion(
            @RequestBody @Valid RecepcionRequest request,
            @RequestHeader("X-Run-Usuario") String runUsuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(invServ.registrarRecepcion(request, runUsuario));
    }

    @PostMapping("/ventas")
    public ResponseEntity<Void> procesarVenta(@RequestBody @Valid VentaRequest request) {
        invServ.procesarVenta(request);
        return ResponseEntity.noContent().build();
    }

    // ==== PETICIONES PUT/PATCH ====

    @PutMapping("/{codSucursal}/productos/{sku}/lotes/{codLote}/estado")
    public ResponseEntity<Void> cambiarEstadoLote(
            @PathVariable String codSucursal, @PathVariable String sku, @PathVariable String codLote,
            @RequestParam EstadoLote nuevoEstado) {
        invServ.cambiarEstadoLote(sku, codSucursal, codLote, nuevoEstado);
        return ResponseEntity.noContent().build();
    }

    // ==== PETICIONES DELETE ====

    @DeleteMapping("/{codSucursal}/productos/{sku}")
    public ResponseEntity<Void> eliminarInventario(@PathVariable String codSucursal, @PathVariable String sku){
        invServ.eliminarInventario(sku, codSucursal);
        return ResponseEntity.noContent().build();
    }
}
