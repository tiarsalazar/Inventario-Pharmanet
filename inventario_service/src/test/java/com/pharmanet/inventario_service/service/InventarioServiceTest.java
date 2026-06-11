package com.pharmanet.inventario_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

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
import com.pharmanet.inventario_service.dto.movimiento.MovimientoResponse;
import com.pharmanet.inventario_service.entity.Inventario;
import com.pharmanet.inventario_service.entity.Movimiento;
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

}
