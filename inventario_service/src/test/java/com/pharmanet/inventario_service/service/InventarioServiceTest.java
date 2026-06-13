package com.pharmanet.inventario_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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

import com.pharmanet.inventario_service.client.ProductoClient;
import com.pharmanet.inventario_service.client.SucursalClient;
import com.pharmanet.inventario_service.dto.inventario.InventarioDetailResponse;
import com.pharmanet.inventario_service.dto.inventario.InventarioResponse;
import com.pharmanet.inventario_service.dto.lote.LoteResponse;
import com.pharmanet.inventario_service.dto.movimiento.MovimientoResponse;
import com.pharmanet.inventario_service.dto.recepcion.DetalleRecepcionRequest;
import com.pharmanet.inventario_service.dto.recepcion.RecepcionRequest;
import com.pharmanet.inventario_service.entity.Inventario;
import com.pharmanet.inventario_service.entity.Lote;
import com.pharmanet.inventario_service.entity.Movimiento;
import com.pharmanet.inventario_service.enums.EstadoLote;
import com.pharmanet.inventario_service.exception.BusinessException;
import com.pharmanet.inventario_service.exception.ResourceNotFoundException;
import com.pharmanet.inventario_service.mapper.InventarioMapper;
import com.pharmanet.inventario_service.repository.InventarioRepository;
import com.pharmanet.inventario_service.repository.LoteRepository;
import com.pharmanet.inventario_service.repository.MovimientoRepository;

@ExtendWith(MockitoExtension.class)
public class InventarioServiceTest {
    @Mock
    private InventarioMapper mapper;
    @Mock
    private InventarioRepository invRepo;
    @Mock
    private LoteRepository loteRepo;
    @Mock
    private MovimientoRepository movRepo;
    @Mock
    private ProductoClient productoClient;
    @Mock
    private SucursalClient sucursalClient;
    @InjectMocks
    private InventarioService invService;

    @Test
    @DisplayName("Deberia obtener un Inventario buscando por sku y codigo de sucursal con exito")
    void deberiaObtenerInventarioPorSkuYCodSucursalExitosamente(){
        //GIVEN 
        String sku = "PR0001";
        String codSucursal = "SU0001";

        Inventario inventario = new Inventario();
        inventario.setSku(sku);
        inventario.setCodSucursal(codSucursal);

        InventarioResponse response = new InventarioResponse();
        response.setSku(sku);
        response.setCodSucursal(codSucursal);

        when(invRepo.findBySkuAndCodSucursal(sku, codSucursal)).thenReturn(Optional.of(inventario));
        when(mapper.toInventarioResponse(inventario)).thenReturn(response);

        //WHEN
        InventarioResponse resultado = invService.obtenerInventarioPorSku(sku, codSucursal);

        //THEN
        assertNotNull(resultado);
        assertEquals(sku, resultado.getSku());
        verify(invRepo, times(1)).findBySkuAndCodSucursal(sku, codSucursal);
        verify(mapper, times(1)).toInventarioResponse(inventario);
    }

    @Test
    @DisplayName("Deberia devolver ResourceNotFoundException cuando el inventario buscado por sku no se encuentra.")
    void deberiaDevolverResourceNotFound_CuandoBusquedaNoExitosaPorSku(){
        //GIVEN 
        String sku = "PR0001";
        String codSucursal = "SU0001";

        when(invRepo.findBySkuAndCodSucursal(sku, codSucursal)).thenReturn(Optional.empty());

        //WHEN Y THEN
        assertThrows(ResourceNotFoundException.class, () -> invService.obtenerInventarioPorSku(sku, codSucursal));
        verify(invRepo, times(1)).findBySkuAndCodSucursal(sku, codSucursal);
    }

