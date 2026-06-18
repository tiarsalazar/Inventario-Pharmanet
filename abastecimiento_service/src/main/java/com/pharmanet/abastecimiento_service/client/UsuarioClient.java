package com.pharmanet.abastecimiento_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuario-service")
public interface UsuarioClient {
    @GetMapping("/api/v1/usuarios/{run}")
    void buscarPorRun(@PathVariable String run);
}
