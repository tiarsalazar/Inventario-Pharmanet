package com.pharmanet.abastecimiento_service.controller;

import java.time.LocalDate;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmanet.abastecimiento_service.dto.recepcion.RecepcionRequest;
import com.pharmanet.abastecimiento_service.dto.recepcion.RecepcionResponse;
import com.pharmanet.abastecimiento_service.service.RecepcionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "Recepciones", description = "Operaciones relacionadas a gestion de Recepciones")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recepciones")
public class RecepcionController {

    private final RecepcionService recepServ;

    // ==== PETICIONES GET ====
    @Operation(summary = "Busca recepcion", description = "Busca los datos de una recepcion por su id y codigo de sucursal.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Recepcion encontrada correctamente."),
        @ApiResponse(responseCode = "404", description = "Recepcion solicitada no encontrada."),
        @ApiResponse(responseCode = "500", description = "Error interno no controlado del servidor.")
    })
    @GetMapping("/sucursales/{codSucursal}/{id}")
    public ResponseEntity<RecepcionResponse> buscarPorId(
            @Parameter(description = "Código único de la sucursal", example = "SU0001") @PathVariable String codSucursal,
            @Parameter(description = "ID numérico de la recepción", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(recepServ.buscarPorId(id, codSucursal));
    }
    
    @Operation(summary = "Listar recepciones por sucursal paginado", description = "Obtiene todas las recepciones asociadas a un código de sucursal.")
    @ApiResponse(responseCode = "200", description = "Listado paginado retornado exitosamente.")
    @GetMapping("/sucursales/{codSucursal}")
    public ResponseEntity<Page<RecepcionResponse>> buscarRecepcionPorSucursal(
            @Parameter(description = "Código único de la sucursal", example = "SU0001") @PathVariable String codSucursal,
            @ParameterObject Pageable pageable){
        return ResponseEntity.ok(recepServ.buscarRecepcionPorSucursal(codSucursal, pageable));
    }

    @Operation(summary = "Filtrar recepciones por rango de fechas", description = "Retorna registros de recepción validados entre una fecha de inicio y una de fin.")
    @ApiResponse(responseCode = "200", description = "Listado filtrado retornado exitosamente.")
    @ApiResponse(responseCode = "400", description = "Formatos de fechas inválidos.")
    @GetMapping("/sucursales/{codSucursal}/fechas")
    public ResponseEntity<Page<RecepcionResponse>> buscarPorFecha(
            @Parameter(description = "Código único de la sucursal", example = "SU0001") @PathVariable String codSucursal,
            @Parameter(description = "Fecha inicial del rango de búsqueda (yyyy-MM-dd)", example = "2026-06-01") @RequestParam LocalDate inicio,
            @Parameter(description = "Fecha final del rango de búsqueda (yyyy-MM-dd)", example = "2026-06-15") @RequestParam LocalDate fin,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(recepServ.buscarPorFecha(codSucursal, inicio, fin, pageable));
    }

    @Operation(summary = "Filtrar recepciones por operador", description = "Obtiene el historial de recepciones generadas por el RUN de un usuario específico.")
    @ApiResponse(responseCode = "200", description = "Listado filtrado retornado exitosamente.")
    @GetMapping("/sucursales/{codSucursal}/usuarios")
    public ResponseEntity<Page<RecepcionResponse>> buscarPorRunUsuario(
            @Parameter(description = "Código único de la sucursal", example = "SU0001") @PathVariable String codSucursal,
            @Parameter(description = "RUN del operador sin puntos y con guión", example = "12345678-9") @RequestParam String runUsuario,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(recepServ.buscarPorUsuario(runUsuario, codSucursal, pageable));
    }
    
    // ==== PETICIONES POST ====

    @Operation(summary = "Registrar una nueva recepción", description = "Registra una nueva recepcion, guardando los datos y solicitando a Inventario el ingreso del stock.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Recepción registrada y stock actualizado con éxito."),
        @ApiResponse(responseCode = "400", description = "Datos de entrada con errores de validación o fallas lógicas de negocio."),
        @ApiResponse(responseCode = "409", description = "Conflicto por duplicidad de número de documento o claves únicas.")
    })
    @PostMapping("/sucursales/{codSucursal}/registrar")
    public ResponseEntity<RecepcionResponse> registrarRecepcion(
            @Parameter(description = "Código único de la sucursal", example = "SU0001") @PathVariable String codSucursal,
            @Valid @RequestBody RecepcionRequest request,
            @Parameter(description = "RUN del usuario autenticado que realiza la recepción", example = "12345678-9", required = true)
            @RequestHeader("X-Run-Usuario") String runUsuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recepServ.registrarRecepcion(request, runUsuario, codSucursal));
    }

    // ==== PETICIONES PUT ====

    @Operation(summary = "Cancelar recepción activa", description = "Cambia el estado de una recepción a cancelado por su ID y sucursal correspondientes.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Transacción cancelada correctamente, no retorna contenido."),
        @ApiResponse(responseCode = "404", description = "ID o sucursal no encontrados.")
    })
    @PutMapping("/sucursales/{codSucursal}/{id}/cancelar")
    public ResponseEntity<Void> cancelarRecepcion(
            @Parameter(description = "Código único de la sucursal", example = "SU0001") @PathVariable String codSucursal,
            @Parameter(description = "ID numérico de la recepción a cancelar", example = "1") @PathVariable Long id) {
        recepServ.cancelarRecepcionPorId(id, codSucursal);
        return ResponseEntity.noContent().build();
    }
    
    // ==== PETICIONES DELETE ====

    @Operation(summary = "Eliminar físicamente una recepción", description = "Remueve permanentemente el registro de la recepcion de la base de datos.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Registro eliminado físicamente de manera correcta."),
        @ApiResponse(responseCode = "404", description = "Registro no encontrado.")
    })
    @DeleteMapping("/sucursales/{codSucursal}/{id}")
    public ResponseEntity<Void> eliminarRecepcionPorId(
            @Parameter(description = "Código único de la sucursal", example = "SU0001") @PathVariable String codSucursal,
            @Parameter(description = "ID numérico de la recepción a eliminar físicamente", example = "1") @PathVariable Long id){
        recepServ.eliminarRecepcionPorId(id, codSucursal);
        return ResponseEntity.noContent().build();
    }
}
