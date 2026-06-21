package com.pharmanet.usuario_service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pharmanet.usuario_service.dto.UsuarioDTO;
import com.pharmanet.usuario_service.service.UsuarioService;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService service;

    private UsuarioDTO dto;

    private Page<UsuarioDTO> page;

    @BeforeEach
    void SetUp() {
        dto = new UsuarioDTO();
        dto.setRun("11111111-1");
        dto.setCodSucursal("SU0001");
    }

    // ========================================
    // GET
    // ========================================

    @Test
    @DisplayName("Mostrar todos los usuarios")
    void shouldReturnPageMostrarTodos() throws Exception {
        when(service.mostrarTodos(any(Pageable.class)))
            .thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/usuarios"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Buscar por run")
    void shouldReturnUsuarioBuscarPorRun() throws Exception {
        when(service.buscarPorRun("11111111-1"))
            .thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/usuarios/{run}", "11111111-1"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    // ========================================
    // DELETE
    // ========================================

    @Test
    @DisplayName("Eliminar usuario")
    void shouldEliminarUsuarioPorRun() throws Exception {
        when(service.buscarPorRun("11111111-1"))
            .thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/usuarios/{run}", "11111111-1"))
            .andExpect(status().isNoContent());
    }
}
