package com.pharmanet.abastecimiento_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.pharmanet.abastecimiento_service.dto.inventario.IngresoInventario;

@FeignClient(name = "inventario-service", url = "http://localhost:8084")
public interface InventarioClient {
    @PostMapping("/api/v1/inventarios/recepciones")
    void registrarStockRecepcion(
        @RequestBody IngresoInventario request,
        @RequestHeader("X-Run-Usuario") String runUsuario
    );
}
