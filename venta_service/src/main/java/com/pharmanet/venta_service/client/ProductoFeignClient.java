package com.pharmanet.venta_service.client;

@FeignClient(name = "producto_service", url = "http://localhost:8083")
public interface ProductoFeignClient {

    @GetMapping("/receta/{sku}")
    String buscarClaseReceta(@PathVariable String sku);
}
