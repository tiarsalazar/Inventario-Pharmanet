package com.pharmanet.venta_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "producto_service", url = "http://localhost:8083")
public interface ProductoFeignClient {

    @GetMapping("/api/v1/productos/receta/{sku}")
    String obtenerReceta(@PathVariable String sku);

}
