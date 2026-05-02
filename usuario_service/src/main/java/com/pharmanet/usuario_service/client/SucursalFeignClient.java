package com.pharmanet.usuario_service.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "sucursal_service")
public interface SucursalFeignClient {

    @GetMapping("/{codInterno}")
    SucursalDTO findByCodInterno(@PathVariable String codInterno); 
    
}
