package com.pharmanet.producto_service.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.pharmanet.producto_service.dto.ProductoDto;
import com.pharmanet.producto_service.service.ProductoService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

@WebMvcTest(ProductoController.class)
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

    private ProductoDto dto;

    @BeforeEach
    void setUp() {
        dto = new ProductoDto();
        dto.setSku("PR0001");
        dto.setPrecioVenta(new BigDecimal(10000));
    }

    // ====================================
    // HTTP METHOD GET
    // ====================================

    @Test
    @DisplayName("GET: Busca un producto por el sku")
    public void cuandoBuscarPorSku_EntoncesEstados200() throws Exception {
        when(productoService.buscarPorSku("PR0001"))
        .thenReturn(dto);
        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/productos/{sku}", "PR0001"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    // ====================================
    // HTTP METHOD PUT
    // ====================================

    @Test
    @DisplayName("PUT: Actualiza un producto")
    public void cuandoActualizarProducto_EntoncesEstados204() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/productos")
            .content(("{ \"sku\" : \"PR0001\", \"nombreComercial\" : \"Motrin\", \"principioActivo\" : \"Ibuprofeno\", \"precioVenta\" : 12000, \"receta\" : \"SIN_RECETA\", \"concentracion\" : \"200MG\"}"))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
    }

    // ====================================
    // HTTP METHOD DELETE
    // ====================================

    @Test
    @DisplayName("DELETE: Elimina un producto por el sku")
    public void cuandoEliminarProducto_EntoncesEstados204() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/productos/{sku}", "PR0001"))
        .andExpect(status().isNoContent());
    }
}
