package com.pharmanet.ubicacion_service.controller;

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

import com.pharmanet.ubicacion_service.dto.RegionDto;
import com.pharmanet.ubicacion_service.service.RegionService;

@WebMvcTest(RegionController.class)
public class RegionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegionService service;

    private RegionDto dto;

    private Page<RegionDto> page;

    @BeforeEach
    void setUp() {
        dto = new RegionDto();
        dto.setCodRegion("5");
        dto.setDescripcion("Región de Valparaíso");
    }

    // ========================================
    // GET
    // ========================================

    @Test
    @DisplayName("Mostrar todas las regiones")
    void shouldReturnPageMostrarTodos() throws Exception {
        when(service.mostrarTodasRegiones(any(Pageable.class)))
            .thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/regiones"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Buscar por código de la región")
    void shouldReturnRegionPorCodigo() throws Exception {
        when(service.buscarRegion("5"))
            .thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/regiones/{codRegion}", "5"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    // ========================================
    // POST
    // ========================================

    @Test
    @DisplayName("Agregar una región")
    public void cuandoAgregarRegion_EntoncesEstados201() throws Exception {

        when(service.agregarRegion(any(RegionDto.class))).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/regiones")
            .content(("{ \"codRegion\" : \"RM\", \"descripcion\" : \"Región Metropolitana de Santiago\"}"))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
    }

    // ========================================
    // PUT
    // ========================================

    @Test
    @DisplayName("Actualizar una región")
    public void cuandoActualizarRegion_EntoncesEstados204() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/regiones")
            .content(("{ \"codRegion\" : \"RM\", \"descripcion\" : \"Región Metropolitana de Santiago\"}"))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
    }

    // ========================================
    // DELETE
    // ========================================

    @Test
    @DisplayName("Eliminar región")
    void shouldEliminarRegionPorCodigo() throws Exception {
        when(service.buscarRegion("5"))
            .thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/regiones/{codRegion}", "5"))
            .andExpect(status().isNoContent());
    }

}
