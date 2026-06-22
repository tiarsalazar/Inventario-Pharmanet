package com.pharmanet.sucursal_service.controller;

import org.springdoc.core.annotations.ParameterObject;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/sucursales")
@RequiredArgsConstructor
@Tag(name = "Sucursal", description = "Operaciones relacionadas con las sucursales")
public class SucursalController {

    private final SucursalService sucursalService;

    @GetMapping
    @Operation(summary = "Devuelve todas las sucursales", description = "Devuelve un pageable de todas las sucursales")
    @ApiResponse(responseCode = "200", description = "Devuelve un pageable de todas las sucursales correctamente")
    public ResponseEntity<Page<SucursalDTO>> mostrarTodasLasSucursales(@PageableDefault (size = 10, sort = "codSucursal") @ParameterObject Pageable pageable) {
        log.info("Inicia búsqueda de todas las sucursales.");
        Page<SucursalDTO> sucursales = sucursalService.mostrarTodasLasSucursales(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(sucursales);
    }

    @GetMapping("/{codSucursal}")
    @Operation(summary = "Busca una sucursal", description = "Busca una sucursal por su código")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Busca una sucursal exitosamente"),
        @ApiResponse(responseCode = "404", description = "No encuentra la sucursal")
    })
    public ResponseEntity<SucursalDTO> buscarSucursal(@PathVariable
            @NotBlank(message = "El código interno no puede estar vacío")
            @Size(max = 8, message = "Máximo 8 caracteres")
            String codSucursal) {
        
        SucursalDTO sucursal = sucursalService.buscarSucursalPorCodSucursal(codSucursal);
        return ResponseEntity.status(HttpStatus.OK).body(sucursal);
    }

    @GetMapping("/region")
    @Operation(summary = "Busca sucursales por región", description = "Devuelve un pageable con todas las sucursales en una región")
    @ApiResponse(responseCode = "200", description = "Retorna un pageable exitosamente")
    public ResponseEntity<Page<SucursalDTO>> buscarPorRegion(@PageableDefault (size = 10, sort = "codSucursal") @ParameterObject Pageable pageable, @RequestParam String region) {

        Page<SucursalDTO> sucursalesEnRegion = sucursalService.buscarPorRegion(region, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(sucursalesEnRegion);
    }

    @PostMapping
    @Operation(summary = "Agrega una sucursal", description = "Agrega una sucursal nueva")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Agrega una sucursal exitosamente"),
        @ApiResponse(responseCode = "409", description = "Ya existe una sucursal con ese código"),
        @ApiResponse(responseCode = "400", description = "Fallo en las validaciones")
    })
    public ResponseEntity<SucursalDTO> agregarSucursal(@Valid @RequestBody SucursalDTO nueva) {

        SucursalDTO dto = sucursalService.agregarSucursal(nueva);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping
    @Operation(summary = "Actualiza una sucursal")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Actualiza una sucursal exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encuentra sucursal"),
        @ApiResponse(responseCode = "400", description = "Fallo en las validaciones")
    })
    public ResponseEntity<Void> actualizarSucursal(@Valid @RequestBody SucursalDTO nueva) {

        sucursalService.actualizarSucursal(nueva);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{codSucursal}")
    @Operation(summary = "Elimina la sucursal", description = "Elimina una sucursal por su código")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Elimina una sucursal exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encuentra la sucursal")
    })
    public ResponseEntity<Void> eliminarSucursal(@PathVariable
            @NotBlank(message = "El código interno no puede estar vacío")
            @Size(max = 10, message = "Máximo 10 caracteres")
            String codSucursal) {

        sucursalService.eliminarSucursal(codSucursal);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}