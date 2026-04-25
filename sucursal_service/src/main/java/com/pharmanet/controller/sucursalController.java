package com.pharmanet.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pharmanet.dto.SucursalDTO;
import com.pharmanet.service.SucursalService;

import feign.ResponseMapper;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/v1")
@RequiredArgsConstructor
public class sucursalController {

    private final SucursalService sucursalService;

    @GetMapping
    public ResponseEntity<List<SucursalDTO>> mostrarTodasLasSucursales() {
        
        List<SucursalDTO> sucursales = sucursalService.mostrarTodasLasSucursales();
        return ResponseEntity.status(HttpStatus.OK).body(sucursales);
    }

    @GetMapping("/{codInterno}")
    public ResponseEntity<SucursalDTO> buscarSucursal(@PathVariable String codInterno) {
        
        SucursalDTO sucursalDTO = sucursalService.buscarSucursal(codInterno);
        return ResponseEntity.status(HttpStatus.OK).body(sucursalDTO);
    }

    @GetMapping("(/region")
    public ResponseEntity<List<SucursalDTO>> buscarPorRegion(@RequestParam String region) {

        List<SucursalDTO> sucursalesEnRegion = sucursalService.buscarPorRegion(region);
        return ResponseEntity.status(HttpStatus.OK).body(sucursalesEnRegion);
    }

    @PostMapping
    public ResponseEntity<SucursalDTO> agregarSucursal(@Valid @RequestBody sucursalAAgregar) {

        SucursalDTO sucursalDTO = sucursalService.agregarSucursal(sucursalAAgregar);
        return ResponseEntity.status(HttpStatus.CREATED).body(sucursalDTO);
    }

    @PutMapping
    public ResponseEntity<SucursalDTO> actualizarSucursal(@Valid @RequestBody sucursalDTO) {

        SucursalDTO sucursalDTO = sucursalService.actualizarSucursal(sucursalDTO);
        return ResponseEntity.status(HttpStatus.OK).body(sucursalDTO);
    }

    @DeleteMapping("/{codInterno}")
    public ResponseEntity<String> eliminarSucursal(@PathVariable String codInterno) {

        sucursalService.eliminarSucursal(codInterno);
        return ResponseEntity.status(HttpStatus.OK).body("La sucursal ha sido eliminada");
    }
}
