package com.pharmanet.inventario_service.client.sucursal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "sucursal-service", url = "http://localhost:8081")
public interface SucursalClient {
    @GetMapping("/api/v1/sucursales/{codInterno}")
    SucursalRequest buscarSucursal(@PathVariable String codInterno);
}
