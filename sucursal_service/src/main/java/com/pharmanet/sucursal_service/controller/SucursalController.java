package com.pharmanet.sucursal_service.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pharmanet.sucursal_service.dto.SucursalDTO;
import com.pharmanet.sucursal_service.service.SucursalService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/sucursales")
@RequiredArgsConstructor
public class SucursalController {

    private final SucursalService sucursalService;

    @GetMapping
    public ResponseEntity<Page<SucursalDTO>> mostrarTodasLasSucursales(@PageableDefault (size = 10, sort = "codSucursal") Pageable pageable) {
        
        log.info("Inicia búsqueda de todas las sucursales.");
        Page<SucursalDTO> sucursales = sucursalService.mostrarTodasLasSucursales(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(sucursales);
    }

    @GetMapping("/{codSucursal}")
    public ResponseEntity<SucursalDTO> buscarSucursal(@PathVariable
            @NotBlank(message = "El código interno no puede estar vacío")
            @Size(max = 8, message = "Máximo 8 caracteres")
            String codSucursal) {
        
        SucursalDTO sucursal = sucursalService.buscarSucursalPorCodSucursal(codSucursal);
        return ResponseEntity.status(HttpStatus.OK).body(sucursal);
    }

    @GetMapping("/region")
    public ResponseEntity<Page<SucursalDTO>> buscarPorRegion(@PageableDefault (size = 10, sort = "codSucursal") Pageable pageable, @RequestParam String region) {

        Page<SucursalDTO> sucursalesEnRegion = sucursalService.buscarPorRegion(region, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(sucursalesEnRegion);
    }

    @PostMapping
    public ResponseEntity<SucursalDTO> agregarSucursal(@Valid @RequestBody SucursalDTO nueva) {

        SucursalDTO dto = sucursalService.agregarSucursal(nueva);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping
    public ResponseEntity<Void> actualizarSucursal(@Valid @RequestBody SucursalDTO nueva) {

        sucursalService.actualizarSucursal(nueva);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{codSucursal}")
    public ResponseEntity<Void> eliminarSucursal(@PathVariable
            @NotBlank(message = "El código interno no puede estar vacío")
            @Size(max = 10, message = "Máximo 10 caracteres")
            String codSucursal) {

        sucursalService.eliminarSucursal(codSucursal);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}