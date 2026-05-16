package com.pharmanet.inventario_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.pharmanet.inventario_service.dto.client.ProductoResponse;

@FeignClient(name = "producto_service", url = "http://localhost:8083")
public interface ProductoClient {
    @GetMapping("/api/v1/productos/{sku}")
    ProductoResponse buscarPorSku(@PathVariable String sku);
}
