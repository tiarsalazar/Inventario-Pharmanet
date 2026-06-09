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
import com.pharmanet.sucursal_service.entity.Sucursal;
import com.pharmanet.sucursal_service.exception.ResourceAlreadyExistsException;
import com.pharmanet.sucursal_service.exception.ResourceNotFoundException;
import com.pharmanet.sucursal_service.repository.SucursalRepository;

import feign.FeignException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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

    @InjectMocks
    SucursalService service;

    
    @Test
    @DisplayName("Agrega una sucursal exitosamente")
    void save_HappyPath() {
        SucursalDTO dto = new SucursalDTO();
        dto.setCodSucursal("SU0001");
        dto.setNombreSucursal("FARMACIA 1");
        dto.setCodComuna(1);
        dto.setCodRegion("1");

        Sucursal entity = new Sucursal();

        when(repo.findByCodSucursal("SU0001")).thenReturn(Optional.empty());
        when(repo.findByNombreSucursal("FARMACIA 1")).thenReturn(Optional.empty());
        when(feign.validarUbicacion(1, "1")).thenReturn(null);
        when(repo.save(any(Sucursal.class))).thenReturn(entity);

        SucursalDTO resultado = service.agregarSucursal(dto);

        assertNotNull(resultado);
        assertEquals(dto, resultado);
        verify(repo, times(1)).findByCodSucursal("SU0001");
        verify(repo, times(1)).findByNombreSucursal("FARMACIA 1");
        verify(feign, times(1)).validarUbicacion(1, "1");
    }

    // convertirCodSucursal
    @Test
    @DisplayName("El código de la sucursal ingresado es normal y se ingresa exitosamente")
    void conversion_NormalCase() {
        String codigo = "SU0001";

        String resultado = service.convertirCodSucursal(codigo);

        assertNotNull(resultado);
        assertEquals(codigo, resultado);
    }

    @Test
    @DisplayName("El código ingresado es menor a 6 carácteres y no es el esperado")
    void conversion_LimitCase() {
        String codigo = "0001";

        String resultado = service.convertirCodSucursal(codigo);

        assertNotNull(resultado);
        assertEquals("SU0001", resultado);
    }

    @Test
    @DisplayName("El código ingresado no es válido")
    void shouldThrowConversion_BadCase() {
        String codigo = "000111111";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.convertirCodSucursal(codigo));

        assertNotNull(exception);
        assertEquals("El código ingresado no es válido. Ingrese un código menor o igual a 6 carácteres o que empiece con 'SU'", exception.getMessage());
    }

    @Test
    @DisplayName("Devuelve una sucursal exitosamente")
    void shouldFindByCodSucursal_whenExists() {

        Sucursal entidad = new Sucursal();
        entidad.setCodSucursal("SU0001");

        SucursalDTO dto =  new SucursalDTO();
        dto.setCodSucursal("SU0001");

        when(repo.findByCodSucursal("SU0001"))
            .thenReturn(Optional.of(entidad));

        SucursalDTO resultado = service.buscarSucursalPorCodSucursal("SU0001");

        assertNotNull(resultado);
        assertEquals(dto, resultado);
    }

    @Test
    @DisplayName("No se encuentra la sucursal")
    void shouldThrow_notFound() {

        when(repo.findByCodSucursal("SU0001")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.buscarSucursalPorCodSucursal("SU0001"));

    }

    @Test
    @DisplayName("Ya exite la sucursal con el código ingresado")
    void shouldThrow_alreadyExistsCodSucursal() {

        Sucursal entity = new Sucursal();
        entity.setCodSucursal("SU0001");

        SucursalDTO dto = new SucursalDTO();
        dto.setCodSucursal("SU0001");

        when(repo.findByCodSucursal("SU0001")).thenReturn(Optional.of(entity));

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class, () -> service.agregarSucursal(dto));

        assertEquals("Ya existe una sucursal con el código: SU0001", exception.getMessage());
        verify(repo, times(1)).findByCodSucursal("SU0001");
    }

    @Test
    @DisplayName("Ya existe una sucursal con el nombre ingresado")
    void shouldThrow_alreadyExistsNombreSucursal() {
        Sucursal entity = new Sucursal();
        entity.setCodSucursal("SU0001");
        entity.setNombreSucursal("FARMACIA 1");

        SucursalDTO dto = new SucursalDTO();
        dto.setCodSucursal("SU0001");
        dto.setNombreSucursal("FARMACIA 1");

        when(repo.findByCodSucursal("SU0001")).thenReturn(Optional.empty());
        when(repo.findByNombreSucursal("FARMACIA 1")).thenReturn(Optional.of(entity));
        
        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class, () -> service.agregarSucursal(dto));

        assertEquals("Ya existe una sucursal con el nombre: FARMACIA 1", exception.getMessage());
        verify(repo, times(1)).findByNombreSucursal("FARMACIA 1");
    }

    @Test
    @DisplayName("Ocurre un error al intentar conectar con otro microservicio")
    void shouldThrow_FeignException() {

        SucursalDTO dto = new SucursalDTO();
        dto.setCodSucursal("SU0001");
        dto.setCodComuna(1);
        dto.setCodRegion("1");

        when(repo.findByCodSucursal(any())).thenReturn(Optional.empty());
        when(repo.findByNombreSucursal(any())).thenReturn(Optional.empty());
        when(feign.validarUbicacion(1, "1")).thenThrow(mock(FeignException.class));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.agregarSucursal(dto));

        assertEquals("Ubicación inválida para comuna 1", exception.getMessage());
    }

    @Test
    @DisplayName("Devuelve un pageable con todas las sucursales")
    void shouldReturnAllSucursales() {

        Pageable pageable = PageRequest.of(0, 10);

        Sucursal entity = new Sucursal();
        entity.setCodSucursal("SU0001");
        entity.setCodRegion("RM");

        SucursalDTO dto = new SucursalDTO();
        dto.setCodSucursal("SU0001");
        dto.setCodRegion("RM");

        List<Sucursal> lista = List.of(entity);

        Page<Sucursal> page = new PageImpl<>(lista, pageable, lista.size());

        when(repo.findAll(pageable)).thenReturn(page);

        Page<SucursalDTO> resultado = service.mostrarTodasLasSucursales(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("SU0001", resultado.getContent().get(0).getCodSucursal());
        
        verify(repo, times(1)).findAll(pageable);
    }

    @Test
    void shouldReturnSucursalesByRegion() {

        Pageable pageable = PageRequest.of(0, 10);

        Sucursal entidad = new Sucursal();
        entidad.setCodSucursal("SU0001");
        entidad.setCodRegion("RM");

        SucursalDTO dto = new SucursalDTO();
        dto.setCodSucursal("SU0001");
        dto.setCodRegion("RM");

        List<Sucursal> lista = List.of(entidad);

        Page<Sucursal> page = new PageImpl<>(lista, pageable, lista.size());

        when(repo.findByCodRegion("RM", pageable))
                .thenReturn(page);

        Page<SucursalDTO> resultado =
                service.buscarPorRegion("RM", pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("SU0001", resultado.getContent().get(0).getCodSucursal());

        verify(repo).findByCodRegion("RM", pageable);
    }

    @Test
    void update_NormalCase() {
        Sucursal entidad = new Sucursal();
        entidad.setCodSucursal("SU0001");

        SucursalDTO dto = new SucursalDTO();
        dto.setCodSucursal("SU0001");

        when(repo.findByCodSucursal("SU0001")).thenReturn(Optional.of(entidad));
        when(repo.save(entidad)).thenReturn(entidad);

        service.actualizarSucursal(dto);

        verify(repo, times(1)).findByCodSucursal("SU0001");
        verify(repo, times(1)).save(entidad);

    }

    @Test
    void update_NotFound() {
        SucursalDTO dto = new SucursalDTO();
        dto.setCodSucursal("SU0001");

        when(repo.findByCodSucursal("SU0001")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> service.actualizarSucursal(dto)
        );

        assertNotNull(exception);
        assertEquals("No se encuentra la sucursal con el código: SU0001", exception.getMessage());
    }

    @Test
    void nombreUnico_NormalCase() {
        Sucursal entidad = new Sucursal();
        entidad.setNombreSucursal("FARMACIA1");

        when(repo.findByNombreSucursal("FARMACIA1")).thenReturn(Optional.empty());

        service.verificarNombreUnico(entidad);

        verify(repo, times(1)).findByNombreSucursal("FARMACIA1");
    }

    @Test
    void nombreUnico_LimitCase() {
        Sucursal entidad = new Sucursal();
        entidad.setCodSucursal("SU0001");
        entidad.setNombreSucursal("FARMACIA1");

        Sucursal verificado = new Sucursal();
        verificado.setCodSucursal("SU0001");
        verificado.setNombreSucursal("FARMACIA1");

        when(repo.findByNombreSucursal("FARMACIA1")).thenReturn(Optional.of(verificado));

        service.verificarNombreUnico(entidad);

        verify(repo, times(2)).findByNombreSucursal("FARMACIA1");
    }

    @Test
    void nombreUnico_BadCase() {
        Sucursal entidad = new Sucursal();
        entidad.setCodSucursal("SU0001");
        entidad.setNombreSucursal("FARMACIA1");

        Sucursal verificado = new Sucursal();
        verificado.setCodSucursal("SU0002");
        verificado.setNombreSucursal("FARMACIA1");

        when(repo.findByNombreSucursal("FARMACIA1")).thenReturn(Optional.of(verificado));

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class,
            () -> service.verificarNombreUnico(entidad));

        assertNotNull(exception);
        assertEquals("Ya existe una sucursal con el nombre: FARMACIA1", exception.getMessage());
        verify(repo, times(2)).findByNombreSucursal("FARMACIA1");
    }

    @Test
    void delete_NormalCase() {
        Sucursal entidad = new Sucursal();
        entidad.setCodSucursal("SU0001");

        when(repo.findByCodSucursal("SU0001")).thenReturn(Optional.of(entidad));

        service.eliminarSucursal("SU0001");

        verify(repo, times(1)).delete(entidad);
    }

    @Test
    void delete_NotFound() {
        when(repo.findByCodSucursal("SU0001")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> service.eliminarSucursal("SU0001")
        );

        assertNotNull(exception);
        assertEquals("No se encuentra la sucursal con el código: SU0001", exception.getMessage());
    }

}
