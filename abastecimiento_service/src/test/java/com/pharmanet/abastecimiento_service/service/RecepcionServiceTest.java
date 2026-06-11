package com.pharmanet.abastecimiento_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.pharmanet.abastecimiento_service.client.InventarioClient;
import com.pharmanet.abastecimiento_service.client.UsuarioClient;
import com.pharmanet.abastecimiento_service.dto.recepcion.RecepcionRequest;
import com.pharmanet.abastecimiento_service.dto.recepcion.RecepcionResponse;
import com.pharmanet.abastecimiento_service.entity.DetalleRecepcion;
import com.pharmanet.abastecimiento_service.entity.Recepcion;
import com.pharmanet.abastecimiento_service.enums.EstadoRecepcion;
import com.pharmanet.abastecimiento_service.enums.TipoDocumento;
import com.pharmanet.abastecimiento_service.exception.BusinessException;
import com.pharmanet.abastecimiento_service.exception.ResourceAlreadyExistsException;
import com.pharmanet.abastecimiento_service.exception.ResourceNotFoundException;
import com.pharmanet.abastecimiento_service.exception.ServiceCommunicationException;
import com.pharmanet.abastecimiento_service.mapper.RecepcionMapper;
import com.pharmanet.abastecimiento_service.repository.RecepcionRepository;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
public class RecepcionServiceTest {
    @Mock
    private RecepcionRepository recepRepo;
    @Mock
    private RecepcionMapper recepMapper;
    @Mock
    private InventarioClient inventarioClient;
    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private RecepcionService recepServ;

    @Test
    @DisplayName("Deberia retornar la recepcion correcta si existe por ID y Sucursal")
    void deberiaRetornarRecepcion_cuandoExistePorIdYSucursal(){
        // GIVEN
        Long id = 1L;
        String codSucursal = "SU0001";

        Recepcion recepcion = new Recepcion();
        recepcion.setId(id);
        recepcion.setCodSucursal(codSucursal);

        RecepcionResponse response = new RecepcionResponse();
        response.setId(id);
        response.setCodSucursal(codSucursal);

        when(recepRepo.findByIdAndCodSucursal(id, codSucursal)).thenReturn(Optional.of(recepcion));
        when(recepMapper.toRecepcionResponse(recepcion)).thenReturn(response);

        // WHEN
        RecepcionResponse resultado = recepServ.buscarPorId(id, codSucursal);

        // THEN
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(recepRepo, times(1)).findByIdAndCodSucursal(id, codSucursal);
        verify(recepMapper, times(1)).toRecepcionResponse(recepcion);
        verifyNoMoreInteractions(recepRepo, recepMapper);
    }

    @Test
    @DisplayName("Deberia lanzar ResourceNotFoundException cuando la recepcion no exista por el ID y codSucursal.")
    void deberiaLanzarResourceNotFoundException_cuandoRecepcionNoExistePorId(){
        // GIVEN
        Long idInexistente = 2L;
        String codSucursal = "SU0001";

        when(recepRepo.findByIdAndCodSucursal(idInexistente, codSucursal)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> {recepServ.buscarPorId(idInexistente, codSucursal);
        });
        verify(recepRepo, times(1)).findByIdAndCodSucursal(idInexistente, codSucursal);
        verifyNoMoreInteractions(recepRepo);
    }

    @Test
    @DisplayName("Deberia retornar una pagina de recepciones al buscar por codigo de Sucursal")
    void deberiaRetornarPaginaDeRecepciones_cuandoSeBuscaPorSucursal(){
        // GIVEN
        String codSucursal = "SU0001";
        Pageable pageable = PageRequest.of(0, 10);

        Recepcion recepcion = new Recepcion();
        recepcion.setCodSucursal(codSucursal);

        RecepcionResponse response = new RecepcionResponse();
        response.setCodSucursal(codSucursal);

        List<Recepcion> lista = List.of(recepcion);
        Page<Recepcion> pagina = new PageImpl<>(lista, pageable, lista.size());

        when(recepRepo.findByCodSucursal(codSucursal, pageable)).thenReturn(pagina);
        when(recepMapper.toRecepcionResponse(recepcion)).thenReturn(response);

        // WHEN
        Page<RecepcionResponse> resultado = recepServ.buscarRecepcionPorSucursal(codSucursal, pageable);

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals(codSucursal, resultado.getContent().get(0).getCodSucursal());
        verify(recepRepo, times(1)).findByCodSucursal(codSucursal, pageable);
    }

