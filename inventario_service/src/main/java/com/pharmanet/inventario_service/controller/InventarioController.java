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
import com.pharmanet.inventario_service.dto.lote.RecepcionRequest;
import com.pharmanet.inventario_service.dto.movimiento.MovimientoResponse;
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

    @GetMapping("/productos/{sku}/sucursales/{codSucursal}")
    public ResponseEntity<InventarioDetailResponse> obtenerInventarioPorSku(
        @PathVariable String sku, @PathVariable String codSucursal){
        return ResponseEntity.ok(invServ.obtenerInventarioPorSku(sku, codSucursal));
    }

    @GetMapping("/sucursales/{codSucursal}")
    public ResponseEntity<Page<InventarioResponse>> obtenerInventarioPorSucursal(
        @PathVariable String codSucursal, Pageable pageable) {
        return ResponseEntity.ok(invServ.obtenerInventarioPorSucursal(codSucursal, pageable));
    }

    // MOVIMIENTOS

    @GetMapping("/movimientos/lotes/{codLote}")
    public ResponseEntity<Page<MovimientoResponse>> obtenerMovimientosPorCodLote(
            @PathVariable String codLote, Pageable pageable) {
        return ResponseEntity.ok(invServ.obtenerMovimientoPorLote(codLote, pageable));
    }

    @GetMapping("/movimientos/usuarios/{rutUsuario}")
    public ResponseEntity<Page<MovimientoResponse>> obtenerMovimientosPorRutUsuario(
            @PathVariable String rutUsuario, Pageable pageable) {
        return ResponseEntity.ok(invServ.obtenerMovimientoPorUsuario(rutUsuario, pageable));
    }

    @GetMapping("/movimientos/fecha")
    public ResponseEntity<Page<MovimientoResponse>> obtenerMovimientosPorFecha(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fin, Pageable pageable) {
        return ResponseEntity.ok(invServ.obtenerMovimientosPorfecha(
            inicio.atStartOfDay(), fin.atTime(23, 59, 59), pageable));
    }

    @GetMapping("/movimientos/sucursales/{codSucursal}")
    public ResponseEntity<Page<MovimientoResponse>> obtenerMovimientosPorSucursal(
            @PathVariable String codSucursal, Pageable pageable) {
        return ResponseEntity.ok(invServ.obtenerMovimientoPorSucursal(codSucursal, pageable));
    }

    // ==== PETICIONES POST ====

    @PostMapping("/recepciones")
    public ResponseEntity<List<LoteResponse>> registrarRecepcion(
            @RequestBody @Valid RecepcionRequest request,
            @RequestHeader("X-Rut-Usuario") String rutUsuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(invServ.registrarRecepcion(request, rutUsuario));
    }

    // Agregar endpoint para VENTA


    // ==== PETICIONES PUT/PATCH ====

    @PutMapping("/{sku}/{codSucursal}/lotes/{codLote}/estado")
    public ResponseEntity<Void> cambiarEstadoLote(
            @PathVariable String sku, @PathVariable String codSucursal, @PathVariable String codLote,
            @RequestParam EstadoLote nuevoEstado) {
        invServ.cambiarEstadoLote(sku, codSucursal, codLote, nuevoEstado);
        return ResponseEntity.noContent().build();
    }


    // ==== PETICIONES DELETE ====

    @DeleteMapping("/{sku}/{codSucursal}")
    public ResponseEntity<Void> eliminarInventario(@PathVariable String sku, @PathVariable String codSucursal){
        invServ.eliminarInventario(sku, codSucursal);
        return ResponseEntity.noContent().build();
    }

}
