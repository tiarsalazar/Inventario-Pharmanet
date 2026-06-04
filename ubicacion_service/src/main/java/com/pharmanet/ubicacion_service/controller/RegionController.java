package com.pharmanet.ubicacion_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmanet.ubicacion_service.dto.RegionDto;
import com.pharmanet.ubicacion_service.service.RegionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/regiones")
@RequiredArgsConstructor
@Slf4j
public class RegionController {

    private final RegionService regionService;

    @GetMapping
    public ResponseEntity<Page<RegionDto>> mostrarTodos(@PageableDefault(size = 10, sort = "codRegion") Pageable pageable) {
        log.info("Inicia búsqueda de todas las regiones.");
        Page<RegionDto> regiones = regionService.mostrarTodasRegiones(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(regiones);
    }

    @GetMapping("/{codRegion}")
    public ResponseEntity<RegionDto> buscarRegion(@PathVariable String codRegion) {
        RegionDto region = regionService.buscarRegion(codRegion);
        return ResponseEntity.status(HttpStatus.OK).body(region);
    }

    @PostMapping
    public ResponseEntity<RegionDto> agregarRegion(@Valid @RequestBody RegionDto region) {
        RegionDto resultado = regionService.agregarRegion(region);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }
    
    @PutMapping
    public ResponseEntity<?> actualizarRegion(@Valid @RequestBody RegionDto region) {
        regionService.actualizarRegion(region);   
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{codRegion}")
    public ResponseEntity<?> eliminarRegion(@PathVariable String codRegion) {
        regionService.eliminarRegion(codRegion);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
}
