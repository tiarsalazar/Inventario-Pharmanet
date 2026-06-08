package com.pharmanet.usuario_service.service;

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
import com.pharmanet.usuario_service.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test de UsuarioService")
public class UsuarioServiceTest {

    @Mock
    private SucursalFeignClient feign;

    @Mock
    private UsuarioRepository repo;

    @InjectMocks
    private UsuarioService service;

    // -----------------------------
    //       AGREGAR USUARIO
    // -----------------------------

    @Test
    @DisplayName("Agrega un usuario exitosamente")
    void save_HappyPath() {
        Usuario entidad = new Usuario();
        entidad.setRun("111111111-1");
        entidad.setCodSucursal("SU0001");

        UsuarioDTO dto = new UsuarioDTO();
        dto.setRun("111111111-1");
        dto.setCodSucursal("SU0001");

        when(repo.findByRun("111111111-1")).thenReturn(Optional.of(entidad));
        when(feign.buscarSucursal("SU0001")).thenReturn()
        

    }
}
