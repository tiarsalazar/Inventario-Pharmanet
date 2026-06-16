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

import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

@WebMvcTest(ProductoController.class)
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductoService productoService;

    private ProductoDto dto;

    @BeforeEach
    void setUp() {
        dto = new ProductoDto();
        dto.setSku("PR0001");
        dto.setPrecioVenta(new BigDecimal("10000"));
    }

    // ====================================
    // HTTP METHOD POST
    // ====================================

    @Test
    @DisplayName("POST: Agrega un producto")
    public void cuandoAgregarProducto_EntoncesEstados201() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/productos")
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("POST: Obtener receta de mayor restricción")
    public void cuandoObtenerReceta_EntoncesEstado200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/productos/recetas")
            .content(("[\"PR0001\", \"PR0002\", \"PR0003\", \"PR0004\"]"))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("POST: Obtener precio total de la venta")
    public void cuandoCalcularPrecioVentaTotal_EntoncesEstados200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/productos/calcular-total")
            .content(("""
            {"PR0001" : 2, "PR0002": 1}
            """))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    // ====================================
    // HTTP METHOD GET
    // ====================================

    @Test
    @DisplayName("GET: Trae todos los productos")
    public void cuandoMostrarTodos_EntoncesEstados200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/productos"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

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

    @Test
    @DisplayName("GET: Busca productos por principio activo")
    public void cuandoBuscarPorPrincipioActivo_EntoncesEstados200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/productos/principio-activo")
            .param("activo", "ibuprofeno"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET: Busca productos por precio venta")
    public void cuandoBuscarPorPrecioVenta_EntoncesEstados200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/productos/precio-venta")
            .param("min", "500")
            .param("max", "40000"))
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
