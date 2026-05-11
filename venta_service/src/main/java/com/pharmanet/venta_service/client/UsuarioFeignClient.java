package com.pharmanet.venta_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@FeignClient(name = "usuario_service", url = "http://localhost:8082")
public class UsuarioFeignClient {

    @GetMapping("/api/v1/usuarios/{run}")
    SucursalDTO buscarSucursalPorCodInterno(@PathVariable String run);
}
