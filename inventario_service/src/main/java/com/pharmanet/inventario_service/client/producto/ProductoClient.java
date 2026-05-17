package com.pharmanet.inventario_service.client.producto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "producto-service", url = "http://localhost:8083")
public interface ProductoClient {
    @GetMapping("/api/v1/productos/{sku}")
    ProductoRequest buscarPorSku(@PathVariable String sku);
}
