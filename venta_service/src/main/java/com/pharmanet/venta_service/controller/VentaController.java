package com.pharmanet.venta_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmanet.venta_service.dto.VentaDto;
import com.pharmanet.venta_service.service.VentaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("venta_service")
@RequiredArgsConstructor
@Slf4j
public class VentaController {

    private final VentaService ventaService;

    @GetMapping
    public ResponseEntity<Page<VentaDto>> mostrarTodos(Pageable pageable) {
        log.info("Inicia búsqueda de todas las ventas");
        Page<VentaDto> ventas = ventaService.mostrarTodos(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ventas);
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<VentaDto> buscarPorId(@PathVariable Long id) {
        VentaDto ventaDto = ventaService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(ventaDto);
    }

    @GetMapping("/entre_fechas")
    public String getMethodName(@Valid @RequestParam LocalDate inicio, @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate termino, Pageable pageable) {
        return new String();
    }
    
    
}
