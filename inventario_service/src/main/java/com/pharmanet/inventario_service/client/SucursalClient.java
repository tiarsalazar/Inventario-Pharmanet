package com.pharmanet.inventario_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.pharmanet.inventario_service.dto.client.SucursalResponse;

@FeignClient(name = "sucursal-service", url = "http://localhost:8081")
public interface SucursalClient {
    @GetMapping("/api/v1/sucursales/{codInterno}")
    SucursalResponse buscarSucursal(@PathVariable String codInterno);
}
