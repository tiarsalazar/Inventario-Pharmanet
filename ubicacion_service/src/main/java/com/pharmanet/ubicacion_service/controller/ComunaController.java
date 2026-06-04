package com.pharmanet.ubicacion_service.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.pharmanet.ubicacion_service.dto.ComunaDto;
import com.pharmanet.ubicacion_service.service.ComunaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/v1/comunas")
@RequiredArgsConstructor
@Slf4j
public class ComunaController {

    private final ComunaService comunaService;

    @GetMapping
    public ResponseEntity<Page<ComunaDto>> mostrarTodos(@PageableDefault(size = 10, sort = "codComuna") Pageable pageable) {
        log.info("Inicia búsqueda de todas las comunas.");
        Page<ComunaDto> comunas = comunaService.mostrarTodasComunas(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(comunas);
    }

    @GetMapping("/{codComuna}")
    public ResponseEntity<ComunaDto> buscarComuna(@PathVariable Integer codComuna) {
        ComunaDto comuna = comunaService.buscarComuna(codComuna);
        return ResponseEntity.status(HttpStatus.OK).body(comuna);
    }

    @GetMapping("/validar/{comu}/{region}")
    public ResponseEntity<ComunaDto> validarUbicacion(@PathVariable Integer comu, @PathVariable String region) {
        ComunaDto comuna = comunaService.validarComuna(comu, region);
        return ResponseEntity.status(HttpStatus.OK).body(comuna);
    }
    

    @PostMapping
    public ResponseEntity<ComunaDto> agregarComuna(@RequestBody ComunaDto comuna) {
        ComunaDto resultado = comunaService.agregarComuna(comuna);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }
    
    @PutMapping
    public ResponseEntity<?> actualizarComuna(@RequestBody ComunaDto comuna) {
        comunaService.actualizarComuna(comuna);   
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{codComuna}")
    public ResponseEntity<?> eliminarComuna(@PathVariable Integer codComuna) {
        comunaService.eliminarComuna(codComuna);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
