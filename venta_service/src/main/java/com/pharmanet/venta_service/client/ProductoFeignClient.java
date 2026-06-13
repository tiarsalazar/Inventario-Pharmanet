package com.pharmanet.venta_service.client;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "producto-service", url = "http://localhost:8083")
public interface ProductoFeignClient {

    @PostMapping("/api/v1/productos/recetas")
    ResponseEntity<String> obtenerReceta(@RequestBody List<String> productos);

    @PostMapping("/api/v1/productos/calcularTotal")
    ResponseEntity<BigDecimal> calcularPrecioVentaTotal(@RequestBody Map<String, Integer> productos);

}