    @Test
    @DisplayName("Debe retornar una pagina de recepciones cuando se busca por rango de fechas y codigo Sucursal")
    void deberiaRetornarPaginaDeRecepciones_cuandoSeBuscaPorRangoDeFechasYSucursal(){
        // GIVEN
        String codSucursal = "SU0001";
        LocalDate inicio = LocalDate.of(2025, 05, 01);
        LocalDate fin = LocalDate.of(2026, 05, 10);
        Pageable pageable = PageRequest.of(0, 10);

        Recepcion recepcion = new Recepcion();
        recepcion.setCodSucursal(codSucursal);

        RecepcionResponse response = new RecepcionResponse();
        response.setCodSucursal(codSucursal);

        List<Recepcion> lista = List.of(recepcion);
        Page<Recepcion> pagina = new PageImpl<>(lista, pageable, lista.size());

        when(recepRepo.findByCodSucursalAndFechaIngresoBetween(
            codSucursal, inicio.atStartOfDay(), fin.atTime(LocalTime.MAX), pageable)).thenReturn(pagina);
        when(recepMapper.toRecepcionResponse(recepcion)).thenReturn(response);

        // WHEN
        Page<RecepcionResponse> resultado = recepServ.buscarPorFecha(codSucursal, inicio, fin, pageable);

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals(codSucursal, resultado.getContent().get(0).getCodSucursal());
        verify(recepRepo, times(1)).findByCodSucursalAndFechaIngresoBetween(
            codSucursal, inicio.atStartOfDay(), fin.atTime(LocalTime.MAX), pageable);
    }

    @Test
    @DisplayName("Debe retornar una pagina de recepciones por RUN Usuario si existe y codigo Sucursal")
    void deberiaRetornarPaginaDeRecepciones_cuandoUsuarioExisteYBuscaPorSucursal(){
        // GIVEN
        String runUsuario = "11111111-1";
        String codSucursal = "SU0001";
        Pageable pageable = PageRequest.of(0, 10);

        doNothing().when(usuarioClient).buscarPorRun(runUsuario);

        Recepcion recepcion = new Recepcion();
        recepcion.setRunUsuario(runUsuario);
        recepcion.setCodSucursal(codSucursal);

        RecepcionResponse response = new RecepcionResponse();
        response.setRunUsuario(runUsuario);
        response.setCodSucursal(codSucursal);

        List<Recepcion> lista = List.of(recepcion);
        Page<Recepcion> pagina = new PageImpl<>(lista, pageable, lista.size());

        when(recepRepo.findByRunUsuarioAndCodSucursal(runUsuario, codSucursal, pageable))
            .thenReturn(pagina);
        when(recepMapper.toRecepcionResponse(recepcion)).thenReturn(response);

        // WHEN
        Page<RecepcionResponse> resultado = recepServ.buscarPorUsuario(runUsuario, codSucursal, pageable);

        // THEN
        assertNotNull(resultado);
        assertNotNull(resultado.getContent());
        assertEquals(1, resultado.getContent().size());
        assertEquals(codSucursal, resultado.getContent().get(0).getCodSucursal());
        assertEquals(runUsuario, resultado.getContent().get(0).getRunUsuario());
        verify(recepRepo, times(1)).findByRunUsuarioAndCodSucursal(runUsuario, codSucursal, pageable);
    }

