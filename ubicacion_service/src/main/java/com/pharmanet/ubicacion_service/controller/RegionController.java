package com.pharmanet.ubicacion_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmanet.ubicacion_service.dto.RegionDto;
import com.pharmanet.ubicacion_service.service.RegionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/regiones")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Regiones", description = "Operaciones relacionadas con las regiones.")
public class RegionController {

    private final RegionService regionService;

    @GetMapping
    @Operation(summary = "Obtiene todas las regiones", description = "Obtiene un pageable de todas las regiones")
    @ApiResponse(responseCode = "200", description = "Se obtiene un pageable de todas las regiones")
    public ResponseEntity<Page<RegionDto>> mostrarTodos(@PageableDefault(size = 10, sort = "codRegion") @ParameterObject Pageable pageable) {
        log.info("Inicia búsqueda de todas las regiones.");
        Page<RegionDto> regiones = regionService.mostrarTodasRegiones(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(regiones);
    }

    @GetMapping("/{codRegion}")
    @Operation(summary = "Busca una región", description = "Busca una región por código de región")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Obtiene una región exitosamente"),
        @ApiResponse(responseCode = "404", description = "Región no encontrada")
    })
    public ResponseEntity<RegionDto> buscarRegion(@Parameter(name = "codRegion", description = "Código de la region", required = true) @PathVariable String codRegion) {
        RegionDto region = regionService.buscarRegion(codRegion);
        return ResponseEntity.status(HttpStatus.OK).body(region);
    }

    @PostMapping
    @Operation(summary = "Agrega una región", description = "Agrega una región nueva")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Agrega una región exitosamente"),
        @ApiResponse(responseCode = "400", description = "Fallo en las validaciones"),
        @ApiResponse(responseCode = "409", description = "Ya existe una región con ese código")
    })
    public ResponseEntity<RegionDto> agregarRegion(@Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Región a crear", required = true) @RequestBody RegionDto region) {
        RegionDto resultado = regionService.agregarRegion(region);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }
    
    @PutMapping
    @Operation(summary = "Actualiza una región")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Actualiza una región exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encuentra la región a actualizar"),
        @ApiResponse(responseCode = "400", description = "Fallo en las validaciones")
    })
    public ResponseEntity<?> actualizarRegion(@Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Región a actualizar", required = true) @RequestBody RegionDto region) {
        regionService.actualizarRegion(region);   
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Elimina una región", description = "Elimina una región por código ingresado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Se elimina una región exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encuentra la región con el código ingresado")
    })
    @DeleteMapping("/{codRegion}")
    public ResponseEntity<?> eliminarRegion(@Parameter(name = "codRegion", description = "Código de la región a eliminar", required = true) @PathVariable String codRegion) {
        regionService.eliminarRegion(codRegion);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
}
