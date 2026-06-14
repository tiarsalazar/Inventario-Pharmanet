package com.pharmanet.venta_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pharmanet.venta_service.client.InventarioFeignClient;
import com.pharmanet.venta_service.client.ProductoFeignClient;
import com.pharmanet.venta_service.client.UsuarioFeignClient;
import com.pharmanet.venta_service.dto.RegistroVenta;
import com.pharmanet.venta_service.dto.connector.FeignClientResponse;
import com.pharmanet.venta_service.entity.DetalleVenta;
import com.pharmanet.venta_service.entity.Venta;
import com.pharmanet.venta_service.exception.ResourceAlreadyExistsException;
import com.pharmanet.venta_service.repository.DetalleVentaRepository;
import com.pharmanet.venta_service.repository.VentaRepository;
import com.pharmanet.venta_service.request.InventarioRequest;
import com.pharmanet.venta_service.request.UsuarioRequest;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
public class VentaServiceTest {

    @Mock
    private DetalleVentaRepository detalleRepo;

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private ProductoFeignClient productoFeign;

    @Mock
    private UsuarioFeignClient usuarioFeign;

    @Mock
    private InventarioFeignClient inventarioFeign;

    @InjectMocks
    private VentaService service;

    // ==========================================================
    // SAVE VENTA
    // ==========================================================

    @Test
    @DisplayName("Agrega un usuario exitosamente")
    void save_HappyPaths() {
        RegistroVenta dto = new RegistroVenta();
        dto.setCodVenta(1L);
        dto.setRun("1111111-1");
        dto.setCodSucursal("SU0001");
        dto.setFechaVenta(LocalDate.now());

        Map<String, Integer> productos = Map.of("PR0001", 1);
        dto.setProductos(productos);

        Venta venta = new Venta();
        venta.setCodVenta(1L);
        
        DetalleVenta detalle = new DetalleVenta();
        detalle.setSku("PR0001");
        detalle.setCantidad(1);

        List<DetalleVenta> lista = List.of(detalle);
        List<String> skus = List.of("PR0001");
        
        ResponseEntity<String> receta = new ResponseEntity<>("SIN_RECETA", HttpStatus.OK);
        ResponseEntity<BigDecimal> valor_total = new ResponseEntity<>(new BigDecimal(1000), HttpStatus.OK);

        FeignClientResponse response = new FeignClientResponse();
        response.setEstado(true);

        ResponseEntity<FeignClientResponse> valido = new ResponseEntity<>(response, HttpStatus.OK);

        when(ventaRepository.findByCodVenta(1L)).thenReturn(Optional.empty());
        when(productoFeign.obtenerReceta(skus)).thenReturn(receta);
        when(productoFeign.calcularPrecioVentaTotal(productos)).thenReturn(valor_total);
        when(usuarioFeign.validarUsuarioVenta(new UsuarioRequest("1111111-1", "SU0001", "SIN_RECETA"))).thenReturn(valido);
        when(inventarioFeign.procesarVenta(new InventarioRequest("SU0001", "1111111-1", lista))).thenReturn(valido);
        when(ventaRepository.save(venta)).thenReturn(venta);
        when(detalleRepo.save(detalle)).thenReturn(detalle);

        RegistroVenta resultado = service.agregarVenta(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getCodVenta());
        assertEquals(1, resultado.getProductos().size());
        assertEquals(1, resultado.getProductos().get("PR0001"));

        verify(ventaRepository).findByCodVenta(1L);
        verify(productoFeign).obtenerReceta(skus);
        verify(productoFeign).calcularPrecioVentaTotal(productos);
        verify(usuarioFeign).validarUsuarioVenta(new UsuarioRequest("1111111-1", "SU0001", "SIN_RECETA"));
        verify(inventarioFeign).procesarVenta(new InventarioRequest("SU0001", "1111111-1", lista));
        verify(ventaRepository).save(venta);
        verify(detalleRepo).save(detalle);

    }

