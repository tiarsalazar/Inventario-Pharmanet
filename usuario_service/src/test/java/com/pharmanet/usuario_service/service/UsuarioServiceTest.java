package com.pharmanet.usuario_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pharmanet.usuario_service.client.SucursalFeignClient;
import com.pharmanet.usuario_service.dto.UsuarioDTO;
import com.pharmanet.usuario_service.entity.Usuario;
import com.pharmanet.usuario_service.exception.ResourceAlreadyExistsException;
import com.pharmanet.usuario_service.exception.ResourceNotFoundException;
import com.pharmanet.usuario_service.repository.UsuarioRepository;

import feign.FeignException.FeignClientException;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test de UsuarioService")
public class UsuarioServiceTest {

    @Mock
    private SucursalFeignClient feign;

    @Mock
    private UsuarioRepository repo;

    @InjectMocks
    private UsuarioService service;

    // ==================================
    // AGREGAR USUARIO
    // ==================================

    @Test
    @DisplayName("Agrega un usuario exitosamente")
    void save_HappyPath() {
        Usuario entidad = new Usuario();
        entidad.setRun("11111111-1");
        entidad.setCodSucursal("SU0001");

        UsuarioDTO dto = new UsuarioDTO();
        dto.setRun("11111111-1");
        dto.setCodSucursal("SU0001");

        when(repo.findByRun("11111111-1")).thenReturn(Optional.of(entidad));
        when(feign.buscarSucursal("SU0001")).thenReturn(null);
        
        UsuarioDTO resultado = service.agregarUsuario(dto);

        assertNotNull(resultado);
        assertEquals(dto, resultado);
        verify(repo, times(1)).findByRun("11111111-1");
        verify(feign, times(1)).buscarSucursal("SU0001");
    }

    @Test
    @DisplayName("El usuario a agregar ya existe en la base de datos")
    void save_AlreadyExists() {
        Usuario entidad = new Usuario();
        entidad.setRun("11111111-1");

        UsuarioDTO dto = new UsuarioDTO();
        dto.setRun("11111111-1");

        when(repo.findByRun("11111111-1")).thenReturn(Optional.of(entidad));
        
        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class,
            () -> service.agregarUsuario(dto)
        );

        assertNotNull(exception);
        assertEquals("Ya existe un usuario con el rut 11111111-1", exception.getMessage());

        verify(repo, times(1)).findByRun("11111111-1");
    }

    @Test
    @DisplayName("Error al conectar al ms sucursal: la sucursal ingresada no existe")
    void saveShouldThrow_FeignClientException() {

        UsuarioDTO dto = new UsuarioDTO();
        dto.setRun("11111111-1");
        dto.setCodSucursal("SU0001");

        when(repo.findByRun("11111111-1")).thenReturn(Optional.empty());

        when(feign.buscarSucursal("SU0001")).thenThrow(FeignClientException.class);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> service.agregarUsuario(dto)
        );

        assertNotNull(exception);
        assertEquals("No se encuentra la sucursal con el código: SU0001", exception.getMessage());
        assertThrows(FeignClientException.class,
            () -> feign.buscarSucursal("SU0001")
        );

        verify(repo, times(1)).findByRun("11111111-1");
        verify(feign, times(1)).buscarSucursal("SU0001");
        
    }

    // ==================================
    // EXCEPTIONS
    // ==================================

    @Test
    @DisplayName("No se encuentra el usuario con el run ingresado")
    void shouldThrow_NotFound() {
        when(repo.findByRun("11111111-1")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> service.buscarPorRun("11111111-1")
        );

        assertNotNull(exception);
        assertEquals("No se encuentra el usuario con el run: 1111111-1", exception.getMessage());
        
        verify(repo, times(1)).findByRun("1111111-1");
    }
    
}