    @Test
    @DisplayName("Deberia obtener un Inventario con los detalles de sus lotes por sku y codigo sucursal")
    void deberiaObtenerInventarioConDetallesPorSkuYSucursalExitosamente(){
        // GIVEN
        String sku = "PR0001";
        String codSucursal = "SU0001";

        Inventario inventario = new Inventario();
        inventario.setSku(sku);
        inventario.setCodSucursal(codSucursal);

        InventarioDetailResponse response = new InventarioDetailResponse();
        response.setSku(sku);
        response.setCodSucursal(codSucursal);

        when(invRepo.findBySkuAndCodSucursal(sku, codSucursal)).thenReturn(Optional.of(inventario));
        when(mapper.toInventarioDetailResponse(inventario)).thenReturn(response);

        // WHEN
        InventarioDetailResponse resultado = invService.obtenerInventarioDetailPorSku(sku, codSucursal);

        // THEN
        assertNotNull(resultado);
        assertEquals(sku, resultado.getSku());
        verify(invRepo, times(1)).findBySkuAndCodSucursal(sku, codSucursal);
        verify(mapper, times(1)).toInventarioDetailResponse(inventario);
    }

    @Test
    @DisplayName("Deberia devolver ResourceNotFoundException cuando Inventario no es encontrado por sku y codigo sucursal")
    void deberiaDevolverResourceNotFound_cuandoInventarioNoEncontradoPorSkuYSucursal(){
        // GIVEN
        String sku = "PR0001";
        String codSucursal = "SU0001";

        when(invRepo.findBySkuAndCodSucursal(sku, codSucursal)).thenReturn(Optional.empty());

        // WHEN Y THEN
        assertThrows(ResourceNotFoundException.class, () -> invService.obtenerInventarioDetailPorSku(sku, codSucursal));
        verify(invRepo, times(1)).findBySkuAndCodSucursal(sku, codSucursal);
    }

