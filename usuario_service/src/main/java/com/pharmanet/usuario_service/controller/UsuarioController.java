package com.pharmanet.usuario_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmanet.usuario_service.dto.UsuarioDTO;
import com.pharmanet.usuario_service.dto.connector.UsuarioRequest;
import com.pharmanet.usuario_service.dto.connector.UsuarioResponse;
import com.pharmanet.usuario_service.service.UsuarioService;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Usuarios", description = "Operaciones relacionadas con los usuarios.")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Obtiene todos los usuarios", description = "Obtiene un pageable de todos los usuarios")
    @ApiResponse(responseCode = "200", description = "Devuelve un pageable de todos los usuarios")
    public ResponseEntity<Page<UsuarioDTO>> mostrarTodos(@PageableDefault(size = 10, sort = "nombreCompleto") @ParameterObject Pageable pageable) {
        log.info("Se inicia búsqueda de todos los usuarios");
        Page<UsuarioDTO> todosUsuarios = usuarioService.mostrarTodos(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(todosUsuarios);
    }

    @GetMapping("/{run}")
    @Operation(summary = "Busca un usuario", description = "Busca un usuario por su run")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Devuelve un usuario"),
        @ApiResponse(responseCode = "404", description = "No encuentra al usuario")
    })
    public ResponseEntity<UsuarioDTO> buscarPorRun(@Parameter (name = "run", description = "Run del usuario")@PathVariable String run) {
        UsuarioDTO usuarioDto = usuarioService.buscarPorRun(run);

        return ResponseEntity.status(HttpStatus.OK).body(usuarioDto);
    }

    @GetMapping("/profesion")
    @Operation(summary = "Buscar por profesión", description = "Obtiene un pageable por tipo de profesión")
    @ApiResponse(responseCode = "200", description = "Obtiene un pageable por tipo de profesión")
    public ResponseEntity<Page<UsuarioDTO>> buscarPorProfesion(@RequestParam String prof, @PageableDefault(size = 10, sort = "nombreCompleto") @ParameterObject Pageable pageable) {
        Page<UsuarioDTO> usuariosPorProfesion = usuarioService.buscarPorProfesion(prof, pageable);
        
        return ResponseEntity.status(HttpStatus.OK).body(usuariosPorProfesion);
    }
    
    @GetMapping("/sucursal")
    @Operation(summary = "Busca por sucursal", description = "Obtiene un pageable por código de la sucursal")
    @ApiResponse(responseCode = "200", description = "Obtiene un pageable por el código de la sucursal")
    public ResponseEntity<Page<UsuarioDTO>> buscarPorSucursal(@RequestParam String su, @PageableDefault(size = 10, sort = "nombreCompleto") @ParameterObject Pageable pageable) {
        Page<UsuarioDTO> usuariosPorSucursal = usuarioService.buscarPorSucursal(su, pageable);
        
        return ResponseEntity.status(HttpStatus.OK).body(usuariosPorSucursal);
    }

    @PostMapping("/validado")
    @Operation(summary = "Valida que la venta sea válida", description = "Valida que el usuario pueda realizar la venta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Devuelve un elemento UsuarioResponse con la información obtenida"),
        @ApiResponse(responseCode = "404", description = "No encuentra el usuario")
    })
    public UsuarioResponse validarUsuarioVenta(@RequestBody UsuarioRequest request) {
        return usuarioService.validarUsuarioVenta(request);
    }

    @PostMapping
    @Operation(summary = "Agrega un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Agrega un usuario exitosamente"),
        @ApiResponse(responseCode = "404", description = "No encuentra la sucursal ingresada"),
        @ApiResponse(responseCode = "409", description = "Ya existe un usuario con ese run"),
        @ApiResponse(responseCode = "400", description = "Fallo en las validaciones")
    })
    public ResponseEntity<UsuarioDTO> agregarUsuario(@Valid @RequestBody UsuarioDTO dto) {
        UsuarioDTO agregado = usuarioService.agregarUsuario(dto);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(agregado);
    }
    
    @PutMapping
    @Operation(summary = "Actualizar usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuario acutalizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encuentra el usuario o no encuentra la sucursal ingresada"),
        @ApiResponse(responseCode = "400", description = "Fallo en las validaciones")
    })
    public ResponseEntity<?> actualizarUsuario(@Valid @RequestBody UsuarioDTO dto) {
        usuarioService.actualizarUsuario(dto);
        
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    @DeleteMapping("/{run}")
    @Operation(summary = "Eliminar un usuario", description = "Eliminar un usuario por su run")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Elimina a un usuario exitosamente"),
        @ApiResponse(responseCode = "404", description = "No encuentra el usuario")
    })
    public ResponseEntity<?> eliminarUsuario(@PathVariable String run) {
        usuarioService.eliminarUsuario(run);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}