    @Test
    void save_LimitCaseSinFecha() {
        RegistroVenta dto = new RegistroVenta();
        dto.setCodVenta(1L);
        dto.setRun("1111111-1");
        dto.setCodSucursal("SU0001");

        Map<String, Integer> productos = Map.of("PR0001", 1);
        dto.setProductos(productos);

        Venta venta = new Venta();
        venta.setCodVenta(1L);
        
        DetalleVenta detalle = new DetalleVenta();
        detalle.setSku("PR0001");
        detalle.setCantidad(1);

        List<DetalleVenta> lista = List.of(detalle);
        List<String> skus = List.of("PR0001");
        
        ResponseEntity<String> receta = new ResponseEntity<>("SIN_RECETA", HttpStatus.OK);
        ResponseEntity<BigDecimal> valor_total = new ResponseEntity<>(new BigDecimal(1000), HttpStatus.OK);

        FeignClientResponse response = new FeignClientResponse();
        response.setEstado(true);

        ResponseEntity<FeignClientResponse> valido = new ResponseEntity<>(response, HttpStatus.OK);

        when(ventaRepository.findByCodVenta(1L)).thenReturn(Optional.empty());
        when(productoFeign.obtenerReceta(skus)).thenReturn(receta);
        when(productoFeign.calcularPrecioVentaTotal(productos)).thenReturn(valor_total);
        when(usuarioFeign.validarUsuarioVenta(new UsuarioRequest("1111111-1", "SU0001", "SIN_RECETA"))).thenReturn(valido);
        when(inventarioFeign.procesarVenta(new InventarioRequest("SU0001", "1111111-1", lista))).thenReturn(valido);
        when(ventaRepository.save(venta)).thenReturn(venta);
        when(detalleRepo.save(detalle)).thenReturn(detalle);

        RegistroVenta resultado = service.agregarVenta(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getCodVenta());
        assertEquals(1, resultado.getProductos().size());
        assertEquals(1, resultado.getProductos().get("PR0001"));
        assertEquals(LocalDate.now(), resultado.getFechaVenta());

        verify(ventaRepository).findByCodVenta(1L);
        verify(productoFeign).obtenerReceta(skus);
        verify(productoFeign).calcularPrecioVentaTotal(productos);
        verify(usuarioFeign).validarUsuarioVenta(new UsuarioRequest("1111111-1", "SU0001", "SIN_RECETA"));
        verify(inventarioFeign).procesarVenta(new InventarioRequest("SU0001", "1111111-1", lista));
        verify(ventaRepository).save(venta);
        verify(detalleRepo).save(detalle);
    }

    @Test
    void save_ResourceAlreadyExists() {
        RegistroVenta dto = new RegistroVenta();
        dto.setCodVenta(1L);

        Venta venta = new Venta();
        venta.setCodVenta(1L);

        when(ventaRepository.findByCodVenta(1L)).thenReturn(Optional.of(venta));

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class, () -> service.agregarVenta(dto));

        assertNotNull(exception);
        assertEquals("Ya existe una venta con el código: 1", exception.getMessage());

        verify(ventaRepository).findByCodVenta(1L);
    }

    @Test
    void save_BadCaseFechaInvalida() {
        RegistroVenta dto = new RegistroVenta();
        dto.setCodVenta(1L);
        dto.setRun("1111111-1");
        dto.setCodSucursal("SU0001");
        dto.setFechaVenta(LocalDate.parse("01/01/2026"));

        Map<String, Integer> productos = Map.of("PR0001", 1);
        dto.setProductos(productos);

        Venta venta = new Venta();
        venta.setCodVenta(1L);

        when(ventaRepository.findByCodVenta(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.agregarVenta(dto));

        assertNotNull(exception);
        assertEquals("La fecha de venta no puede ser distinta a la fecha actual. Fecha ingresada : 01/01/2026. Fecha actual: " + LocalDate.now(), exception.getMessage());

        verify(ventaRepository).findByCodVenta(1L);
    }

    @Test
    void save_HappyPaths() {
        RegistroVenta dto = new RegistroVenta();
        dto.setCodVenta(1L);
        dto.setRun("1111111-1");
        dto.setCodSucursal("SU0001");
        dto.setFechaVenta(LocalDate.now());

        Map<String, Integer> productos = Map.of("PR0001", 1);
        dto.setProductos(productos);

        DetalleVenta detalle = new DetalleVenta();
        detalle.setSku("PR0001");
        detalle.setCantidad(1);

        List<DetalleVenta> lista = List.of(detalle);
        List<String> skus = List.of("PR0001");
        
        ResponseEntity<String> receta = new ResponseEntity<>("SIN_RECETA", HttpStatus.OK);
        ResponseEntity<BigDecimal> valor_total = new ResponseEntity<>(new BigDecimal(1000), HttpStatus.OK);

        FeignClientResponse response = new FeignClientResponse();
        response.setEstado(true);

        ResponseEntity<FeignClientResponse> valido = new ResponseEntity<>(response, HttpStatus.OK);

        when(ventaRepository.findByCodVenta(1L)).thenReturn(Optional.empty());

        FeignException exception = assertThrows(FeignException.class, () -> service.agregarVenta(dto));
        when(productoFeign.obtenerReceta(skus)).thenReturn(receta);

        RegistroVenta resultado = service.agregarVenta(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getCodVenta());
        assertEquals(1, resultado.getProductos().size());
        assertEquals(1, resultado.getProductos().get("PR0001"));

        verify(ventaRepository).findByCodVenta(1L);
        verify(productoFeign).obtenerReceta(skus);
        verify(detalleRepo).save(detalle);

    }
}
