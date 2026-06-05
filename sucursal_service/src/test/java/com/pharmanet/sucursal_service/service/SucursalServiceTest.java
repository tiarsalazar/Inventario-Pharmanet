package com.pharmanet.sucursal_service.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.pharmanet.sucursal_service.client.UbicacionFeignClient;
import com.pharmanet.sucursal_service.dto.SucursalDTO;
import com.pharmanet.sucursal_service.dto.SucursalMapper;
import com.pharmanet.sucursal_service.entity.Sucursal;
import com.pharmanet.sucursal_service.exception.ResourceAlreadyExistsException;
import com.pharmanet.sucursal_service.exception.ResourceNotFoundException;
import com.pharmanet.sucursal_service.repository.SucursalRepository;

import feign.FeignException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test de SucursalService")
public class SucursalServiceTest {

    @Mock
    private UbicacionFeignClient feign;

    @Mock
    private SucursalRepository repo;

    @Mock
    private SucursalMapper mapper;

    @InjectMocks
    SucursalService service;

    
    @Test
    @DisplayName("Agrega una sucursal exitosamente")
    void save_GoodCase() {
        // GIVEN
        SucursalDTO dto = new SucursalDTO();
        dto.setCodSucursal("SU0001");
        dto.setNombreSucursal("FARMACIA 1");
        dto.setCodComuna(1);
        dto.setCodRegion("1");

        when(repo.findByCodSucursal("SU0001")).thenReturn(Optional.empty());
        when(repo.findByNombreSucursal("FARMACIA 1")).thenReturn(Optional.empty());
        when(service.convertirCodSucursal("SU0001")).thenReturn("SU0001");
        when(feign.validarUbicacion(1, "1")).thenReturn(null);

        // WHEN
        SucursalDTO resultado = service.agregarSucursal(dto);

        // THEN
        assertNotNull(resultado);
        assertEquals(dto, resultado);
        verify(repo, times(1)).findByCodSucursal("SU0001");
        verify(repo, times(1)).findByNombreSucursal("FARMACIA 1");
        verify(service, times(1)).convertirCodSucursal("SU0001");
        verify(feign, times(1)).validarUbicacion(1, "1");
    }

    // convertirCodSucursal
    @Test
    void conversion_NormalCase() {
        // GIVEN
        SucursalDTO dto = new SucursalDTO();
        dto.setCodSucursal("SU0001");

        when(service.convertirCodSucursal("SU0001")).thenReturn("SU0001");

        // WHEN
        SucursalDTO resultado = service.agregarSucursal(dto);

        // THEN
        assertNotNull(resultado.getCodSucursal());
        assertEquals("SU0001", resultado.getCodSucursal());
        verify(service, times(1)).convertirCodSucursal("SU0001");
    }

    @Test
    @DisplayName("Convierte el código de la sucursal en caso límite: Menos de 7 carácteres que no empiezan por SU")
    void conversion_LimitCase() {
        // GIVEN
        SucursalDTO dto = new SucursalDTO();
        dto.setCodSucursal("0001");

        when(service.convertirCodSucursal("0001")).thenReturn("SU0001");

        // WHEN
        SucursalDTO resultado = service.agregarSucursal(dto);

        // THEN
        assertNotNull(resultado.getCodSucursal());
        assertEquals("SU0001", resultado.getCodSucursal());
        verify(service, times(1)).convertirCodSucursal("0001");
    }

    @Test
    void shouldThrowConversion_BadCase() {
        // GIVEN
        SucursalDTO dto = new SucursalDTO();
        dto.setCodRegion("000111111");

        when(service.convertirCodSucursal("000111111"));

        // WHEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.agregarSucursal(dto));

        // THEN
        assertEquals("El código ingresado no es válido. Ingrese un código menor o igual a 6 carácteres o que empiece con 'SU'", exception);
        verify(service, times(1)).convertirCodSucursal("000111111");
    }

