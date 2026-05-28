package com.pharmanet.sucursal_service.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "localidad-service", url = "http://localhost:8087")
public interface LocalidadFeignClient {

    void validarRegionComuna(Integer regionId, Integer comunaId);
}
