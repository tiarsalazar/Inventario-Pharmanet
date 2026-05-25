package com.pharmanet.venta_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.pharmanet.venta_service.request.UsuarioRequest;

@FeignClient(name = "usuario-service", url = "http://localhost:8082")
public interface UsuarioFeignClient {

    @PostMapping("/api/v1/usuarios/validado")
    boolean validarUsuarioVenta(@RequestBody UsuarioRequest request);
}
