package com.pharmanet.usuario_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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

import com.pharmanet.usuario_service.client.SucursalFeignClient;
import com.pharmanet.usuario_service.dto.UsuarioDTO;
import com.pharmanet.usuario_service.dto.connector.UsuarioRequest;
import com.pharmanet.usuario_service.dto.connector.UsuarioResponse;
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
    // SAVE
    // ================================== 

    @Test
    @DisplayName("Agrega un usuario exitosamente")
    void save_HappyPath() {
        Usuario entidad = new Usuario();
        entidad.setRun("11111111-1");
        entidad.setCodSucursal("SU0001");
        entidad.setProfesion("TEC FARMACIA");
        entidad.setCorreoInstitucional("xxxxx@pharmanet.cl");

        UsuarioDTO dto = new UsuarioDTO();
        dto.setRun("11111111-1");
        dto.setCodSucursal("SU0001");
        dto.setProfesion("TEC FARMACIA");
        dto.setCorreoInstitucional("xxxxx@pharmanet.cl");

        when(repo.findByRun("11111111-1")).thenReturn(Optional.empty());
        when(feign.buscarSucursal("SU0001")).thenReturn(null);
        when(repo.save(any(Usuario.class))).thenReturn(entidad);
        
        UsuarioDTO resultado = service.agregarUsuario(dto);

        assertNotNull(resultado);
        assertEquals(dto.getRun(), resultado.getRun());

        verify(repo).save(any(Usuario.class));
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
        assertEquals("Ya existe el usuario con el run: 11111111-1", exception.getMessage());

        verify(repo, times(1)).findByRun("11111111-1");
    }

    @Test
    @DisplayName("Error al conectar al ms sucursal: la sucursal ingresada no existe")
    void saveShouldThrow_FeignClientException() {

        UsuarioDTO dto = new UsuarioDTO();
        dto.setRun("11111111-1");
        dto.setCodSucursal("SU0001");
        dto.setProfesion("TEC FARMACIA");

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
        verify(feign, times(2)).buscarSucursal("SU0001");
        
    }

    // ==================================
    // BUSCAR POR RUN
    // ==================================

    @Test
    @DisplayName("Devuelve un usuario dto exitosamente")
    void shouldReturnUsuario_WhenExists() {
        Usuario entidad = new Usuario();
        entidad.setRun("11111111-1");
        entidad.setProfesion("TEC FARMACIA");

        UsuarioDTO dto = new UsuarioDTO();
        dto.setRun("11111111-1");
        dto.setProfesion("TEC FARMACIA");

        when(repo.findByRun("11111111-1")).thenReturn(Optional.of(entidad));

        UsuarioDTO resultado = service.buscarPorRun("11111111-1");

        assertNotNull(resultado);
        assertEquals(dto, resultado);
        
        verify(repo).findByRun("11111111-1");
    }

    @Test
    @DisplayName("No se encuentra el usuario con el run ingresado")
    void shouldThrow_NotFound() {
        when(repo.findByRun("11111111-1")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> service.buscarPorRun("11111111-1")
        );

        assertNotNull(exception);
        assertEquals("No se encuentra el usuario con el run: 11111111-1", exception.getMessage());
        
        verify(repo, times(1)).findByRun("11111111-1");
    }

    // ==================================
    // BUSCAR POR PROFESIÓN
    // ==================================

    @Test
    void shouldReturnPageUsuario_WhenFindByProfesion() {

        String prof = "ANALISTA QUIMICO";

        Pageable pageable = PageRequest.of(0, 10 );

        Usuario entidad = new Usuario();
        entidad.setProfesion(prof);

        UsuarioDTO dto = new UsuarioDTO();
        dto.setProfesion(prof);

        List<Usuario> lista = List.of(entidad);

        Page<Usuario> page = new PageImpl<>(lista, pageable, lista.size());

        when(repo.findByProfesion(prof, pageable)).thenReturn(page);

        Page<UsuarioDTO> resultado = service.buscarPorProfesion(prof, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(prof, resultado.getContent().get(0).getProfesion());

        verify(repo).findByProfesion(prof, pageable);
    }
    
    // ==================================
    // BUSCAR POR SUCURSAL
    // ==================================

    @Test
    void shouldReturnPageUsuario_WhenFindBySucursal() {

        Pageable pageable = PageRequest.of(0, 10 );

        Usuario entidad = new Usuario();
        entidad.setCodSucursal("SU0001");
        entidad.setProfesion("TEC FARMACIA");

        UsuarioDTO dto = new UsuarioDTO();
        dto.setCodSucursal("SU0001");
        dto.setProfesion("TEC FARMACIA");


        List<Usuario> lista = List.of(entidad);

        Page<Usuario> page = new PageImpl<>(lista, pageable, lista.size());

        when(repo.findByCodSucursal("SU0001", pageable)).thenReturn(page);

        Page<UsuarioDTO> resultado = service.buscarPorSucursal("SU0001", pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("SU0001", resultado.getContent().get(0).getCodSucursal());

        verify(repo).findByCodSucursal("SU0001", pageable);
    }

    // ==================================
    // MOSTRAR TODOS
    // ==================================

    @Test
    void shouldReturnPageUsuario_WhenFindAll() {

        String run = "11111111-1";

        Pageable pageable = PageRequest.of(0, 10 );

        Usuario entidad = new Usuario();
        entidad.setRun(run);
        entidad.setProfesion("TEC FARMACIA");

        UsuarioDTO dto = new UsuarioDTO();
        dto.setRun(run);

        List<Usuario> lista = List.of(entidad);

        Page<Usuario> page = new PageImpl<>(lista, pageable, lista.size());

        when(repo.findAll(pageable)).thenReturn(page);

        Page<UsuarioDTO> resultado = service.mostrarTodos(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(run, resultado.getContent().get(0).getRun());

        verify(repo).findAll(pageable);
    }

    // ==================================
    // UPDATE
    // ==================================

    @Test
    void update_NormalCase() {
        Usuario entidad = new Usuario();
        entidad.setRun("11111111-1");
        entidad.setProfesion("TEC FARMACIA");
        entidad.setCorreoInstitucional("xxxxx@pharmanet.cl");

        UsuarioDTO dto = new UsuarioDTO();
        dto.setRun("11111111-1");
        dto.setProfesion("TEC FARMACIA");
        dto.setCorreoInstitucional("xxxxx@pharmanet.cl");

        when(repo.findByRun("11111111-1")).thenReturn(Optional.of(entidad));
        when(repo.save(entidad)).thenReturn(entidad);

        service.actualizarUsuario(dto);

        verify(repo).findByRun("11111111-1");
        verify(repo).save(entidad);
    }

    @Test
    void update_NotFound() {

        UsuarioDTO dto = new UsuarioDTO();
        dto.setRun("11111111-1");

        when(repo.findByRun("11111111-1")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.actualizarUsuario(dto));

        assertNotNull(exception);
        assertEquals("No se encuentra el usuario con el run: 11111111-1", exception.getMessage());
        verify(repo).findByRun("11111111-1");
    }

    // ==================================
    // DELETE
    // ==================================

    @Test
    void delete_NormalCase() {
        Usuario entidad = new Usuario();
        entidad.setRun("11111111-1");

        when(repo.findByRun("11111111-1")).thenReturn(Optional.of(entidad));

        service.eliminarUsuario("11111111-1");

        verify(repo).findByRun("11111111-1");
        verify(repo).delete(entidad);
    }

    @Test
    void delete_NotFound() {

        when(repo.findByRun("11111111-1")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.eliminarUsuario("11111111-1"));

        assertNotNull(exception);
        assertEquals("No se encuentra el usuario con el run: 11111111-1", exception.getMessage());
        verify(repo).findByRun("11111111-1");
    }

    // ==================================
    // VALIDAR USUARIO
    // ==================================

    @Test
    void validarUsuario_NormalCase() {

        UsuarioRequest request = new UsuarioRequest();
        request.setRun("11111111-1");
        request.setCodSucursal("SU0001");
        request.setReceta("SIN_RECETA");

        Usuario entidad = new Usuario();
        entidad.setRun("11111111-1");
        entidad.setCodSucursal("SU0001");
        entidad.setProfesion("TEC FARMACIA");


        when(repo.findByRun("11111111-1")).thenReturn(Optional.of(entidad));

        UsuarioResponse resultado = service.validarUsuarioVenta(request);

        assertNotNull(entidad);
        assertEquals(true, resultado.isEstado());
        assertEquals("La venta del vendedor con el run: 11111111-1 es válida", resultado.getMensaje());

        verify(repo).findByRun("11111111-1");
    }

    @Test
    @DisplayName("Venta con receta")
    void validarUsuario_LimitCase() {

        UsuarioRequest request = new UsuarioRequest();
        request.setRun("11111111-1");
        request.setCodSucursal("SU0001");
        request.setReceta("RECETA_PRESENTADA");

        Usuario entidad = new Usuario();
        entidad.setRun("11111111-1");
        entidad.setCodSucursal("SU0001");
        entidad.setProfesion("ANALISTA QUIMICO");

        List<Usuario> lista = List.of(entidad);


        when(repo.findByRun("11111111-1")).thenReturn(Optional.of(entidad));
        when(repo.findByProfesionAndCodSucursal("ANALISTA QUIMICO", "SU0001")).thenReturn(lista);

        UsuarioResponse resultado = service.validarUsuarioVenta(request);

        assertNotNull(entidad);
        assertEquals(true, resultado.isEstado());
        assertEquals("La venta del vendedor con el run: 11111111-1 es válida", resultado.getMensaje());

        verify(repo).findByRun("11111111-1");
        verify(repo).findByProfesionAndCodSucursal("ANALISTA QUIMICO", "SU0001");
    }

    @Test
    @DisplayName("El vendedor no tiene el título profesional para vender el producto")
    void validarUsuario_BadCase_NoAutorizado() {

        UsuarioRequest request = new UsuarioRequest();
        request.setRun("11111111-1");
        request.setCodSucursal("SU0001");
        request.setReceta("RECETA_RETENIDA");

        Usuario entidad = new Usuario();
        entidad.setRun("11111111-1");
        entidad.setCodSucursal("SU0001");
        entidad.setProfesion("DEVOPS");


        when(repo.findByRun("11111111-1")).thenReturn(Optional.of(entidad));

        UsuarioResponse resultado = service.validarUsuarioVenta(request);

        assertNotNull(entidad);
        assertEquals(false, resultado.isEstado());
        assertEquals("El usuario con el run: 11111111-1 no está autorizado para vender el/los productos", resultado.getMensaje());

        verify(repo).findByRun("11111111-1");
    }

    @Test
    @DisplayName("El vendedor no se encuentra en la sucursal ingresada")
    void validarUsuario_BadCase_DistintaSucursal() {

        UsuarioRequest request = new UsuarioRequest();
        request.setRun("11111111-1");
        request.setCodSucursal("SU0001");
        request.setReceta("SIN_RECETA");

        Usuario entidad = new Usuario();
        entidad.setRun("11111111-1");
        entidad.setCodSucursal("SU0002");
        entidad.setProfesion("TEC FARMACIA");


        when(repo.findByRun("11111111-1")).thenReturn(Optional.of(entidad));

        UsuarioResponse resultado = service.validarUsuarioVenta(request);

        assertNotNull(entidad);
        assertEquals(false, resultado.isEstado());
        assertEquals("El usuario no se encuentra en la sucursal con el código: SU0001", resultado.getMensaje());

        verify(repo).findByRun("11111111-1");
    }

    @Test
    @DisplayName("No hay analistas químicos autorizados para vender productos que requieren receta")
    void validarUsuario_BadCase_SinAnalistaQuimico() {

        UsuarioRequest request = new UsuarioRequest();
        request.setRun("11111111-1");
        request.setCodSucursal("SU0001");
        request.setReceta("RECETA_PRESENTADA");

        Usuario entidad = new Usuario();
        entidad.setRun("11111111-1");
        entidad.setCodSucursal("SU0001");
        entidad.setProfesion("TEC FARMACIA");

        List<Usuario> lista = new ArrayList<>();


        when(repo.findByRun("11111111-1")).thenReturn(Optional.of(entidad));
        when(repo.findByProfesionAndCodSucursal("ANALISTA QUIMICO", "SU0001")).thenReturn(lista);

        UsuarioResponse resultado = service.validarUsuarioVenta(request);

        assertNotNull(entidad);
        assertEquals(false, resultado.isEstado());
        assertEquals("No hay ANALISTA QUÍMICO en la sucursal con el código: SU0001", resultado.getMensaje());

        verify(repo).findByRun("11111111-1");
        verify(repo).findByProfesionAndCodSucursal("ANALISTA QUIMICO", "SU0001");
    }
}