    @Test
    void shouldFindByCodSucursal_whenExists() {

        // GIVEN
        Sucursal entidad = new Sucursal();
        entidad.setCodSucursal("SU0001");

        SucursalDTO dto =  new SucursalDTO("SU0001", null, "5", 12, "CALLE 9");

        when(repo.findByCodSucursal("SU0001"))
            .thenReturn(Optional.of(entidad));
        
        when(mapper.toDto(entidad)).thenReturn(dto);

        // WHEN
        SucursalDTO resultado = service.buscarSucursalPorCodSucursal("SU0001");

        // THEN
        assertNotNull(resultado);
        assertEquals(dto, resultado);
    }

    // EXCEPTIONS

    @Test
    void shouldThrow_notFound() {

        // GIVEN
        when(repo.findByCodSucursal("SU0001")).thenReturn(Optional.empty());

        // WHEN + THEN
        assertThrows(ResourceNotFoundException.class, () -> service.buscarSucursalPorCodSucursal("SU0001"));
    }

    @Test
    void shouldThrow_alreadyExistsCodSucursal() {

        // GIVEN
        Sucursal entity = new Sucursal();
        entity.setCodSucursal("SU0001");

        SucursalDTO dto = new SucursalDTO();
        dto.setCodSucursal("SU0001");

        when(repo.findByCodSucursal("SU0001")).thenReturn(Optional.of(entity));

        // WHEN
        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class, () -> service.agregarSucursal(dto));

        // THEN
        assertEquals("Ya existe una sucursal con el código: SU0001", exception.getMessage());
        verify(repo, times(1)).findByCodSucursal("SU0001");
    }

    @Test
    void shouldThrow_alreadyExistsNombreSucursal() {
        // GIVEN
        Sucursal entity = new Sucursal();
        entity.setNombreSucursal("FARMACIA 1");

        SucursalDTO dto = new SucursalDTO();
        dto.setNombreSucursal("FARMACIA 1");

        when(repo.findByNombreSucursal("FARMACIA 1")).thenReturn(Optional.of(entity));
        
        // WHEN
        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class, () -> service.agregarSucursal(dto));

        // THEN
        assertEquals("Ya existe una sucursal con el nombre FARMACIA 1", exception);
        verify(repo, times(1)).findByNombreSucursal("FARMACIA 1");
    }

    @Test
    void shouldThrow_FeignException() {

        // GIVEN
        SucursalDTO dto = new SucursalDTO();
        dto.setCodComuna(1);
        dto.setCodRegion("1");

        when(feign.validarUbicacion(1, "1")).thenThrow(FeignException.class);

        // WHEN
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.agregarSucursal(dto));

        // THEN
        assertEquals("Ubicación inválida para comuna 1", exception);
    }

    @Test
    @DisplayName("Devuelve un pageable con todas las sucursales")
    void shouldReturnAllSucursales() {

        // GIVEN
        Pageable pageable = PageRequest.of(0, 10);

        Sucursal entity = new Sucursal();
        entity.setCodSucursal("SU0001");

        List<Sucursal> lista = List.of(entity);

        Page<Sucursal> page = new PageImpl<>(lista, pageable, lista.size());

        when(repo.findAll(pageable)).thenReturn(page);

        // WHEN
        Page<SucursalDTO> resultado = service.mostrarTodasLasSucursales(pageable);

        // THEN
        assertNotNull(resultado);
        assertEquals("SU0001", resultado.getContent().get(0).getCodSucursal());
        verify(repo, times(1)).findAll();
    }

    @Test
    void shouldReturnSucursalesByRegion() {

        // GIVEN
        Pageable pageable = PageRequest.of(0, 10);

        Sucursal entidad = new Sucursal();
        entidad.setCodSucursal("SU0001");
        entidad.setCodRegion("RM");

        List<Sucursal> lista = List.of(entidad);

        Page<Sucursal> page = new PageImpl<>(lista, pageable, lista.size());

        when(repo.findByCodRegion("RM", pageable))
                .thenReturn(page);

        // WHEN
        Page<SucursalDTO> resultado =
                service.buscarPorRegion("RM", pageable);

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("SU0001", resultado.getContent().get(0).getCodSucursal());

        verify(repo).findByCodRegion("RM", pageable);
    }

}
