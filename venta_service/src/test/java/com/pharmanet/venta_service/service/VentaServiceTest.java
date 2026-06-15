package com.pharmanet.venta_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pharmanet.venta_service.client.InventarioFeignClient;
import com.pharmanet.venta_service.client.ProductoFeignClient;
import com.pharmanet.venta_service.client.UsuarioFeignClient;
import com.pharmanet.venta_service.dto.RegistroVenta;
import com.pharmanet.venta_service.dto.VentaDto;
import com.pharmanet.venta_service.dto.connector.FeignClientResponse;
import com.pharmanet.venta_service.entity.DetalleVenta;
import com.pharmanet.venta_service.entity.Venta;
import com.pharmanet.venta_service.exception.ResourceAlreadyExistsException;
import com.pharmanet.venta_service.exception.ResourceNotFoundException;
import com.pharmanet.venta_service.exception.VentaInvalida;
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
        dto.setMontoTotal(new BigDecimal(1000));

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
        ResponseEntity<BigDecimal> valorTotal = new ResponseEntity<>(new BigDecimal(1000), HttpStatus.OK);

        FeignClientResponse response = new FeignClientResponse();
        response.setEstado(true);

        ResponseEntity<FeignClientResponse> valido = new ResponseEntity<>(response, HttpStatus.OK);

        when(ventaRepository.findByCodVenta(1L)).thenReturn(Optional.empty());
        when(productoFeign.obtenerReceta(skus)).thenReturn(receta);
        when(usuarioFeign.validarUsuarioVenta(new UsuarioRequest("1111111-1", "SU0001", "SIN_RECETA"))).thenReturn(valido);
        when(inventarioFeign.procesarVenta(new InventarioRequest("SU0001", "1111111-1", lista))).thenReturn(valido);
        when(productoFeign.calcularPrecioVentaTotal(productos)).thenReturn(valorTotal);
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);
        when(detalleRepo.save(detalle)).thenReturn(detalle);

        RegistroVenta resultado = service.agregarVenta(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getCodVenta());
        assertEquals(1, resultado.getProductos().size());
        assertEquals(1, resultado.getProductos().get("PR0001"));

        verify(ventaRepository).findByCodVenta(1L);
        verify(productoFeign).obtenerReceta(skus);
        verify(usuarioFeign).validarUsuarioVenta(new UsuarioRequest("1111111-1", "SU0001", "SIN_RECETA"));
        verify(inventarioFeign).procesarVenta(new InventarioRequest("SU0001", "1111111-1", lista));
        verify(ventaRepository).save(any(Venta.class));
        verify(detalleRepo).save(detalle);
        verify(productoFeign).calcularPrecioVentaTotal(productos);
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
        ResponseEntity<BigDecimal> valorTotal = new ResponseEntity<>(new BigDecimal(1000), HttpStatus.OK);

        FeignClientResponse response = new FeignClientResponse();
        response.setEstado(true);

        ResponseEntity<FeignClientResponse> valido = new ResponseEntity<>(response, HttpStatus.OK);

        when(ventaRepository.findByCodVenta(1L)).thenReturn(Optional.empty());
        when(productoFeign.obtenerReceta(skus)).thenReturn(receta);
        when(usuarioFeign.validarUsuarioVenta(new UsuarioRequest("1111111-1", "SU0001", "SIN_RECETA"))).thenReturn(valido);
        when(inventarioFeign.procesarVenta(new InventarioRequest("SU0001", "1111111-1", lista))).thenReturn(valido);
        when(productoFeign.calcularPrecioVentaTotal(productos)).thenReturn(valorTotal);
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);
        when(detalleRepo.save(detalle)).thenReturn(detalle);

        RegistroVenta resultado = service.agregarVenta(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getCodVenta());
        assertEquals(1, resultado.getProductos().size());
        assertEquals(1, resultado.getProductos().get("PR0001"));
        assertEquals(LocalDate.now(), resultado.getFechaVenta());

        verify(ventaRepository).findByCodVenta(1L);
        verify(productoFeign).obtenerReceta(skus);
        verify(usuarioFeign).validarUsuarioVenta(new UsuarioRequest("1111111-1", "SU0001", "SIN_RECETA"));
        verify(inventarioFeign).procesarVenta(new InventarioRequest("SU0001", "1111111-1", lista));
        verify(ventaRepository).save(any(Venta.class));
        verify(detalleRepo).save(detalle);
        verify(productoFeign).calcularPrecioVentaTotal(productos);
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
    void save_RecetaBadCase() {
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

        List<String> skus = List.of("PR0001");

        when(ventaRepository.findByCodVenta(1L)).thenReturn(Optional.empty());
        when(productoFeign.obtenerReceta(skus)).thenThrow(FeignException.class);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.agregarVenta(dto));

        assertNotNull(exception);
        assertEquals("No se encuentra al menos uno de los productos ingresados", exception.getMessage());

        verify(ventaRepository).findByCodVenta(1L);
        verify(productoFeign).obtenerReceta(skus);
    }

    @Test
    void save_UsuarioNotFound() {
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

        List<String> skus = List.of("PR0001");
        
        ResponseEntity<String> receta = new ResponseEntity<>("SIN_RECETA", HttpStatus.OK);

        when(ventaRepository.findByCodVenta(1L)).thenReturn(Optional.empty());
        when(productoFeign.obtenerReceta(skus)).thenReturn(receta);
        when(usuarioFeign.validarUsuarioVenta(new UsuarioRequest("1111111-1", "SU0001", "SIN_RECETA"))).thenThrow(FeignException.class);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.agregarVenta(dto));

        assertNotNull(exception);
        assertEquals("No se encuentra el usuario con el run: 1111111-1", exception.getMessage());

        verify(ventaRepository).findByCodVenta(1L);
        verify(productoFeign).obtenerReceta(skus);
        verify(usuarioFeign).validarUsuarioVenta(new UsuarioRequest("1111111-1", "SU0001", "SIN_RECETA"));
    }

    @Test
    void save_UsuarioBadCase() {
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

        List<String> skus = List.of("PR0001");
        
        ResponseEntity<String> receta = new ResponseEntity<>("SIN_RECETA", HttpStatus.OK);

        FeignClientResponse response = new FeignClientResponse();
        response.setEstado(true);
        response.setMensaje("Venta inválida");

        ResponseEntity<FeignClientResponse> usuarioResponse = new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        when(ventaRepository.findByCodVenta(1L)).thenReturn(Optional.empty());
        when(productoFeign.obtenerReceta(skus)).thenReturn(receta);
        when(usuarioFeign.validarUsuarioVenta(new UsuarioRequest("1111111-1", "SU0001", "SIN_RECETA"))).thenReturn(usuarioResponse);

        VentaInvalida exception = assertThrows(VentaInvalida.class, () -> service.agregarVenta(dto));

        assertNotNull(exception);
        assertEquals("Venta inválida", exception.getMessage());

        verify(ventaRepository).findByCodVenta(1L);
        verify(productoFeign).obtenerReceta(skus);
        verify(usuarioFeign).validarUsuarioVenta(new UsuarioRequest("1111111-1", "SU0001", "SIN_RECETA"));
    }

    @Test
    void save_InventarioNotFound() {
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

        FeignClientResponse response = new FeignClientResponse();
        response.setEstado(true);

        ResponseEntity<FeignClientResponse> usuarioResponse = new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        when(ventaRepository.findByCodVenta(1L)).thenReturn(Optional.empty());
        when(productoFeign.obtenerReceta(skus)).thenReturn(receta);
        when(usuarioFeign.validarUsuarioVenta(new UsuarioRequest("1111111-1", "SU0001", "SIN_RECETA"))).thenReturn(usuarioResponse);
        when(inventarioFeign.procesarVenta(new InventarioRequest("SU0001", "1111111-1", lista))).thenThrow(FeignException.class);

        VentaInvalida exception = assertThrows(VentaInvalida.class, () -> service.agregarVenta(dto));

        assertNotNull(exception);
        assertEquals("No existe un inventario con el código de la sucursal: SU0001 o alguno de los productos no está disponible", exception.getMessage());

        verify(ventaRepository).findByCodVenta(1L);
        verify(productoFeign).obtenerReceta(skus);
        verify(usuarioFeign).validarUsuarioVenta(new UsuarioRequest("1111111-1", "SU0001", "SIN_RECETA"));
        verify(inventarioFeign).procesarVenta(new InventarioRequest("SU0001", "1111111-1", lista));
    }

    @Test
    void save_InventarioBadCase() {
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

        FeignClientResponse response = new FeignClientResponse();
        response.setEstado(true);

        FeignClientResponse error = new FeignClientResponse();
        error.setEstado(false);
        error.setMensaje("Venta inválida");

        ResponseEntity<FeignClientResponse> usuarioResponse = new ResponseEntity<>(response, HttpStatus.OK);
        ResponseEntity<FeignClientResponse> inventarioResponse = new ResponseEntity<>(response, HttpStatus.OK);

        when(ventaRepository.findByCodVenta(1L)).thenReturn(Optional.empty());
        when(productoFeign.obtenerReceta(skus)).thenReturn(receta);
        when(usuarioFeign.validarUsuarioVenta(new UsuarioRequest("1111111-1", "SU0001", "SIN_RECETA"))).thenReturn(usuarioResponse);
        when(inventarioFeign.procesarVenta(new InventarioRequest("SU0001", "1111111-1", lista))).thenReturn(inventarioResponse);
        VentaInvalida exception = assertThrows(VentaInvalida.class, () -> service.agregarVenta(dto));

        assertNotNull(exception);
        assertEquals("Venta inválida", exception.getMessage());

        verify(ventaRepository).findByCodVenta(1L);
        verify(productoFeign).obtenerReceta(skus);
        verify(usuarioFeign).validarUsuarioVenta(new UsuarioRequest("1111111-1", "SU0001", "SIN_RECETA"));
        verify(inventarioFeign).procesarVenta(new InventarioRequest("SU0001", "1111111-1", lista));
    }

    @Test
    void save_montoInvalido() {
        RegistroVenta dto = new RegistroVenta();
        dto.setCodVenta(1L);
        dto.setRun("1111111-1");
        dto.setCodSucursal("SU0001");
        dto.setFechaVenta(LocalDate.now());
        dto.setMontoTotal(new BigDecimal(1500));

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
        ResponseEntity<BigDecimal> total = new ResponseEntity<>(new BigDecimal(1000), HttpStatus.OK);

        FeignClientResponse response = new FeignClientResponse();
        response.setEstado(true);


        ResponseEntity<FeignClientResponse> valido = new ResponseEntity<>(response, HttpStatus.OK);

        when(ventaRepository.findByCodVenta(1L)).thenReturn(Optional.empty());
        when(productoFeign.obtenerReceta(skus)).thenReturn(receta);
        when(usuarioFeign.validarUsuarioVenta(new UsuarioRequest("1111111-1", "SU0001", "SIN_RECETA"))).thenReturn(valido);
        when(inventarioFeign.procesarVenta(new InventarioRequest("SU0001", "1111111-1", lista))).thenReturn(valido);
        when(productoFeign.calcularPrecioVentaTotal(productos)).thenReturn(total);

        VentaInvalida exception = assertThrows(VentaInvalida.class, () -> service.agregarVenta(dto));

        assertNotNull(exception);
        assertEquals("Monto ingresado distinto al monto calculado. Monto ingresado: $1500. Monto total: $1000", exception.getMessage());

        verify(ventaRepository).findByCodVenta(1L);
        verify(productoFeign).obtenerReceta(skus);
        verify(usuarioFeign).validarUsuarioVenta(new UsuarioRequest("1111111-1", "SU0001", "SIN_RECETA"));
        verify(inventarioFeign).procesarVenta(new InventarioRequest("SU0001", "1111111-1", lista));
        verify(productoFeign).calcularPrecioVentaTotal(productos);
    }

    // ==========================================================
    // CONVERTIR DETALLE VENTA
    // ==========================================================

    @Test
    @DisplayName("Agrega los detalles de venta con éxito")
    void saveDetalleVenta_HappyPath() {
        DetalleVenta entidad = new DetalleVenta();
        entidad.setCantidad(4);

        Map<String, Integer> mapa = Map.of("PR0001", 4, "PR0002", 4);

        when(detalleRepo.save(entidad)).thenReturn(entidad);

        List<DetalleVenta> resultado = service.convertirDetalleVenta(mapa);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(entidad.getCantidad(), resultado.get(0).getCantidad());

        verify(detalleRepo, times(2)).save(entidad);
    }

    @Test
    @DisplayName("La cantidad ingresada es menor o igual a 0")
    void saveDetalleVenta_BadCase() {
        Map<String, Integer> mapa = Map.of("PR0001", 0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.convertirDetalleVenta(mapa));

        assertNotNull(exception);
        assertEquals("La cantidad ingresada por producto no puede ser igual o inferior a 0", exception.getMessage());
    }

    // ==========================================================
    // MOSTRAR TODOS
    // ==========================================================

    @Test
    @DisplayName("Muestra todo los productos")
    void shouldReturnPageUsuario_WhenFindAll() {

        Pageable pageable = PageRequest.of(0, 10 );

        Venta entidad = new Venta();
        entidad.setCodVenta(1L);

        List<Venta> lista = List.of(entidad);

        Page<Venta> page = new PageImpl<>(lista, pageable, lista.size());

        when(ventaRepository.findAll(pageable)).thenReturn(page);

        Page<VentaDto> resultado = service.mostrarTodos(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(1L, resultado.getContent().get(0).getCodVenta());

        verify(ventaRepository).findAll(pageable);
    }

    // ==========================================================
    // BUSCAR POR CÓDIGO DE VENTA
    // ==========================================================

    @Test
    @DisplayName("Devuelve un registro de venta con éxito")
    void shouldReturnRegistroVenta_NormalCase() {
        Venta venta = new Venta();
        venta.setCodVenta(1L);

        DetalleVenta detalle = new DetalleVenta();
        detalle.setVenta(venta);
        detalle.setSku("PR0001");

        List<DetalleVenta> lista = List.of(detalle);

        when(ventaRepository.findByCodVenta(1L)).thenReturn(Optional.of(venta));
        when(detalleRepo.findByVenta_CodVenta(1L)).thenReturn(lista);

        RegistroVenta resultado = service.buscarPorCodVenta(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getCodVenta());
        assertEquals(1L, resultado.getProductos().size());

        verify(ventaRepository).findByCodVenta(1L);
        verify(detalleRepo).findByVenta_CodVenta(1L);
    }

    @Test
    @DisplayName("No se encuentra la venta con ese código")
    void shouldThrowRegistroVenta_NotFound() {
        when(ventaRepository.findByCodVenta(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.buscarPorCodVenta(1L));

        assertNotNull(exception);
        assertEquals("No se encuentra la venta con el código: 1", exception.getMessage());

        verify(ventaRepository).findByCodVenta(1L);
    }

    // ==========================================================
    // BUSCAR POR FECHAS
    // ==========================================================

    @Test
    @DisplayName("Devuelve los productos entre las fechas ingresadas con éxito")
    void shouldReturnPageEntreFechas_NormalCase()  {
        Pageable pageable = PageRequest.of(0, 10 );

        Venta entidad = new Venta();
        entidad.setCodVenta(1L);
        entidad.setFechaVenta(LocalDate.parse("1999-03-25"));

        List<Venta> lista = List.of(entidad);

        Page<Venta> page = new PageImpl<>(lista, pageable, lista.size());

        when(ventaRepository.findByFechaVentaBetween(LocalDate.parse("1998-01-01"), LocalDate.parse("2000-05-05"), pageable)).thenReturn(page);

        Page<VentaDto> resultado = service.buscarPorFechas(LocalDate.parse("1998-01-01"), LocalDate.parse("2000-05-05"), pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(1L, resultado.getContent().get(0).getCodVenta());

        verify(ventaRepository).findByFechaVentaBetween(LocalDate.parse("2025-12-30"), LocalDate.parse("2026-05-05"), pageable);
    }

    @Test
    @DisplayName("Devuelve los productos entre las fechas ingresadas con éxito")
    void shouldThrowEntreFechas_FechaInicioPosteriorTermino()  {
        Pageable pageable = PageRequest.of(0, 10 );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.buscarPorFechas(LocalDate.parse("2025-12-30"), LocalDate.parse("2024-05-05"), pageable));

        assertNotNull(exception);
        assertEquals("La fecha de inicio debe ser anterior a la fecha de término", exception.getMessage());
    }

    @Test
    @DisplayName("La fecha de inicio es anterior al 1 de enero de 1998")
    void shouldThrowEntreFechas_FechaInicioAnterior1998() {
        Pageable pageable = PageRequest.of(0, 10 );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.buscarPorFechas(LocalDate.parse("1997-12-31"), LocalDate.parse("2000-05-05"), pageable));

        assertNotNull(exception);
        assertEquals("No hay registros antes de la fecha 1998", exception.getMessage());
    }

    @Test
    @DisplayName("La fecha de termino es posterior a la actual")
    void shouldThrowEntreFechas_FechaTerminoPosteriorActual() {
        Pageable pageable = PageRequest.of(0, 10 );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.buscarPorFechas(LocalDate.parse("2026-01-31"), LocalDate.parse("2027-05-05"), pageable));

        assertNotNull(exception);
        assertEquals("La fecha de término no puede ser posterior a la actual", exception.getMessage());
    }

    // ==========================================================
    // BUSCAR POR DÍA
    // ==========================================================

    @Test
    @DisplayName("Devuelve las ventas realizadas en la fecha ingresada exitósamente")
    void shouldReturnPagePorDia_NormalCase() {
        Pageable pageable = PageRequest.of(0, 10 );

        Venta entidad = new Venta();
        entidad.setCodVenta(1L);
        entidad.setFechaVenta(LocalDate.parse("2026-03-25"));

        List<Venta> lista = List.of(entidad);

        Page<Venta> page = new PageImpl<>(lista, pageable, lista.size());

        when(ventaRepository.findByFechaVenta(LocalDate.parse("2026-03-25"), pageable)).thenReturn(page);

        Page<VentaDto> resultado = service.buscarPorDia(LocalDate.parse("2026-03-25"), pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(1L, resultado.getContent().get(0).getCodVenta());

        verify(ventaRepository).findByFechaVenta(LocalDate.parse("2026-03-25"), pageable);
    }

    @Test
    @DisplayName("La fecha no puede ser posterior a la actual")
    void shouldThrowPorDia_FechaPosteriorActual() {
        Pageable pageable = PageRequest.of(0, 10 );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.buscarPorDia(LocalDate.parse("2050-12-31"), pageable));

        assertNotNull(exception);
        assertEquals("La fecha ingresada no puede ser posterior a la actual", exception.getMessage());
    }

    @Test
    @DisplayName("La fecha ingresada es anterior a 1998")
    void shouldThrowPorDia_FechaAnterior1998() {
        Pageable pageable = PageRequest.of(0, 10 );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.buscarPorDia(LocalDate.parse("1997-12-31"), pageable));

        assertNotNull(exception);
        assertEquals("No hay registros antes de la fecha 1998", exception.getMessage());
    }

    // ==========================================================
    // UPDATE
    // ==========================================================

    @Test
    @DisplayName("Actualiza una venta con éxito")
    void update_HappyPath() {
        VentaDto dto = new VentaDto();
        dto.setCodVenta(1L);

        Venta entidad = new Venta();
        entidad.setCodVenta(1L);

        when(ventaRepository.findByCodVenta(1L)).thenReturn(Optional.of(entidad));
        when(ventaRepository.save(any(Venta.class))).thenReturn(null);

        service.actualizarVenta(dto);

        verify(ventaRepository).save(any(Venta.class));
    }

    @Test
    @DisplayName("No encuentra el recurso buscado")
    void update_NotFound() {
        VentaDto dto = new VentaDto();
        dto.setCodVenta(1L);

        when(ventaRepository.findByCodVenta(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.actualizarVenta(dto));

        assertNotNull(exception);
        assertEquals("No se encuentra la venta con el código: 1", exception.getMessage());
    }

    // ==========================================================
    // DELETE
    // ==========================================================

    @Test
    @DisplayName("Elimina un recurso con éxito")
    void delete_HappyPath() {
        Venta entidad = new Venta();
        entidad.setCodVenta(1L);

        when(ventaRepository.findByCodVenta(1L)).thenReturn(Optional.of(entidad));

        service.eliminarVenta(1L);

        verify(ventaRepository).findByCodVenta(1L);
        verify(ventaRepository).delete(entidad);
    }

    @Test
    @DisplayName("No encuentra el recurso")
    void delete_NotFound() {
        when(ventaRepository.findByCodVenta(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.eliminarVenta(1L));

        assertNotNull(exception);
        assertEquals("No se encuentra la venta con el código: 1", exception.getMessage());

        verify(ventaRepository).findByCodVenta(1L);
    }
}