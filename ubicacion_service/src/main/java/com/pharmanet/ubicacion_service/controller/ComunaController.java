package com.pharmanet.ubicacion_service.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.pharmanet.ubicacion_service.dto.ComunaDto;
import com.pharmanet.ubicacion_service.service.ComunaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/v1/comunas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Comunas", description = "Operaciones relacionadas con las comunas.")
public class ComunaController {

    private final ComunaService comunaService;

    @GetMapping
    @Operation(summary = "Obtiene todas las comunas", description = "Obtiene un pageable de todas las comunas")
    @ApiResponse(responseCode = "200", description = "Se obtiene un pageable de todas las comuna")
    public ResponseEntity<Page<ComunaDto>> mostrarTodos(@PageableDefault(size = 10, sort = "codComuna") @ParameterObject Pageable pageable) {
        log.info("Inicia búsqueda de todas las comunas.");
        Page<ComunaDto> comunas = comunaService.mostrarTodasComunas(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(comunas);
    }

    @GetMapping("/{codComuna}")
    @Operation(summary = "Busca una comuna", description = "Busca una comuna por código")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Obtiene una comuna exitosamente"),
        @ApiResponse(responseCode = "404", description = "Comuna no encontrada")
    })
    public ResponseEntity<ComunaDto> buscarComuna(@PathVariable Integer codComuna) {
        ComunaDto comuna = comunaService.buscarComuna(codComuna);
        return ResponseEntity.status(HttpStatus.OK).body(comuna);
    }

    @GetMapping("/validado/{comu}/{region}")
    @Operation(summary = "Valida una ubicación", description = "Devuelve una respuesta con el resultado de la validación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Devuelve una respuesta exitosamente"),
        @ApiResponse(responseCode = "404", description = "La comuna ingresada no existe")
    }
    )
    public ResponseEntity<ComunaDto> validarUbicacion(@PathVariable Integer comu, @PathVariable String region) {
        ComunaDto comuna = comunaService.validarComuna(comu, region);
        return ResponseEntity.status(HttpStatus.OK).body(comuna);
    }
    

    @PostMapping
    @Operation(summary = "Agrega una comuna", description = "Agrega una comuna nueva")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Agrega una comuna exitosamente"),
        @ApiResponse(responseCode = "400", description = "Fallo en las validaciones"),
        @ApiResponse(responseCode = "409", description = "Ya existe una comuna con ese código")
    })
    public ResponseEntity<ComunaDto> agregarComuna(@Valid @RequestBody ComunaDto comuna) {
        ComunaDto resultado = comunaService.agregarComuna(comuna);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }
    
    @PutMapping
    @Operation(summary = "Actualiza una comuna")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Actualiza una comuna exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encuentra la comuna a actualizar"),
        @ApiResponse(responseCode = "400", description = "Fallo en las validaciones")
    })
    public ResponseEntity<?> actualizarComuna(@Valid @RequestBody ComunaDto comuna) {
        comunaService.actualizarComuna(comuna);   
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Elimina una comuna", description = "Elimina una comuna por código ingresado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Se elimina una comuna exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encuentra la comuna con el código ingresado")
    })
    @DeleteMapping("/{codComuna}")
    public ResponseEntity<?> eliminarComuna(@PathVariable Integer codComuna) {
        comunaService.eliminarComuna(codComuna);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
