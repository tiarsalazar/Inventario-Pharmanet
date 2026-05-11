package com.pharmanet.usuario_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmanet.usuario_service.dto.UsuarioDTO;
import com.pharmanet.usuario_service.service.UsuarioService;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Slf4j
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<Page<UsuarioDTO>> mostrarTodos(@PageableDefault(size = 10, sort = "nombreCompleto") Pageable pageable) {
        log.info("Se inicia búsqueda de todos los usuarios");
        Page<UsuarioDTO> todosUsuarios = usuarioService.mostrarTodos(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(todosUsuarios);
    }

    @GetMapping("/{run}")
    public ResponseEntity<UsuarioDTO> buscarPorRun(@PathVariable String run) {
        UsuarioDTO usuarioDto = usuarioService.buscarPorRun(run);

        return ResponseEntity.status(HttpStatus.OK).body(usuarioDto);
    }

    @GetMapping("profesion")
    public ResponseEntity<Page<UsuarioDTO>> buscarPorProfesion(@RequestParam String profesion, @PageableDefault(size = 10, sort = "nombreCompleto") Pageable pageable) {
        Page<UsuarioDTO> usuariosPorProfesion = usuarioService.buscarPorProfesion(profesion, pageable);
        
        return ResponseEntity.status(HttpStatus.OK).body(usuariosPorProfesion);
    }
    
    @GetMapping("sucursal")
    public ResponseEntity<Page<UsuarioDTO>> buscarPorSucursal(@RequestParam String codInterno, @PageableDefault(size = 10, sort = "nombreCompleto") Pageable pageable) {
        Page<UsuarioDTO> usuariosPorSucursal = usuarioService.buscarPorSucursal(codInterno, pageable);
        
        return ResponseEntity.status(HttpStatus.OK).body(usuariosPorSucursal);
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> agregarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO agregado = usuarioService.agregarUsuario(usuarioDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(agregado);
    }
    
    @PutMapping
    public ResponseEntity<?> actualizarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        usuarioService.actualizarUsuario(usuarioDTO);
        
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    @DeleteMapping("/{run}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable String run) {
        usuarioService.eliminarUsuario(run);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}