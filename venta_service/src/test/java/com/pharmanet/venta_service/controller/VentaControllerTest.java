package com.pharmanet.venta_service.controller;

import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.pharmanet.venta_service.dto.RegistroVenta;
import com.pharmanet.venta_service.dto.VentaDto;
import com.pharmanet.venta_service.service.VentaService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VentaController.class)
public class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private VentaService service;

    private VentaDto dto;

    private RegistroVenta registro;

    private Page<VentaDto> page;

    @BeforeEach
    void setUp() {
        dto = new VentaDto();
        dto.setCodVenta(1L);
        dto.setFechaVenta(LocalDate.now());

        registro = new RegistroVenta();
        registro.setCodVenta(1L);
        registro.setFechaVenta(LocalDate.now());
        registro.setProductos(Map.of("PR0001", 2));
        
        page = Page.empty();
    }

    // ====================================
    // HTTP METHOD GET
    // ====================================

    @Test
    @DisplayName("GET: Busca una venta por el código")
    public void cuandoBuscarVenta_EntoncesEstados200() throws Exception {

        when(service.buscarPorCodVenta(1L)).thenReturn(registro);
        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/ventas/{codVenta}", 1L))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldReturnPageableFindAll() throws Exception {
        when(service.mostrarTodos(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/ventas"))
            .andExpect(status().isOk());
    }

    // ====================================
    // HTTP METHOD POST
    // ====================================

    @Test
    @DisplayName("POST: Agregar una venta")
    public void cuandoAgregarVenta_EntoncesEstados201() throws Exception {

        when(service.agregarVenta(any(RegistroVenta.class))).thenReturn(registro);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/ventas")
            .content(("{ \"codVenta\" : 1, \"codSucursal\" : \"SU0001\", \"run\" : \"19941476-3\", \"productos\": {\n" + //
                                "                    \"PR0001\": 2,\n" + //
                                "                    \"PR0002\": 5\n" + //
                                "                }, \"fechaVenta\" : \"2026-06-20\", \"montoTotal\" : 4000}"))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
    }

    // ====================================
    // HTTP METHOD PUT
    // ====================================

    @Test
    @DisplayName("PUT: Actualiza una venta")
    public void cuandoActualizarVenta_EntoncesEstados204() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/ventas")
            .content(("{ \"codVenta\" : 1, \"codSucursal\" : \"SU0001\", \"run\" : \"19941476-3\", \"fechaVenta\" : \"2026-06-20\", \"montoTotal\" : 4000}"))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
    }

    // ====================================
    // HTTP METHOD DELETE
    // ====================================

    @Test
    @DisplayName("DELETE: Elimina una venta por el código")
    public void cuandoEliminarProducto_EntoncesEstados204() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/ventas/{codVenta}", 1L))
        .andExpect(status().isNoContent());
    }
}
