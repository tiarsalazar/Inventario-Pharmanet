package com.pharmanet.venta_service.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pharmanet.venta_service.client.InventarioFeignClient;
import com.pharmanet.venta_service.client.SucursalFeignClient;
import com.pharmanet.venta_service.client.UsuarioFeignClient;
import com.pharmanet.venta_service.dto.ValidadoDto;
import com.pharmanet.venta_service.dto.VentaDto;
import com.pharmanet.venta_service.dto.VentaMapper;
import com.pharmanet.venta_service.entity.EstadoPago;
import com.pharmanet.venta_service.entity.Venta;
import com.pharmanet.venta_service.exception.ResourceNotFoundException;
import com.pharmanet.venta_service.exception.VentaNotUniqueException;
import com.pharmanet.venta_service.repository.VentaRepository;
import com.pharmanet.venta_service.request.InventarioRequest;
import com.pharmanet.venta_service.request.UsuarioRequest;

import feign.FeignException.FeignClientException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class VentaService {

    private final InventarioFeignClient inventarioFeignClient;

    private final UsuarioFeignClient usuarioFeignClient;

    private final VentaRepository ventaRepository;

    public VentaDto agregarVenta(VentaDto ventaDto) {
        log.info("Inicia funcionalidad de guardado de venta");
        log.debug("id: {}", ventaDto.getId());

        log.info("Valida que la venta no haya sido registrada anteriormente");
        if (ventaRepository.findById(ventaDto.getId()).isPresent()) {
            throw new VentaNotUniqueException("Ya existe una venta con el id: " + ventaDto.getId());
        }

        log.info("Valida que la fecha de la venta sea exactamente a la de ingreso");
        if(!ventaDto.getFechaVenta().equals(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de la venta no puede ser distinta a la fecha actual.");
        }
        
        log.info("Envío de solicitud de venta a inventario");
        log.debug("codSucursal: {}, sku: {}, cantidad: {}", ventaDto.getCodSucursal(), ventaDto.getSku(), ventaDto.getCantidad());
        try {
            InventarioRequest inventarioRequest = new InventarioRequest(ventaDto.getCodSucursal(), ventaDto.getSku(), ventaDto.getCantidad());
            inventarioFeignClient.procesarVenta(inventarioRequest);
        } catch (FeignClientException e){
            throw new ResourceNotFoundException("No existe un inventario con el código de la sucursal: " + ventaDto.getCodSucursal() + " ni el sku: " + ventaDto.getSku() + " o no hay stock disponible.");
        }

        log.info("Valida que el usuario pueda vender producto");
        log.debug("runEmpleado: {}", ventaDto.getRunEmpleado());
        
        UsuarioRequest usuarioRequest = new UsuarioRequest();
        ValidadoDto validacionVenta = usuarioFeignClient.validarUsuarioVenta(usuarioRequest);

        log.info("Convierte ventaDto en modelo");
        Venta venta = VentaMapper(ventaDto);
        venta.setEstadoPago(EstadoPago.PENDIENTE);

        // VALIDAR SI EN LA SUCURSAL HAY ALGUIEN QUE PUEDA VENDERLE

        // AGREGAR METODO DE REGISTRAR VENTA CONECTADO A INVENTARIO (VALIDAR QUE EXISTA STOCK Y QUE SE PUEDA VENDER EL PRODUCTO)

        if (venta.getEstadoPago().equalsIgnorCase("PENDIENTE")) {
            venta.setEstadoPago(EstadoPago.APROBADO);
        }

        log.info("Agrega nueva venta");
        log.debug("venta: {}", venta);
        return ventaRepository.save(venta);
    }

    public Page<VentaDto> mostrarTodos(Pageable pageable) {
        return ventaRepository.findAll(pageable)
            .map(VentaMapper::toDto);
    }

    public VentaDto buscarPorId(Long id) {
        log.info("Inicia búsqueda de venta por ID");
        log.debug("ID: {}", id);
    
        Venta venta = ventaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No existe una venta con el id: " + ventaDto.getId()));
        
        log.info("Convierte a dto y retorna");
        return VentaMapper.toDto(venta);
    }

    public Page<VentaDto> buscarPorFechas(LocalDate inicio, LocalDate termino, Pageable pageable) {
        log.info("Inicia búsqueda de ventas entre fechas indicadas");

        log.info("Valida que la fecha de inicio sea anterior a la fecha de término");
        log.debug("Inicio: {} Término: {}", inicio, termino);
        if (!inicio.isBefore(termino)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de término");
        }

        log.info("Devuelve page de ventas");
        return ventaRepository.findByBetweenFechaVenta(inicio, termino, pageable)
            .map(VentaMapper::toDto);
    }

    public Page<VentaDto> buscarPorDia(LocalDate dia, Pageable pageable) {
        log.info("Inicia búsqueda de ventas por día");
        log.debug("día: {}", dia);
        
        log.info("Valida que la fecha ingresada sea igual o anterior a la actual");
        if (dia.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha actual");
        }

        log.info("Devuelve page de ventas");
        return ventaRepository.findByDia(dia, pageable)
            .map(VentaMapper::toDto);
    }

    public Page<VentaDto> contarTodoPorCodProd(Pageable pageable) {
        log.info("Inicia búsqueda de ventas de todos los productos");
        return ventaRepository.countAllByCodProd(codProd, pageable)
            .map(VentaMapper::toDto);
    }

    public Page<VentaDto> contarPorCodProd(String codProd, Pageable pageable) {
        log.info("Inicia búsqueda de ventas por código del producto");
        log.debug("codProd: {}", codProd);

        return ventaRepository.countByCodProdGroupByCodInterno(codProd, pageable)
            .map(VentaMapper::toDto);
    }
}
