package com.pharmanet.sucursal_service.controller;

import java.util.List;

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

@RestController
@RequestMapping("/api/v1/sucursales")
@RequiredArgsConstructor
public class SucursalController {

    private final SucursalService sucursalService;

    @GetMapping
    public ResponseEntity<List<SucursalDTO>> mostrarTodasLasSucursales() {
        
        List<SucursalDTO> sucursales = sucursalService.mostrarTodasLasSucursales();
        return ResponseEntity.status(HttpStatus.OK).body(sucursales);
    }

    @GetMapping("/{codInterno}")
    public ResponseEntity<SucursalDTO> buscarSucursal(@PathVariable
            @NotBlank(message = "El código interno no puede estar vacío")
            @Size(max = 10, message = "Máximo 10 caracteres")
            String codInterno) {
        
        SucursalDTO sucursalDTO = sucursalService.buscarSucursal(codInterno);
        return ResponseEntity.status(HttpStatus.OK).body(sucursalDTO);
    }

    @GetMapping("/region")
    public ResponseEntity<List<SucursalDTO>> buscarPorRegion(@RequestParam String region) {

        List<SucursalDTO> sucursalesEnRegion = sucursalService.buscarPorRegion(region);
        return ResponseEntity.status(HttpStatus.OK).body(sucursalesEnRegion);
    }

    @PostMapping
    public ResponseEntity<SucursalDTO> agregarSucursal(@Valid @RequestBody SucursalDTO sucursalAAgregar) {

        SucursalDTO sucursalDTO = sucursalService.agregarSucursal(sucursalAAgregar);
        return ResponseEntity.status(HttpStatus.CREATED).body(sucursalDTO);
    }

    @PutMapping
    public ResponseEntity<Void> actualizarSucursal(@Valid @RequestBody SucursalDTO sucursalDTO) {

        sucursalService.actualizarSucursal(sucursalDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{codInterno}")
    public ResponseEntity<Void> eliminarSucursal(@PathVariable
            @NotBlank(message = "El código interno no puede estar vacío")
            @Size(max = 10, message = "Máximo 10 caracteres")
            String codInterno) {

        sucursalService.eliminarSucursal(codInterno);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
