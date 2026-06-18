package com.pharmanet.ubicacion_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.pharmanet.ubicacion_service.dto.ComunaDto;
import com.pharmanet.ubicacion_service.entity.Comuna;
import com.pharmanet.ubicacion_service.entity.Region;
import com.pharmanet.ubicacion_service.exception.ResourceAlreadyExistsException;
import com.pharmanet.ubicacion_service.exception.ResourceNotFoundException;
import com.pharmanet.ubicacion_service.repository.ComunaRepository;
import com.pharmanet.ubicacion_service.repository.RegionRepository;

@ExtendWith(MockitoExtension.class)
public class ComunaServiceTest {

    @Mock
    private ComunaRepository repo;

    @Mock
    private RegionRepository repoReg;

    @InjectMocks
    private ComunaService service;

    // ===========================================
    // SAVE
    // ===========================================

    @Test
    @DisplayName("Agrega una comuna exitosamente")
    void save_HappyPath() {
        Region region = new Region();
        region.setCodRegion("1");

        Comuna entidad = new Comuna();
        entidad.setCodComuna(1);
        entidad.setDescripcion("COMUNA");
        entidad.setRegion(region);

        ComunaDto dto = new ComunaDto();
        dto.setCodComuna(1);
        dto.setDescripcion("COMUNA");
        dto.setRegion("1");

        when(repo.findByCodComuna(1)).thenReturn(Optional.empty());
        when(repoReg.findByCodRegion("1")).thenReturn(Optional.of(region));
        when(repo.save(any(Comuna.class))).thenReturn(entidad);
        

        ComunaDto resultado = service.agregarComuna(dto);

        assertNotNull(resultado);
        assertEquals(dto.getCodComuna(), resultado.getCodComuna());

        verify(repo).findByCodComuna(1);
        verify(repo).save(any(Comuna.class));
    }

    @Test
    @DisplayName("Ya existe una comuna con ese código")
    void save_AlreadyExistsCodRegion() {
        Comuna entidad = new Comuna();
        entidad.setCodComuna(1);

        ComunaDto dto = new ComunaDto();
        dto.setCodComuna(1);

        when(repo.findByCodComuna(1)).thenReturn(Optional.of(entidad));

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class, () -> service.agregarComuna(dto));

        assertNotNull(exception);
        assertEquals("Ya existe la comuna con ese código o descripción", exception.getMessage());

