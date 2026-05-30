package com.pharmanet.ubicacion_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmanet.ubicacion_service.entity.Region;
import com.pharmanet.ubicacion_service.service.RegionService;

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
    public ResponseEntity<Page<Region>> mostrarTodos(@PageableDefault(size = 10, sort = "regionId") Pageable pageable) {
        log.info("Inicia búsqueda de todas las regiones.");
        Page<Region> regiones = regionService.mostrarTodasRegiones(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(regiones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Region> buscarRegion(@PathVariable Integer id) {
        Region region = regionService.buscarRegion(id);
        return ResponseEntity.status(HttpStatus.OK).body(region);
    }

    @PostMapping
    public ResponseEntity<Region> agregarRegion(@RequestBody Region region) {
        Region resultado = regionService.agregarRegion(region);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }
    
    @PutMapping
    public ResponseEntity<?> actualizarRegion(@RequestBody Region region) {
        regionService.actualizarRegion(region);   
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping
    public ResponseEntity<?> eliminarRegion(@PathVariable Integer id) {
        regionService.eliminarRegion(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
}
