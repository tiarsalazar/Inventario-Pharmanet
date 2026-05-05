package com.pharmanet.usuario_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmanet.usuario_service.dto.EmpleadoDTO;
import com.pharmanet.usuario_service.service.EmpleadoService;

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
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    @GetMapping
    public ResponseEntity<Page<EmpleadoDTO>> mostrarTodos(@PageableDefault(size = 10, sort = "nombreCompleto") Pageable pageable) {
        log.info("Se inicia búsqueda de todos los empleados");
        Page<EmpleadoDTO> todosEmpleados = empleadoService.mostrarTodos(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(todosEmpleados);
    }

    @GetMapping("/{run}")
    public ResponseEntity<EmpleadoDTO> buscarPorRun(@PathVariable String run) {
        EmpleadoDTO empleadoDto = empleadoService.buscarPorRun(run);

        return ResponseEntity.status(HttpStatus.OK).body(empleadoDto);
    }

    @GetMapping("profesion")
    public ResponseEntity<Page<EmpleadoDTO>> buscarPorProfesion(@RequestParam String profesion, @PageableDefault(size = 10, sort = "nombreCompleto") Pageable pageable) {
        Page<EmpleadoDTO> empleadosPorProfesion = empleadoService.buscarPorProfesion(profesion, pageable);
        
        return ResponseEntity.status(HttpStatus.OK).body(empleadosPorProfesion);
    }
    
    @GetMapping("sucursal")
    public ResponseEntity<Page<EmpleadoDTO>> buscarPorSucursal(@RequestParam String codInterno, @PageableDefault(size = 10, sort = "nombreCompleto") Pageable pageable) {
        Page<EmpleadoDTO> empleadosPorProfesion = empleadoService.buscarPorSucursal(codInterno, pageable);
        
        return ResponseEntity.status(HttpStatus.OK).body(empleadosPorProfesion);
    }

    @PostMapping
    public ResponseEntity<EmpleadoDTO> agregarEmpleado(@Valid @RequestBody EmpleadoDTO empleadoDTO) {
        EmpleadoDTO agregado = empleadoService.agregarEmpleado(empleadoDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(agregado);
    }
    
    @PutMapping
    public ResponseEntity<?> actualizarEmpleado(@Valid @RequestBody EmpleadoDTO empleadoDTO) {
        empleadoService.actualizarEmpleado(empleadoDTO);
        
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    @DeleteMapping("/{run}")
    public ResponseEntity<?> eliminarEmpleado(@PathVariable String run) {
        empleadoService.eliminarEmpleado(run);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}