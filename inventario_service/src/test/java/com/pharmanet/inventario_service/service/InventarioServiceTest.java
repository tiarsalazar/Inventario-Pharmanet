package com.pharmanet.inventario_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pharmanet.inventario_service.client.ProductoClient;
import com.pharmanet.inventario_service.client.SucursalClient;
import com.pharmanet.inventario_service.dto.inventario.InventarioResponse;
import com.pharmanet.inventario_service.entity.Inventario;
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
        verifyNoMoreInteractions(invRepo, mapper);
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
        verifyNoMoreInteractions(invRepo);
    }

}
