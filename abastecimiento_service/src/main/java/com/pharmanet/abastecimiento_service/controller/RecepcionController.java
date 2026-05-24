package com.pharmanet.abastecimiento_service.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmanet.abastecimiento_service.dto.recepcion.RecepcionRequest;
import com.pharmanet.abastecimiento_service.dto.recepcion.RecepcionResponse;
import com.pharmanet.abastecimiento_service.service.RecepcionService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recepciones")
public class RecepcionController {

    private final RecepcionService recepServ;

    // ==== PETICIONES GET ====

    @GetMapping("/sucursales/{codSucursal}/{id}")
    public ResponseEntity<RecepcionResponse> buscarPorId(
            @PathVariable String codSucursal,@PathVariable Long id) {
        return ResponseEntity.ok(recepServ.buscarPorId(id, codSucursal));
    }
    
    @GetMapping("/sucursales/{codSucursal}")
    public ResponseEntity<Page<RecepcionResponse>> buscarRecepcionPorSucursal(
            @PathVariable String codSucursal, Pageable pageable){
        return ResponseEntity.ok(recepServ.buscarRecepcionPorSucursal(codSucursal, pageable));
    }
    
    @GetMapping("/sucursales/{codSucursal}/proveedores")
    public ResponseEntity<Page<RecepcionResponse>> buscarPorRutProveedor(
            @PathVariable String codSucursal, @RequestParam String rutProveedor, Pageable pageable) {
        return ResponseEntity.ok(recepServ.buscarRecepcionPorProveedor(rutProveedor, codSucursal, pageable));
    }

    @GetMapping("/sucursales/{codSucursal}/ordenes")
    public ResponseEntity<Page<RecepcionResponse>> buscarPorOrdenCompra(
            @PathVariable String codSucursal, @RequestParam String ordenCompra, Pageable pageable) {
        return ResponseEntity.ok(recepServ.buscarPorOrdenCompra(ordenCompra, codSucursal, pageable));
    }

    @GetMapping("/sucursales/{codSucursal}/fechas")
    public ResponseEntity<Page<RecepcionResponse>> buscarPorFecha(
            @PathVariable String codSucursal, @RequestParam LocalDate inicio, @RequestParam LocalDate fin, Pageable pageable) {
        return ResponseEntity.ok(recepServ.buscarPorFecha(codSucursal, inicio, fin, pageable));
    }

    @GetMapping("/sucursales/{codSucursal}/usuarios")
    public ResponseEntity<Page<RecepcionResponse>> buscarPorRunUsuario(
            @PathVariable String codSucursal, @RequestParam String runUsuario, Pageable pageable) {
        return ResponseEntity.ok(recepServ.buscarPorUsuario(runUsuario, codSucursal, pageable));
    }
    
    // ==== PETICIONES POST ====

    @PostMapping("/Sucursales/{codSucursal}/registrar")
    public ResponseEntity<RecepcionResponse> registrarRecepcion(
            @PathVariable String codSucursal, @RequestBody RecepcionRequest request,
            @RequestHeader("X-Run-Usuario") String runUsuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recepServ.registrarRecepcion(request, runUsuario, codSucursal));
    }

    // ==== PETICIONES PUT ====

    @PutMapping("/sucursales/{codSucursal}/{id}/cancelar")
    public ResponseEntity<Void> cancelarRecepcion(
            @PathVariable String codSucursal, @PathVariable Long id) {
        recepServ.cancelarRecepcionPorId(id, codSucursal);
        return ResponseEntity.noContent().build();
    }
    

    // ==== PETICIONES DELETE ====

    @DeleteMapping("/sucursales/{codSucursal}/{id}")
    public ResponseEntity<Void> eliminarRecepcionPorId(
            @PathVariable String codSucursal, @PathVariable Long id){
        recepServ.eliminarRecepcionPorId(id, codSucursal);
        return ResponseEntity.noContent().build();
    }
    
}
