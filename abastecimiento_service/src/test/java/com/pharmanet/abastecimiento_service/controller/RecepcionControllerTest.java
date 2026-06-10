package com.pharmanet.abastecimiento_service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.pharmanet.abastecimiento_service.dto.recepcion.RecepcionResponse;
import com.pharmanet.abastecimiento_service.exception.GlobalExceptionHandler;
import com.pharmanet.abastecimiento_service.exception.ResourceNotFoundException;
import com.pharmanet.abastecimiento_service.service.RecepcionService;

@WebMvcTest(RecepcionController.class)
@Import(GlobalExceptionHandler.class)
public class RecepcionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecepcionService recepServ;

    @Test
    @DisplayName("Deberia retornar una recepcion cuando existe por ID y sucursal")
    void deberiaRetornarRecepcion_cuandoExistePorIdYSucursal() throws Exception {
        // GIVEN
        Long id = 50L;
        String codSucursal = "SU0001";
        
        RecepcionResponse response = new RecepcionResponse();
        response.setId(id);
        response.setNumeroDocumento("1234");

        when(recepServ.buscarPorId(id, codSucursal)).thenReturn(response);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/recepciones/sucursales/{codSucursal}/{id}", codSucursal, id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(50L))
                .andExpect(jsonPath("$.numeroDocumento").value("1234"));
    }

    @Test
    @DisplayName("Deberia retornar una pagina de recepciones por sucursal")
    void deberiaRetornarPaginaRecepciones_porSucursal() throws Exception {
        // GIVEN
        String codSucursal = "SU0001";
        RecepcionResponse response = new RecepcionResponse();
        response.setId(100L);
        Page<RecepcionResponse> pagina = new PageImpl<>(List.of(response));

        when(recepServ.buscarRecepcionPorSucursal(eq(codSucursal), any(Pageable.class))).thenReturn(pagina);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/recepciones/sucursales/{codSucursal}", codSucursal)
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(100L));
    }

    @Test
    @DisplayName("Deberia filtrar recepciones por rango de fechas")
    void deberiaFiltrarPorFechas() throws Exception {
        // GIVEN
        String codSucursal = "SU0001";
        RecepcionResponse response = new RecepcionResponse();
        response.setId(101L);
        Page<RecepcionResponse> pagina = new PageImpl<>(List.of(response));

        when(recepServ.buscarPorFecha(eq(codSucursal), any(), any(), any(Pageable.class))).thenReturn(pagina);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/recepciones/sucursales/{codSucursal}/fechas", codSucursal)
                .param("inicio", "2026-01-01")
                .param("fin", "2026-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(101L));
    }

    @Test
    @DisplayName("Deberia buscar recepciones por el RUN del usuario")
    void deberiaBuscarPorRunUsuario() throws Exception {
        // GIVEN
        String codSucursal = "SU0001";
        String runUsuario = "11222333-4";
        RecepcionResponse response = new RecepcionResponse();
        response.setId(102L);
        Page<RecepcionResponse> pagina = new PageImpl<>(List.of(response));

        when(recepServ.buscarPorUsuario(eq(runUsuario), eq(codSucursal), any(Pageable.class))).thenReturn(pagina);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/recepciones/sucursales/{codSucursal}/usuarios", codSucursal)
                .param("runUsuario", runUsuario))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(102L));
    }

    @Test
    @DisplayName("Deberia retornar no content al cancelar una recepcion")
    void deberiaRetornarNoContent_cuandoSeCancelaRecepcion() throws Exception {
        // GIVEN
        Long id = 1L;
        String codSucursal = "SU0001";

        doNothing().when(recepServ).cancelarRecepcionPorId(id, codSucursal);

        // WHEN & THEN
        mockMvc.perform(put("/api/v1/recepciones/sucursales/{codSucursal}/{id}/cancelar", codSucursal, id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deberia retornar no content al eliminar una recepcion")
    void deberiaRetornarNoContent_cuandoSeEliminaRecepcion() throws Exception {
        // GIVEN
        Long id = 1L;
        String codSucursal = "SU0001";

        doNothing().when(recepServ).eliminarRecepcionPorId(id, codSucursal);

        // WHEN & THEN
        mockMvc.perform(delete("/api/v1/recepciones/sucursales/{codSucursal}/{id}", codSucursal, id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deberia retornar 404 cuando el recurso no existe")
    void deberiaRetornar404_cuandoResourceNotFoundException() throws Exception {
        // GIVEN
        Long id = 99L;
        String codSucursal = "SU0001";

        when(recepServ.buscarPorId(id, codSucursal))
                .thenThrow(new ResourceNotFoundException("Recepcion no encontrada"));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/recepciones/sucursales/{codSucursal}/{id}", codSucursal, id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Recepcion no encontrada"));
    }

}