    @Test
    @DisplayName("Deberia lanzar ResourceNotFoundException cuando el usuario no existe al buscar por RUN Usuario.")
    void deberiaLanzarResourceNotFoundException_cuandoUsuarioNoExisteAlBuscarPorUsuario(){
        // GIVEN
        String runInexistente = "11111111-1";
        String codSucursal = "SU0001";
        Pageable pageable = PageRequest.of(0, 10);

        FeignException.NotFound feignNotFound = Mockito.mock(FeignException.NotFound.class);
        doThrow(feignNotFound).when(usuarioClient).buscarPorRun(runInexistente);

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> {
            recepServ.buscarPorUsuario(runInexistente, codSucursal, pageable);
        });
        verify(recepRepo, never()).findByRunUsuarioAndCodSucursal(runInexistente, codSucursal, pageable);
    }

    @Test
    @DisplayName("Deberia registrar una recepcion exitosamente")
    void deberiaRegistrarRecepcionExitosamente(){
        // GIVEN
        String runUsuario = "11222333-4";
        String codSucursal = "SU0001";

        RecepcionRequest request = new RecepcionRequest();
            request.setNumeroDocumento("1234");
            request.setTipoDocumento(TipoDocumento.GUIA_DESPACHO);
            request.setRutProveedor("12.234.678-9");

        DetalleRecepcion detalle = new DetalleRecepcion();
            detalle.setSku("PR0001");
            detalle.setCantidad(10);
            detalle.setPrecioUnitario(BigDecimal.valueOf(1500));
            detalle.setCodLote("L0001");
            detalle.setFechaVencimiento(LocalDate.of(2026, 12, 31));

        Recepcion recepcionNueva = new Recepcion();
            recepcionNueva.setNumeroDocumento("1234");
            recepcionNueva.setTipoDocumento(TipoDocumento.GUIA_DESPACHO);
            recepcionNueva.setRutProveedor("12.234.678-9");
            recepcionNueva.setCodSucursal("SU0001");
            recepcionNueva.setDetalles(List.of(detalle));

        Recepcion recepcionGuardada = new Recepcion();
            recepcionGuardada.setId(50L);
            recepcionGuardada.setNumeroDocumento("1234");
            recepcionGuardada.setEstado(EstadoRecepcion.PROCESADA);
            recepcionGuardada.setDetalles(List.of(detalle));

        RecepcionResponse response = new RecepcionResponse();
            response.setId(50L);
            response.setNumeroDocumento("1234");

        doNothing().when(usuarioClient).buscarPorRun(runUsuario);
        when(recepRepo.existsByRutProveedorAndTipoDocumentoAndNumeroDocumento(
                request.getRutProveedor(),
                request.getTipoDocumento(),
                request.getNumeroDocumento())).thenReturn(false);
        when(recepMapper.toRecepcionEntity(request, runUsuario, codSucursal)).thenReturn(recepcionNueva);
        when(recepRepo.save(recepcionNueva)).thenReturn(recepcionGuardada);
        doNothing().when(inventarioClient).registrarStockRecepcion(any(), eq(runUsuario));
        when(recepMapper.toRecepcionResponse(recepcionGuardada)).thenReturn(response);

        // WHEN
        RecepcionResponse resultado = recepServ.registrarRecepcion(request, runUsuario, codSucursal);

        // THEN 
        assertNotNull(resultado);
        assertEquals(50L, resultado.getId());
        assertEquals("1234", resultado.getNumeroDocumento());
        verify(recepRepo, times(1)).save(recepcionNueva);
        verify(inventarioClient, times(1)).registrarStockRecepcion(any(), eq(runUsuario));
    }

    @Test
    @DisplayName("Debería lanzar ResourceNotFoundException cuando el usuario que registra no existe")
    void deberiaLanzarResourceNotFoundException_cuandoUsuarioNoExisteAlRegistrar() {
        // GIVEN
        String runInexistente = "99999999-9";
        String codSucursal = "SU0001";
        RecepcionRequest request = new RecepcionRequest();

        FeignException.NotFound feignNotFound = Mockito.mock(FeignException.NotFound.class);
        doThrow(feignNotFound).when(usuarioClient).buscarPorRun(runInexistente);

        // WHEN & THEN 
        assertThrows(ResourceNotFoundException.class, () -> {
            recepServ.registrarRecepcion(request, runInexistente, codSucursal);
        });
        verify(recepRepo, never()).save(any());
        verify(inventarioClient, never()).registrarStockRecepcion(any(), any());
    }

    @Test
    @DisplayName("Debería lanzar ResourceAlreadyExistsException cuando el documento ya fue registrado antes")
    void deberiaLanzarResourceAlreadyExistsException_cuandoDocumentoEstaDuplicado() {
        // GIVEN
        String runUsuario = "11222333-4";
        String codSucursal = "SU0001";

        RecepcionRequest request = new RecepcionRequest();
        request.setNumeroDocumento("1234");
        request.setTipoDocumento(TipoDocumento.GUIA_DESPACHO);
        request.setRutProveedor("12.234.678-9");

        doNothing().when(usuarioClient).buscarPorRun(runUsuario);

        when(recepRepo.existsByRutProveedorAndTipoDocumentoAndNumeroDocumento(
                request.getRutProveedor(),
                request.getTipoDocumento(),
                request.getNumeroDocumento())).thenReturn(true);

        // WHEN & THEN
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            recepServ.registrarRecepcion(request, runUsuario, codSucursal);
        });
        verify(recepMapper, never()).toRecepcionEntity(any(), any(), any());
        verify(recepRepo, never()).save(any());
    }

    @Test
    @DisplayName("Debería lanzar BusinessException cuando el microservicio de Inventario retorna 404 Not Found")
    void deberiaLanzarBusinessException_cuandoInventarioRetorna404() {
        // GIVEN
        String runUsuario = "11222333-4";
        String codSucursal = "SU0001";

        RecepcionRequest request = new RecepcionRequest();
        request.setNumeroDocumento("1234");
        request.setTipoDocumento(TipoDocumento.GUIA_DESPACHO);
        request.setRutProveedor("12.234.678-9");

        DetalleRecepcion detalle = new DetalleRecepcion();
        detalle.setSku("PR0001");
        detalle.setCantidad(10);
        detalle.setPrecioUnitario(BigDecimal.valueOf(1500));

        Recepcion recepcionNueva = new Recepcion();
        recepcionNueva.setDetalles(List.of(detalle));

        Recepcion recepcionGuardada = new Recepcion();
        recepcionGuardada.setId(50L);
        recepcionGuardada.setDetalles(List.of(detalle));

        doNothing().when(usuarioClient).buscarPorRun(runUsuario);
        when(recepRepo.existsByRutProveedorAndTipoDocumentoAndNumeroDocumento(any(), any(), any())).thenReturn(false);
        when(recepMapper.toRecepcionEntity(request, runUsuario, codSucursal)).thenReturn(recepcionNueva);
        when(recepRepo.save(recepcionNueva)).thenReturn(recepcionGuardada);

        FeignException.NotFound feignNotFound = Mockito.mock(FeignException.NotFound.class);
        doThrow(feignNotFound).when(inventarioClient).registrarStockRecepcion(any(), eq(runUsuario));

        // WHEN & THEN
        assertThrows(BusinessException.class, () -> {
            recepServ.registrarRecepcion(request, runUsuario, codSucursal);
        });

        verify(recepRepo, times(1)).save(any(Recepcion.class));
    }

    @Test
    @DisplayName("Debería lanzar ServiceCommunicationException cuando el microservicio de Inventario está caído")
    void deberiaLanzarServiceCommunicationException_cuandoInventarioEstaCaido() {
        // GIVEN
        String runUsuario = "11222333-4";
        String codSucursal = "SU0001";

        RecepcionRequest request = new RecepcionRequest();
        request.setNumeroDocumento("1234");
        request.setTipoDocumento(TipoDocumento.GUIA_DESPACHO);
        request.setRutProveedor("12.234.678-9");

        DetalleRecepcion detalle = new DetalleRecepcion();
        detalle.setSku("PR0001");
        detalle.setCantidad(10);
        detalle.setPrecioUnitario(BigDecimal.valueOf(1500));

        Recepcion recepcionNueva = new Recepcion();
        recepcionNueva.setDetalles(List.of(detalle));

        Recepcion recepcionGuardada = new Recepcion();
        recepcionGuardada.setId(50L);
        recepcionGuardada.setDetalles(List.of(detalle));

        doNothing().when(usuarioClient).buscarPorRun(runUsuario);
        when(recepRepo.existsByRutProveedorAndTipoDocumentoAndNumeroDocumento(any(), any(), any())).thenReturn(false);
        when(recepMapper.toRecepcionEntity(request, runUsuario, codSucursal)).thenReturn(recepcionNueva);
        when(recepRepo.save(recepcionNueva)).thenReturn(recepcionGuardada);

        doThrow(FeignException.class).when(inventarioClient).registrarStockRecepcion(any(), eq(runUsuario));

        // WHEN & THEN
        assertThrows(ServiceCommunicationException.class, () -> {
            recepServ.registrarRecepcion(request, runUsuario, codSucursal);
        });
        verify(recepRepo, times(1)).save(any(Recepcion.class));
    }

    @Test
    @DisplayName("Debería cambiar el estado a CANCELADA cuando la recepción existe")
    void deberiaCancelarRecepcionExitosamente() {
        // GIVEN
        Long id = 1L;
        String codSucursal = "SU0001";
        
        Recepcion recepcion = new Recepcion();
        recepcion.setId(id);
        recepcion.setCodSucursal(codSucursal);
        recepcion.setEstado(EstadoRecepcion.PROCESADA);

        when(recepRepo.findByIdAndCodSucursal(id, codSucursal)).thenReturn(Optional.of(recepcion));
        when(recepRepo.save(any(Recepcion.class))).thenReturn(recepcion);

        // WHEN
        recepServ.cancelarRecepcionPorId(id, codSucursal);

        // THEN
        assertEquals(EstadoRecepcion.CANCELADA, recepcion.getEstado(), "El estado de la recepción debería ser CANCELADA");
        verify(recepRepo, times(1)).save(recepcion);
    }

    @Test
    @DisplayName("Debería lanzar ResourceNotFoundException al cancelar una recepción que no existe")
    void deberiaLanzarResourceNotFoundException_cuandoRecepcionNoExisteAlCancelar() {
        // GIVEN
        Long idInexistente = 99L;
        String codSucursal = "SU0001";

        when(recepRepo.findByIdAndCodSucursal(idInexistente, codSucursal)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> {
            recepServ.cancelarRecepcionPorId(idInexistente, codSucursal);
        });
        verify(recepRepo, never()).save(any());
    }

    @Test
    @DisplayName("Debería eliminar la recepción exitosamente cuando existe")
    void deberiaEliminarRecepcionExitosamente() {
        // GIVEN
        Long id = 1L;
        String codSucursal = "SU0001";
        
        Recepcion recepcion = new Recepcion();
        recepcion.setId(id);
        recepcion.setCodSucursal(codSucursal);

        when(recepRepo.findByIdAndCodSucursal(id, codSucursal)).thenReturn(Optional.of(recepcion));
        doNothing().when(recepRepo).delete(recepcion);

        // WHEN
        recepServ.eliminarRecepcionPorId(id, codSucursal);

        // THEN
        verify(recepRepo, times(1)).delete(recepcion);
    }

    @Test
    @DisplayName("Debería lanzar ResourceNotFoundException al intentar eliminar una recepción que no existe")
    void deberiaLanzarResourceNotFoundException_cuandoRecepcionNoExisteAlEliminar() {
        // GIVEN
        Long idInexistente = 99L;
        String codSucursal = "SU0001";

        when(recepRepo.findByIdAndCodSucursal(idInexistente, codSucursal)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> {
            recepServ.eliminarRecepcionPorId(idInexistente, codSucursal);
        });

        verify(recepRepo, never()).delete(any());
    }
}
