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

import com.pharmanet.ubicacion_service.dto.RegionDto;
import com.pharmanet.ubicacion_service.entity.Region;
import com.pharmanet.ubicacion_service.exception.ResourceAlreadyExistsException;
import com.pharmanet.ubicacion_service.exception.ResourceNotFoundException;
import com.pharmanet.ubicacion_service.repository.RegionRepository;

@ExtendWith(MockitoExtension.class)
public class RegionServiceTest {
 
    @Mock
    private RegionRepository repo;

    @InjectMocks
    private RegionService service;

    // ===========================================
    // SAVE
    // ===========================================

    @Test
    @DisplayName("Agrega una región exitosamente")
    void save_HappyPath() {
        Region entidad = new Region();
        entidad.setCodRegion("1");
        entidad.setDescripcion("REGION");

        RegionDto dto = new RegionDto();
        dto.setCodRegion("1");
        dto.setDescripcion("REGION");

        when(repo.findByCodRegion("1")).thenReturn(Optional.empty());
        when(repo.save(any(Region.class))).thenReturn(entidad);

        RegionDto resultado = service.agregarRegion(dto);

        assertNotNull(resultado);
        assertEquals(dto.getCodRegion(), resultado.getCodRegion());

        verify(repo).findByCodRegion("1");
        verify(repo).save(any(Region.class));
    }

    @Test
    @DisplayName("Ya existe una región con ese código")
    void save_AlreadyExistsCodRegion() {
        Region entidad = new Region();
        entidad.setCodRegion("1");

        RegionDto dto = new RegionDto();
        dto.setCodRegion("1");

        when(repo.findByCodRegion("1")).thenReturn(Optional.of(entidad));

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class, () -> service.agregarRegion(dto));

        assertNotNull(exception);
        assertEquals("Ya existe la región con ese código o descripción", exception.getMessage());

        verify(repo).findByCodRegion("1");
    }

    @Test
    @DisplayName("Ya existe una región con esa descripción")
    void save_AlreadyExistsDescripcion() {
        Region entidad = new Region();
        entidad.setCodRegion("1");
        entidad.setDescripcion("REGION");

        RegionDto dto = new RegionDto();
        dto.setCodRegion("1");
        dto.setDescripcion("REGION");

        when(repo.findByCodRegion("1")).thenReturn(Optional.of(entidad));

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class, () -> service.agregarRegion(dto));

        assertNotNull(exception);
        assertEquals("Ya existe la región con ese código o descripción", exception.getMessage());

        verify(repo).findByCodRegion("1");
    }

    // ===========================================
    // BUSCAR POR CÓDIGO DE LA REGIÓN
    // ===========================================

    @Test
    @DisplayName("Devuelve una región")
    void shouldReturnRegion_NormalCase() {
        Region entidad = new Region();
        entidad.setCodRegion("1");

        when(repo.findByCodRegion("1")).thenReturn(Optional.of(entidad));

        RegionDto resultado = service.buscarRegion("1");

        assertNotNull(resultado);
        assertEquals("1", resultado.getCodRegion());

        verify(repo).findByCodRegion("1");
    }

    @Test
    @DisplayName("No encuentra la región")
    void shouldThrow_NotFound() {
        when(repo.findByCodRegion("1")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> service.buscarRegion("1"));

        assertNotNull(exception);
        assertEquals("No se encuentra la región con el código: 1", exception.getMessage());

        verify(repo).findByCodRegion("1");
    }

    // ===========================================
    // MOSTRAR TODOS
    // ===========================================

    @Test
    @DisplayName("Devuelve un pageable de todos las regiones")
    void shouldReturnAllProducts() {

        Region entidad = new Region();
        entidad.setCodRegion("1");

        Pageable pageable = PageRequest.of(0, 10);

        List<Region> lista = List.of(entidad);

        Page<Region> page = new PageImpl<>(lista, pageable, lista.size());

        when(repo.findAll(pageable)).thenReturn(page);

        Page<RegionDto> resultado = service.mostrarTodasRegiones(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("1", resultado.getContent().get(0).getCodRegion());

        verify(repo).findAll(pageable);
    }

    // ===========================================
    // UPDATE
    // ===========================================

    @Test
    @DisplayName("Actualiza la región exitosamente")
    void update_NormalCase() {
        Region entidad = new Region();
        entidad.setCodRegion("1");

        RegionDto dto = new RegionDto();
        dto.setCodRegion("1");

        when(repo.findByCodRegion("1")).thenReturn(Optional.of(entidad));
        when(repo.save(entidad)).thenReturn(null);

        service.actualizarRegion(dto);

        verify(repo).findByCodRegion("1");
        verify(repo).save(entidad);
    }

    @Test
    @DisplayName("No se encuentra la región")
    void update_NotFound() {
        RegionDto dto = new RegionDto();
        dto.setCodRegion("1");

        when(repo.findByCodRegion("1")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> service.actualizarRegion(dto)
        );

        assertNotNull(exception);
        assertEquals("No se encuentra la región con el código: 1", exception.getMessage());

        verify(repo).findByCodRegion("1");
    }

    // ===========================================
    // DELETE
    // ===========================================

    @Test
    @DisplayName("Elimina una región exitosamente")
    void delete_NormalCase() {
        Region entidad = new Region();
        entidad.setCodRegion("1");

        when(repo.findByCodRegion("1")).thenReturn(Optional.of(entidad));

        service.eliminarRegion("1");

        verify(repo).findByCodRegion("1");
        verify(repo).delete(entidad);
    }

    @Test
    @DisplayName("No encuentra la región")
    void delete_NotFound() {

        when(repo.findByCodRegion("1")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> service.eliminarRegion("1")
        );

        assertNotNull(exception);
        assertEquals("No se encuentra la región con el código: 1", exception.getMessage());

        verify(repo).findByCodRegion("1");
    }

}
