package com.pharmanet.usuario_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.pharmanet.usuario_service.dto.SucursalDTO;
import com.pharmanet.usuario_service.exception.ResourceNotFoundException;

import feign.FeignException;

@FeignClient(name = "sucursal_service")
public interface EmpleadoClient {

    @GetMapping("/api/v1/sucursales/{codInterno}")

        SucursalDTO obtenerSucursalDTO(@PathVariable String codInterno);
    } catch (FeignExceptionNotFound e) {
        throw new ResourceNotFoundException("Sucursal no encontrada: " + codInterno);
    } catch (Exception ex) {
        throw new Exception("Error genérico");
    }

    try {
    SucursalDTO sucursal = empleadoClient.obtenerSucursalDTO(codInterno);
} catch (FeignException.NotFound e) {
    throw new ResourceNotFoundException("Sucursal no encontrada: " + codInterno);
}
    
}
