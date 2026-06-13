package com.pharmanet.producto_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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

import com.pharmanet.producto_service.dto.ProductoDto;
import com.pharmanet.producto_service.entity.ClaseReceta;
import com.pharmanet.producto_service.entity.Producto;
import com.pharmanet.producto_service.exception.ResourceAlreadyExistsException;
import com.pharmanet.producto_service.exception.ResourceNotFoundException;
import com.pharmanet.producto_service.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private ProductoRepository repo;

    @InjectMocks
    private ProductoService service;

    // ===========================================
    // SAVE
    // ===========================================

    @Test
    @DisplayName("Agrega un producto exitosamente")
    void save_HappyPath() {
        Producto entidad = new Producto();
        entidad.setSku("PR0001");

        ProductoDto dto = new ProductoDto();
        dto.setSku("PR0001");

        when(repo.findBySku("PR0001")).thenReturn(Optional.empty());
        when(repo.save(any(Producto.class))).thenReturn(entidad);

        ProductoDto resultado = service.agregarProducto(dto);

        assertNotNull(resultado);
        assertEquals(dto.getSku(), resultado.getSku());

        verify(repo).findBySku("PR0001");
        verify(repo).save(any(Producto.class));
    }

    @Test
    @DisplayName("El producto ya está registrado")
    void save_AlreadyExists() {
        Producto entidad = new Producto();
        entidad.setSku("PR0001");

        ProductoDto dto = new ProductoDto();
        dto.setSku("PR0001");

        when(repo.findBySku("PR0001")).thenReturn(Optional.of(entidad));

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class, () -> service.agregarProducto(dto));

        assertNotNull(exception);
        assertEquals("Ya existe un producto con el sku: PR0001", exception.getMessage());

        verify(repo).findBySku("PR0001");
    }

    @Test
    @DisplayName("El sku tiene el formato exacto")
    void procesarSku_NormalCase() {
        String sku = "PR0001";

        String resultado = service.procesarSku(sku);

        assertNotNull(resultado);
        assertEquals(sku, resultado);
    }

    @Test
    @DisplayName("El sku ingresado es menor a 8 carácteres y no cumple el formato")
    void procesarSku_LimitCase() {
        String sku = "00000001";

        String resultado = service.procesarSku(sku);

        assertNotNull(resultado);
        assertEquals("PR00000001", resultado);
    }

    @Test
    @DisplayName("El código ingresado no es válido")
    void procesarSku_BadCase() {
        String sku = "000000001";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.procesarSku(sku));

        assertNotNull(exception);
        assertEquals("Sku inválido. Ingrese con un sku menor o igual a 8 carácteres o que empiece 'PR'", exception.getMessage());
    }

    // ===========================================
    // BUSCAR POR SKU
    // ===========================================

    @Test
    @DisplayName("Devuelve un producto registrado")
    void shouldReturnProducto_NormalCase() {
        Producto entidad = new Producto();
        entidad.setSku("PR0001");

        when(repo.findBySku("PR0001")).thenReturn(Optional.of(entidad));

        ProductoDto resultado = service.buscarPorSku("PR0001");

        assertNotNull(resultado);
        assertEquals("PR0001", resultado.getSku());

        verify(repo).findBySku("PR0001");
    }

    @Test
    @DisplayName("No encuentra el producto")
    void shouldThrow_NotFound() {
        when(repo.findBySku("PR0001")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> service.buscarPorSku("PR0001"));

        assertNotNull(exception);
        assertEquals("No se encuentra el producto con el sku: PR0001", exception.getMessage());

        verify(repo).findBySku("PR0001");
    }

    // ===========================================
    // BUSCAR POR PRINCIPIO ACTIVO
    // ===========================================

    @Test
    @DisplayName("Retorna una lista de pageables que contienen parte o todo el texto en el principio activo")
    void shouldReturnPageablePrincipioActivo() {

        Producto entidad = new Producto();
        entidad.setPrincipioActivo("Amoxicilina");

        Pageable pageable = PageRequest.of(0, 10);

        List<Producto> lista = List.of(entidad);

        Page<Producto> page = new PageImpl<>(lista, pageable, lista.size());

        when(repo.findByPrincipioActivoContainingIgnoreCase("ina", pageable)).thenReturn(page);

        Page<ProductoDto> resultado = service.buscarPorPrincipioActivo("ina", pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("Amoxicilina", resultado.getContent().get(0).getPrincipioActivo());

        verify(repo).findByPrincipioActivoContainingIgnoreCase("ina", pageable);
    }

    // ===========================================
    // BUSCAR POR PRECIO VENTA
    // ===========================================

    @Test
    @DisplayName("Retorna una lista de pageables que esta entre un mínimo y un máximo de precios")
    void precioVenta_NormalCase() {

        Producto entidad = new Producto();
        entidad.setPrecioVenta(new BigDecimal(7000));

        Pageable pageable = PageRequest.of(0, 10);

        List<Producto> lista = List.of(entidad);

        Page<Producto> page = new PageImpl<>(lista, pageable, lista.size());

        when(repo.findByPrecioVentaBetween(5000, 8000, pageable)).thenReturn(page);

        Page<ProductoDto> resultado = service.buscarPorPrecioVenta(5000, 8000, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(new BigDecimal(7000), resultado.getContent().get(0).getPrecioVenta());

        verify(repo).findByPrecioVentaBetween(5000, 8000, pageable);
    }

    @Test
    @DisplayName("Del método entre precios el mínimo excede al máximo")
    void prrecioVenta_BadCase() {

        Pageable pageable = PageRequest.of(0, 10);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> service.buscarPorPrecioVenta(4000, 2000, pageable)
        );

        assertNotNull(exception);
        assertEquals("El mínimo excede al máximo", exception.getMessage());
    }

    // ===========================================
    // MOSTRAR TODOS
    // ===========================================

    @Test
    @DisplayName("Devuelve un pageable de todos los productos")
    void shouldReturnAllProducts() {

        Producto entidad = new Producto();
        entidad.setSku("SU0001");

        Pageable pageable = PageRequest.of(0, 10);

        List<Producto> lista = List.of(entidad);

        Page<Producto> page = new PageImpl<>(lista, pageable, lista.size());

        when(repo.findAll(pageable)).thenReturn(page);

        Page<ProductoDto> resultado = service.mostrarTodos(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("SU0001", resultado.getContent().get(0).getSku());

        verify(repo).findAll(pageable);
    }

    // ===========================================
    // UPDATE
    // ===========================================

    @Test
    @DisplayName("Actualiza un producto exitosamente")
    void update_NormalCase() {
        Producto entidad = new Producto();
        entidad.setSku("PR0001");

        ProductoDto dto = new ProductoDto();
        dto.setSku("PR0001");

        when(repo.findBySku("PR0001")).thenReturn(Optional.of(entidad));
        when(repo.save(entidad)).thenReturn(null);

        service.actualizarProducto(dto);

        verify(repo).findBySku("PR0001");
        verify(repo).save(entidad);
    }

    @Test
    @DisplayName("No se encuentra el producto")
    void update_NotFound() {
        ProductoDto dto = new ProductoDto();
        dto.setSku("PR0001");

        when(repo.findBySku("PR0001")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> service.actualizarProducto(dto)
        );

        assertNotNull(exception);
        assertEquals("No se encuentra el producto con el sku: PR0001", exception.getMessage());

        verify(repo).findBySku("PR0001");
    }

    // ===========================================
    // DELETE
    // ===========================================

    @Test
    @DisplayName("Elimina un producto exitosamente")
    void delete_NormalCase() {
        Producto entidad = new Producto();
        entidad.setSku("PR0001");

        when(repo.findBySku("PR0001")).thenReturn(Optional.of(entidad));

        service.eliminarProducto("PR0001");

        verify(repo).findBySku("PR0001");
        verify(repo).delete(entidad);
    }

    @Test
    @DisplayName("No se encuentra el producto")
    void delete_NotFound() {

        when(repo.findBySku("PR0001")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> service.eliminarProducto("PR0001")
        );

        assertNotNull(exception);
        assertEquals("No se encuentra el producto con el sku: PR0001", exception.getMessage());

        verify(repo).findBySku("PR0001");
    }

    // ===========================================
    // OBTENER RECETA
    // ===========================================

    @Test
    @DisplayName("Obtiene la clase de receta de SIN_RECETA")
    void shouldReturnReceta_SIN_RECETA() {

        Producto entidad = new Producto();
        entidad.setSku("PR0001");
        entidad.setReceta(ClaseReceta.SIN_RECETA);

        List<String> lista = List.of("PR0001");

        when(repo.findBySku("PR0001")).thenReturn(Optional.of(entidad));

        String resultado = service.obtenerReceta(lista);

        assertNotNull(resultado);
        assertEquals("SIN_RECETA", resultado);

        verify(repo).findBySku("PR0001");
        
    }

    @Test
    @DisplayName("Obtiene la clase de receta de RECETA_PRESENTADA")
    void shouldReturnReceta_RECETA_PRESENTADA() {

        Producto entidad = new Producto();
        entidad.setSku("PR0001");
        entidad.setReceta(ClaseReceta.RECETA_PRESENTADA);

        List<String> lista = List.of("PR0001");

        when(repo.findBySku("PR0001")).thenReturn(Optional.of(entidad));

        String resultado = service.obtenerReceta(lista);

        assertNotNull(resultado);
        assertEquals("RECETA_PRESENTADA", resultado);

        verify(repo).findBySku("PR0001");
        
    }

    @Test
    @DisplayName("Obtiene la clase de receta de RECETA_RETENIDA")
    void shouldReturnReceta_RECETA_RETENIDA() {

        Producto entidad = new Producto();
        entidad.setSku("PR0001");
        entidad.setReceta(ClaseReceta.RECETA_RETENIDA);

        List<String> lista = List.of("PR0001");

        when(repo.findBySku("PR0001")).thenReturn(Optional.of(entidad));

        String resultado = service.obtenerReceta(lista);

        assertNotNull(resultado);
        assertEquals("RECETA_RETENIDA", resultado);

        verify(repo).findBySku("PR0001");
        
    }

    @Test
    @DisplayName("No encuentra el producto ingresado")
    void shouldReturnReceta_NotFound() {

        List<String> lista = List.of("PR0001");

        when(repo.findBySku("PR0001")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.obtenerReceta(lista));

        assertNotNull(exception);
        assertEquals("No se encuentra el producto con el sku: PR0001", exception.getMessage());

        verify(repo).findBySku("PR0001");
        
    }

    // ===========================================
    // CALCULAR PRECIO TOTAL
    // ===========================================

    @Test
    @DisplayName("Obtiene el precio total de una venta")
    void shouldReturnTotal_NormalCase() {
        Producto entidad = new Producto();
        entidad.setSku("PR0001");
        entidad.setPrecioVenta(new BigDecimal(12000));

        Map<String, Integer> map = Map.of("PR0001", 1);

        when(repo.findBySku("PR0001")).thenReturn(Optional.of(entidad));

        BigDecimal resultado = service.calcularPrecioVentaTotal(map);

        assertNotNull(resultado);
        assertEquals(new BigDecimal(12000), resultado);

        verify(repo).findBySku("PR0001");
    }

    @Test
    @DisplayName("Obtiene el precio total de una venta de varios productos")
    void shouldReturnTotal_LimitCase() {
        Producto entidad1 = new Producto();
        entidad1.setSku("PR0001");
        entidad1.setPrecioVenta(new BigDecimal(12000));

        Producto entidad2 = new Producto();
        entidad2.setSku("PR0002");
        entidad2.setPrecioVenta(new BigDecimal(6000));

        Map<String, Integer> map = Map.of("PR0001", 5, "PR0002", 4);

        when(repo.findBySku("PR0001")).thenReturn(Optional.of(entidad1));
        when(repo.findBySku("PR0002")).thenReturn(Optional.of(entidad2));

        BigDecimal resultado = service.calcularPrecioVentaTotal(map);

        assertNotNull(resultado);
        assertEquals(new BigDecimal(84000), resultado);

        verify(repo).findBySku("PR0001");
    }

    @Test
    @DisplayName("No encuentra el producto")
    void shouldReturnTotal_NotFound() {

        Map<String, Integer> map = Map.of("PR0001", 1);

        when(repo.findBySku("PR0001")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.calcularPrecioVentaTotal(map));

        assertNotNull(exception);
        assertEquals("No se encuentra el producto con el sku: PR0001", exception.getMessage());

        verify(repo).findBySku("PR0001");
    }
    
}
