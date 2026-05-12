package com.pharmanet.venta_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@FeignClient(name = "inventario_service", url = "http://localhost:8081")
public class SucursalFeignClient {

    @GetMapping("/api/v1/sucursales/{codInterno}")
    SucursalDTO buscarSucursalPorCodInterno(@PathVariable
            @NotBlank(message = "El código interno no puede estar vacío")
            @Size(max = 10, message = "Máximo 10 caracteres")
            String codInterno);
}
