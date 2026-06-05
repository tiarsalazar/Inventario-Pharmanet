package com.pharmanet.localidad_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pharmanet.ubicacion_service.dto.RegionMapper;
import com.pharmanet.ubicacion_service.repository.RegionRepository;

@ExtendWith(MockitoExtension.class)
public class RegionService {
 
    @Mock
    private RegionRepository repository;

    @Mock
    private RegionMapper dtoMapper;

    @InjectMocks
    private RegionService service;

    @Test
    void devolverPageableDtoRegion() {

    }

}