        verify(repo).findByCodComuna(1);
    }

    @Test
    @DisplayName("Ya existe una comuna con esa descripción")
    void save_AlreadyExistsDescripcion() {
        Comuna entidad = new Comuna();
        entidad.setCodComuna(1);
        entidad.setDescripcion("COMUNA");

        ComunaDto dto = new ComunaDto();
        dto.setCodComuna(1);

        when(repo.findByCodComuna(1)).thenReturn(Optional.of(entidad));

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class, () -> service.agregarComuna(dto));

        assertNotNull(exception);
        assertEquals("Ya existe la comuna con ese código o descripción", exception.getMessage());

        verify(repo).findByCodComuna(1);
    }

    @Test
    @DisplayName("No existe la región")
    void save_RegionNotFound() {
        ComunaDto dto = new ComunaDto();
        dto.setCodComuna(1);
        dto.setRegion("1");

        when(repo.findByCodComuna(1)).thenReturn(Optional.empty());
        when(repoReg.findByCodRegion("1")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.agregarComuna(dto));

        assertNotNull(exception);
        assertEquals("No se encuentra la región con el código: 1", exception.getMessage());

        verify(repo).findByCodComuna(1);
        verify(repoReg).findByCodRegion("1");
    }

    // ===========================================
    // BUSCAR POR CÓDIGO DE LA COMUNA
    // ===========================================

    @Test
    @DisplayName("Devuelve una comuna")
    void shouldReturnComuna_NormalCase() {

        Region region = new Region();
        region.setCodRegion("1");

        Comuna entidad = new Comuna();
        entidad.setCodComuna(1);
        entidad.setRegion(region);

        when(repo.findByCodComuna(1)).thenReturn(Optional.of(entidad));

        ComunaDto resultado = service.buscarComuna(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getCodComuna());

        verify(repo).findByCodComuna(1);
    }

    @Test
    @DisplayName("No encuentra la comuna")
    void shouldThrow_NotFound() {
        when(repo.findByCodComuna(1)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> service.buscarComuna(1));

        assertNotNull(exception);
        assertEquals("No se encuentra la comuna con el código: 1", exception.getMessage());

        verify(repo).findByCodComuna(1);
    }

    // ===========================================
    // MOSTRAR TODOS
    // ===========================================

    @Test
    @DisplayName("Devuelve un pageable de todos las regiones")
    void shouldReturnAllProducts() {

        Region region = new Region();
        region.setCodRegion("1");

        Comuna entidad = new Comuna();
        entidad.setCodComuna(1);
        entidad.setRegion(region);

        Pageable pageable = PageRequest.of(0, 10);

        List<Comuna> lista = List.of(entidad);

        Page<Comuna> page = new PageImpl<>(lista, pageable, lista.size());

        when(repo.findAll(pageable)).thenReturn(page);

        Page<ComunaDto> resultado = service.mostrarTodasComunas(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(1, resultado.getContent().get(0).getCodComuna());

        verify(repo).findAll(pageable);
    }

    // ===========================================
    // UPDATE
    // ===========================================

    @Test
    @DisplayName("Actualiza la comuna exitosamente")
    void update_NormalCase() {
        Region region = new Region();
        region.setCodRegion("1");

        Comuna entidad = new Comuna();
        entidad.setCodComuna(1);
        entidad.setRegion(region);

        ComunaDto dto = new ComunaDto();
        dto.setCodComuna(1);
        dto.setRegion("1");

        when(repo.findByCodComuna(1)).thenReturn(Optional.of(entidad));
        when(repoReg.findByCodRegion("1")).thenReturn(Optional.of(region));
        when(repo.save(entidad)).thenReturn(null);

        service.actualizarComuna(dto);

        verify(repo).findByCodComuna(1);
        verify(repo).save(entidad);
    }

    @Test
    @DisplayName("No se encuentra la comuna")
    void update_NotFound() {
        ComunaDto dto = new ComunaDto();
        dto.setCodComuna(1);

        when(repo.findByCodComuna(1)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> service.actualizarComuna(dto)
        );

        assertNotNull(exception);
        assertEquals("No se encuentra la comuna con el código: 1", exception.getMessage());

        verify(repo).findByCodComuna(1);
    }

    @Test
    @DisplayName("No se encuentra la región")
    void update_RegionNotFound() {

        Comuna entidad = new Comuna();
        entidad.setCodComuna(1);

        ComunaDto dto = new ComunaDto();
        dto.setCodComuna(1);
        dto.setRegion("1");

        when(repo.findByCodComuna(1)).thenReturn(Optional.of(entidad));
        when(repoReg.findByCodRegion("1")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> service.actualizarComuna(dto)
        );

        assertNotNull(exception);
        assertEquals("No se encuentra la región con el código: 1", exception.getMessage());

        verify(repo).findByCodComuna(1);
        verify(repoReg).findByCodRegion("1");
    }

    // ===========================================
    // DELETE
    // ===========================================

    @Test
    @DisplayName("Elimina una comuna exitosamente")
    void delete_NormalCase() {
        Comuna entidad = new Comuna();
        entidad.setCodComuna(1);

        when(repo.findByCodComuna(1)).thenReturn(Optional.of(entidad));

        service.eliminarComuna(1);

        verify(repo).findByCodComuna(1);
        verify(repo).delete(entidad);
    }

    @Test
    @DisplayName("No encuentra la comuna")
    void delete_NotFound() {

        when(repo.findByCodComuna(1)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> service.eliminarComuna(1)
        );

        assertNotNull(exception);
        assertEquals("No se encuentra la comuna con el código: 1", exception.getMessage());

        verify(repo).findByCodComuna(1);
    }

    // ===========================================
    // VALIDAR UBICACION
    // ===========================================

    @Test
    @DisplayName("Valida que la comuna se encuentre en la región ingresada")
    void validarComuna_NormalCase() {

        Region region = new Region();
        region.setCodRegion("1");

        Comuna entidad = new Comuna();
        entidad.setCodComuna(1);
        entidad.setRegion(region);

        when(repo.findByCodComunaAndRegion_CodRegion(1, "1")).thenReturn(Optional.of(entidad));

        ComunaDto resultado = service.validarComuna(1, "1");

        assertNotNull(resultado);
        assertEquals(1, resultado.getCodComuna());
        assertEquals("1", resultado.getRegion());

        verify(repo).findByCodComunaAndRegion_CodRegion(1, "1");
    }

    @Test
    @DisplayName("No existe una comuna con ese código y región")
    void validarComuna_BadCase() {

        when(repo.findByCodComunaAndRegion_CodRegion(1, "1")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.validarComuna(1, "1"));

        assertNotNull(exception);
        assertEquals("No se encuentra la comuna con el código: 1 de la región: 1", exception.getMessage());

        verify(repo).findByCodComunaAndRegion_CodRegion(1, "1");
    }
}
