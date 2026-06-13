package com.pharmanet.venta_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmanet.venta_service.dto.RegistroVenta;
import com.pharmanet.venta_service.dto.VentaDto;
import com.pharmanet.venta_service.service.VentaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

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
public class VentaController {

    private final VentaService ventaService;

    @GetMapping
    public ResponseEntity<Page<VentaDto>> mostrarTodos(@PageableDefault(size = 10, sort = {"fechaVenta", "codVenta"}) Pageable pageable) {
        log.info("Inicia búsqueda de todas las ventas");
        Page<VentaDto> ventas = ventaService.mostrarTodos(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ventas);
    }
    

    @GetMapping("/{codVenta}")
    public ResponseEntity<RegistroVenta> buscarPorCodVenta(@PathVariable Long codVenta) {
        RegistroVenta dto = ventaService.buscarPorCodVenta(codVenta);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @GetMapping("/entre_fechas")
    public ResponseEntity<Page<VentaDto>> buscarPorFechas(
        @Valid @RequestParam LocalDate inicio,
        @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate termino,
        @PageableDefault (size = 10, sort = {"fechaVenta", "codVenta"}) Pageable pageable) {
        Page<VentaDto> resultado = ventaService.buscarPorFechas(inicio, termino, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resultado);
    }
    
    @GetMapping("/dia/{dia}")
    public ResponseEntity<Page<VentaDto>> buscarPorDia(@PathVariable @DateTimeFormat (iso = ISO.DATE) LocalDate dia,
        @PageableDefault(size = 10, sort= "codVenta") Pageable pageable) {
        Page<VentaDto> ventasPorDia = ventaService.buscarPorDia(dia, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ventasPorDia);
    }

    @PostMapping
    public ResponseEntity<RegistroVenta> agregarVenta(@Valid @RequestBody RegistroVenta dto) {
        RegistroVenta resultado = ventaService.agregarVenta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }
    
    @PutMapping
    public ResponseEntity<?> actualizarVenta(@Valid @RequestBody VentaDto ventaDto) {
        ventaService.actualizarVenta(ventaDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{codVenta}")
    public ResponseEntity<?> eliminarVenta(@PathVariable Long codVenta) {
        ventaService.eliminarVenta(codVenta);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