    @Test
    @DisplayName("Deberia devolver una pagina de sucursales cuando se busca por codigo sucursal")
    void deberiaDevolverPaginaDeInventariosPorCodSucursalExitosamente(){
        // GIVEN
        String codSucursal = "SU0001";
        Pageable pageable = PageRequest.of(0, 10);

        Inventario inventario  = new Inventario();
        inventario.setCodSucursal(codSucursal);

        InventarioResponse response = new InventarioResponse();
        response.setCodSucursal(codSucursal);

        Page<Inventario> pagina = new PageImpl<>(List.of(inventario), pageable, 1);

        when(invRepo.findByCodSucursal(codSucursal, pageable)).thenReturn(pagina);
        when(mapper.toInventarioResponse(inventario)).thenReturn(response);

        // WHEN
        Page<InventarioResponse> resultado = invService.obtenerInventarioPorSucursal(codSucursal, pageable);

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.getNumberOfElements());
        assertEquals(codSucursal, resultado.getContent().getFirst().getCodSucursal());
        verify(invRepo, times(1)).findByCodSucursal(codSucursal, pageable);
        verify(mapper, times(1)).toInventarioResponse(inventario);
    }

    @Test
    @DisplayName("Deberia devolver una pagina vacia cuando no existen inventarios en sucursal")
    void deberiaDevolverPaginaVacia_CuandoNoExistenInventarios(){
        // GIVEN
        String codSucursal = "SU0001";
        Pageable pageable = PageRequest.of(0, 10);

        Page<Inventario> pagina = Page.empty(pageable);

        when(invRepo.findByCodSucursal(codSucursal, pageable)).thenReturn(pagina);

        // WHEN
        Page<InventarioResponse> resultado = invService.obtenerInventarioPorSucursal(codSucursal, pageable);
        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(invRepo).findByCodSucursal(codSucursal, pageable);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Deberia devolver una pagina de movimientos al buscar por sku y codSucursal exitosamente")
    void deberiaDevolverPaginaDeMovimientosBuscandoPorSkuYSucursalExitosamente(){
        String sku = "PR0001";
        String codSucursal = "SU0001";
        Pageable pageable = PageRequest.of(0, 10);

        Movimiento movimiento = new Movimiento();
        movimiento.setSku(sku);
        movimiento.setCodSucursal(codSucursal);

        MovimientoResponse response = new MovimientoResponse();
        response.setSku(sku);
        response.setCodSucursal(codSucursal);

        Page<Movimiento> pagina = new PageImpl<>(List.of(movimiento), pageable, 1);

        when(movRepo.findBySkuAndCodSucursal(sku, codSucursal, pageable)).thenReturn(pagina);
        when(mapper.toMovimientoResponse(movimiento)).thenReturn(response);

        // WHEN 
        Page<MovimientoResponse> resultado = invService.obtenerMovimientoPorSku(sku, codSucursal, pageable);

        //THEN
         assertNotNull(resultado);
         assertEquals(1, resultado.getNumberOfElements());
         assertEquals(sku, resultado.getContent().getFirst().getSku());
         verify(movRepo).findBySkuAndCodSucursal(sku, codSucursal, pageable);
         verify(mapper).toMovimientoResponse(movimiento);
    }

    @Test
    @DisplayName("Deberia devolver una pagina de movimientos al buscar por usuario y codSucursal exitosamente")
    void deberiaDevolverPaginaDeMovimientosBuscandoPorUsuarioYSucursalExitosamente(){
        String run = "11222333-4";
        String codSucursal = "SU0001";
        Pageable pageable = PageRequest.of(0, 10);

        Movimiento movimiento = new Movimiento();
        movimiento.setRunUsuario(run);
        movimiento.setCodSucursal(codSucursal);

        MovimientoResponse response = new MovimientoResponse();
        response.setRunUsuario(run);
        response.setCodSucursal(codSucursal);

        Page<Movimiento> pagina = new PageImpl<>(List.of(movimiento), pageable, 1);

        when(movRepo.findByRunUsuarioAndCodSucursal(run, codSucursal, pageable)).thenReturn(pagina);
        when(mapper.toMovimientoResponse(movimiento)).thenReturn(response);

        // WHEN 
        Page<MovimientoResponse> resultado = invService.obtenerMovimientoPorUsuario(run, codSucursal, pageable);

        //THEN
         assertNotNull(resultado);
         assertEquals(1, resultado.getNumberOfElements());
         assertEquals(run, resultado.getContent().getFirst().getRunUsuario());
         verify(movRepo).findByRunUsuarioAndCodSucursal(run, codSucursal, pageable);
         verify(mapper).toMovimientoResponse(movimiento);
    }

    @Test
    @DisplayName("Deberia obtener una pagina de movimientos al buscar por rango de fecha y Sucursal")
    void deberiaObtenerPaginaDeMovimientosBuscandoPorRangoDeFechas(){
        // GIVEN
        String codSucursal = "SU0001";
        LocalDate inicio = LocalDate.of(2025, 05, 01);
        LocalDate fin = LocalDate.of(2026, 05, 10);
        Pageable pageable = PageRequest.of(0, 10);

        Movimiento movimiento = new Movimiento();
        movimiento.setCodSucursal(codSucursal);

        MovimientoResponse response = new MovimientoResponse();
        response.setCodSucursal(codSucursal);

        Page<Movimiento> pagina = new PageImpl<>(List.of(movimiento), pageable, 1);

        when(movRepo.findByCodSucursalAndFechaBetween(codSucursal, inicio.atStartOfDay(), fin.atTime(LocalTime.MAX), pageable)).thenReturn(pagina);
        when(mapper.toMovimientoResponse(movimiento)).thenReturn(response);

        //WHEN
        Page<MovimientoResponse> resultado = invService.obtenerMovimientoPorFecha(codSucursal, inicio, fin, pageable);

        //THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.getNumberOfElements());
        assertEquals(codSucursal, resultado.getContent().getFirst().getCodSucursal());
        verify(movRepo).findByCodSucursalAndFechaBetween(codSucursal, inicio.atStartOfDay(), fin.atTime(LocalTime.MAX), pageable);
        verify(mapper).toMovimientoResponse(movimiento);
    }

    @Test
    @DisplayName("Deberia obtener una pagina de movimientos de la sucursal al buscar por codigo Sucursal")
    void deberiaobtenerPaginaDeMovimientosBuscandoPorCodSucursal(){
        // GIVEN
        String codSucursal = "SU0001";
        Pageable pageable = PageRequest.of(0, 10);

        Movimiento movimiento = new Movimiento();
        movimiento.setCodSucursal(codSucursal);

        MovimientoResponse response = new MovimientoResponse();
        response.setCodSucursal(codSucursal);

        Page<Movimiento> pagina = new PageImpl<>(List.of(movimiento), pageable, 1);

        when(movRepo.findByCodSucursal(codSucursal, pageable)).thenReturn(pagina);
        when(mapper.toMovimientoResponse(movimiento)).thenReturn(response);

        //WHEN
        Page<MovimientoResponse> resultado = invService.obtenerMovimientoPorSucursal(codSucursal, pageable);

        //THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.getNumberOfElements());
        assertEquals(codSucursal, resultado.getContent().getFirst().getCodSucursal());
        verify(movRepo).findByCodSucursal(codSucursal, pageable);
        verify(mapper).toMovimientoResponse(movimiento);
    }


    @Test
    @DisplayName("Deberia hacer un ingreso de stock exitosamente cuando Recepcion haga una peticion")
    void deberiaIngresarStock_CuandoRecepcionHagaUnaPeticionExitosamente(){
        String runUsuario = "11222333-4";
        String codSucursal = "SU0001";
        String sku = "PR0001";

        DetalleRecepcionRequest detalleRecepcionRequest = new DetalleRecepcionRequest();
        detalleRecepcionRequest.setSku(sku);
        detalleRecepcionRequest.setCodLote("LO0001");
        detalleRecepcionRequest.setFechaVencimiento(LocalDate.of(2030, 12, 31));
        detalleRecepcionRequest.setCantidad(10);

        RecepcionRequest recepcionRequest = new RecepcionRequest();
        recepcionRequest.setCodSucursal(codSucursal);
        recepcionRequest.setDetalles(List.of(detalleRecepcionRequest));

        Inventario inventario = new Inventario();
        inventario.setId(1L);
        inventario.setCodSucursal(codSucursal);
        inventario.setSku(sku);
        inventario.setStockTotal(0);
        inventario.setLotes(new ArrayList<>());

        Lote nuevoLote = new Lote();
        nuevoLote.setCantidad(10);
        nuevoLote.setCodLote("LO0001");
        nuevoLote.setFechaVencimiento(LocalDate.of(2030, 12, 31));
        nuevoLote.setEstado(EstadoLote.ACTIVO);

        Inventario inventarioPersistido = new Inventario();
        inventarioPersistido.setId(1L);
        inventarioPersistido.setCodSucursal(codSucursal);
        inventarioPersistido.setSku(sku);
        inventarioPersistido.setStockTotal(10);
        
        Lote lotePersistido = new Lote();
        lotePersistido.setCantidad(10);
        lotePersistido.setCodLote("LO0001");
        lotePersistido.setFechaVencimiento(LocalDate.of(2030, 12, 31));
        lotePersistido.setEstado(EstadoLote.ACTIVO);
        lotePersistido.setInventario(inventarioPersistido);

        inventarioPersistido.setLotes(List.of(lotePersistido));

        LoteResponse loteResponse = new LoteResponse();
        loteResponse.setCodLote("LO0001");

        doNothing().when(sucursalClient).buscarSucursal(codSucursal);
        doNothing().when(productoClient).buscarPorSku(sku);
        when(invRepo.findBySkuAndCodSucursal(sku, codSucursal)).thenReturn(Optional.of(inventario));
        when(mapper.toLoteEntity(detalleRecepcionRequest)).thenReturn(nuevoLote);
        when(invRepo.save(inventario)).thenReturn(inventarioPersistido);
        when(mapper.toLoteResponse(any(Lote.class))).thenReturn(loteResponse);

        //WHEN
        List<LoteResponse> resultado = invService.registrarRecepcion(recepcionRequest, runUsuario);

        //THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("LO0001", resultado.getFirst().getCodLote());
        verify(sucursalClient).buscarSucursal(codSucursal);
        verify(productoClient).buscarPorSku(sku);
        verify(invRepo).save(inventario);
        verify(movRepo).save(any(Movimiento.class));
    }

    // AGREGAR LOS CAMINOS TRISTES PARA EL INGRESO DE STOCK

    @Test
    @DisplayName("Deberia rebajar Stock de Inventario exitosamente cuando Venta envia una peticion")
    void deberiaRestarStockDeUnInventarioCuandoVentaEnviaPeticionExitosamente(){

    }

    @Test
    @DisplayName("Deberia cambiar exitosamente el estado de un lote")
    void deberiaCambiarElEstadoDeUnLoteExitosamente(){
        String sku = "SKU0001";
        String codSucursal = "SU0001";
        String codLote = "LOTE0001";
        EstadoLote nuevoEstado = EstadoLote.DEFECTUOSO;

        Inventario inventario = new Inventario();
        inventario.setStockTotal(10);

        Lote lote = new Lote();
        lote.setCodLote(codLote);
        lote.setEstado(EstadoLote.ACTIVO);
        lote.setInventario(inventario);

        when(loteRepo.findByCodLoteAndInventario_SkuAndInventario_CodSucursal(codLote, sku, codSucursal)).thenReturn(List.of(lote));
        when(invRepo.save(any(Inventario.class))).thenReturn(inventario);

        //WHEN
        invService.cambiarEstadoLote(sku, codSucursal, codLote, nuevoEstado);

        //THEN
        assertEquals(nuevoEstado, lote.getEstado());
        verify(loteRepo).findByCodLoteAndInventario_SkuAndInventario_CodSucursal(codLote, sku, codSucursal);
        verify(invRepo).save(inventario);
    }

    @Test
    @DisplayName("Deberia lanzar ResourceNotFoundException cuando lote a cambiar estado no existe en el inventario")
    void deberiaLanzarResourceNotFoundCuandoLoteNoExiste(){
        String sku = "SKU0001";
        String codSucursal = "SU0001";
        String codLote = "LOTE0001";

        when(loteRepo.findByCodLoteAndInventario_SkuAndInventario_CodSucursal(codLote, sku, codSucursal)).thenReturn(List.of());

        //WHEN Y THEN
        assertThrows(ResourceNotFoundException.class, () -> {
            invService.cambiarEstadoLote(sku, codSucursal, codLote, EstadoLote.DEFECTUOSO);});
        verify(loteRepo).findByCodLoteAndInventario_SkuAndInventario_CodSucursal(codLote, sku, codSucursal);
    }

    @Test
    @DisplayName("Deberia eliminar el inventario exitosamente cuando el stock actual es cero")
    void deberiaEliminarInventarioExitosamenteCuandoStockEsCero() {
        // GIVEN
        String sku = "PR0001";
        String codSucursal = "SU0001";

        Inventario inventario = new Inventario();
        inventario.setSku(sku);
        inventario.setCodSucursal(codSucursal);
        inventario.setStockTotal(0);

        when(invRepo.findBySkuAndCodSucursal(sku, codSucursal)).thenReturn(Optional.of(inventario));

        // WHEN
        invService.eliminarInventario(sku, codSucursal);

        // THEN
        verify(invRepo).delete(inventario); 
    }

    @Test
    @DisplayName("Deberia lanzar BusinessException al intentar eliminar un inventario que aun tiene stock")
    void deberiaLanzaExcepcionBussines_CuandoEliminasInventarioYTieneStock() {
        // GIVEN
        String sku = "PR0001";
        String codSucursal = "SU0001";

        Inventario inventario = new Inventario();
        inventario.setSku(sku);
        inventario.setCodSucursal(codSucursal);
        inventario.setStockTotal(5); 

        when(invRepo.findBySkuAndCodSucursal(sku, codSucursal)).thenReturn(Optional.of(inventario));

        // WHEN & THEN
        assertThrows(BusinessException.class, () -> {
        invService.eliminarInventario(sku, codSucursal);
        });

        verify(invRepo, never()).delete(any(Inventario.class));
    }
}
