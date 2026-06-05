package com.pharmanet.sucursal_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.pharmanet.sucursal_service.dto.ComunaDTO;

@FeignClient(name = "ubicacion-service", url = "http://localhost:8087")
public interface UbicacionFeignClient {

    @GetMapping("/api/v1/comunas/validado/{comu}/{region}")
    ComunaDTO validarUbicacion(@PathVariable Integer comu, @PathVariable String region);
}
