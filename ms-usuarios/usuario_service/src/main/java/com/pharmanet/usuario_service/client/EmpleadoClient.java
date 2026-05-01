package com.pharmanet.usuario_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.pharmanet.usuario_service.dto.SucursalDTO;

@FeignClient(name = "sucursal_service")
public interface EmpleadoClient {

    @GetMapping("/usuarios/{codInterno}")
    SucursalDTO obtenerSucursalDTO(@PathVariable String codInterno);
}
