package com.pharmanet.sucursal_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pharmanet.sucursal_service.dto.SucursalDTO;
import com.pharmanet.sucursal_service.dto.SucursalMapper;
import com.pharmanet.sucursal_service.repository.SucursalRepository;

@ExtendWith(MockitoExtension.class)
public class SucursalService {

    @Mock
    SucursalRepository repository;

    @Mock
    SucursalMapper mapper;

    @InjectMocks
    SucursalService service;

    @Test
    void devolverPageableDtoSucursal() {
        Pageable pageable = new Pageable(size = 10, sort = "codSucursal");

        

        Page<SucursalDTO> sucursales = repository.findAll(pageable)
            .map(mapper::toDto);
    }
}
