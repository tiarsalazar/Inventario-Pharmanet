package com.pharmanet.venta_service.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pharmanet.venta_service.client.InventarioFeignClient;
import com.pharmanet.venta_service.client.ProductoFeignClient;
import com.pharmanet.venta_service.client.UsuarioFeignClient;
import com.pharmanet.venta_service.dto.RegistroVenta;
import com.pharmanet.venta_service.dto.VentaDto;
import com.pharmanet.venta_service.dto.VentaMapper;
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

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class VentaService {

    private final InventarioFeignClient inventarioFeignClient;

    private final ProductoFeignClient productoFeignClient;

    private final UsuarioFeignClient usuarioFeignClient;

    private final VentaRepository ventaRepository;

    private final DetalleVentaRepository detalleVentaRepository;

    // ==========================================
    // SAVE
    // ==========================================

    public RegistroVenta agregarVenta(RegistroVenta dto) {
        log.info("Inicia funcionalidad de agregar venta");
        log.debug("dto: {}", dto);

        log.info("Valida que la venta no haya sido registrada anteriormente");
        if (ventaRepository.findByCodVenta(dto.getCodVenta()).isPresent()) {
            throw new ResourceAlreadyExistsException("Ya existe una venta con el código: " + dto.getCodVenta());
        }

        log.info("Valida que la fecha de la venta sea exactamente igual a la de ingreso");
        if (dto.getFechaVenta() == null) {
            dto.setFechaVenta(LocalDate.now());
        } else if(!dto.getFechaVenta().equals(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de venta no puede ser distinta a la fecha actual. Fecha ingresada: "
            + dto.getFechaVenta()
            + ". Fecha actual: " + LocalDate.now());
        }

        log.info("Obtiene tipo de receta del producto");

        List<DetalleVenta> detalleVentas = convertirDetalleVenta(dto.getProductos());

        List<String> skus = detalleVentas.stream()
            .map(DetalleVenta::getSku)
            .toList();

        log.debug("skus: {}", skus);

        String receta;
        try {
            receta = productoFeignClient.obtenerReceta(skus)
                .getBody();
        } catch (Exception ex) {
            throw new ResourceNotFoundException("No se encuentra al menos uno de los productos ingresados");
        }

        log.info("Valida que la venta del usuario sea efectiva");
        
        UsuarioRequest usuarioRequest = new UsuarioRequest();
        usuarioRequest.setRun(dto.getRun());
        usuarioRequest.setCodSucursal(dto.getCodSucursal());
        usuarioRequest.setReceta(receta);
        log.debug("run: {}, codSucursal: {}, receta: {}", usuarioRequest.getRun(), usuarioRequest.getCodSucursal(), receta);

        FeignClientResponse usuarioValido;
        try {
            usuarioValido = usuarioFeignClient.validarUsuarioVenta(usuarioRequest)
                .getBody();
        } catch (Exception ex) {
            throw new ResourceNotFoundException("No se encuentra el usuario con el run: " + usuarioRequest.getRun());
        }

        if (!usuarioValido.isEstado())
            throw new VentaInvalida(usuarioValido.getMensaje());

        log.info("Envío de solicitud de venta a inventario");
        FeignClientResponse inventarioValido;
        try {
            InventarioRequest inventarioRequest = new InventarioRequest();
            inventarioRequest.setRun(dto.getRun());
            inventarioRequest.setCodSucursal(dto.getCodSucursal());
            inventarioRequest.setProductos(detalleVentas);
            log.debug("runVendedor: {}, codSucursal: {}, productos: {}", inventarioRequest.getRun(), inventarioRequest.getCodSucursal(), inventarioRequest.getProductos());

            inventarioValido = inventarioFeignClient.procesarVenta(inventarioRequest)
                .getBody();
        } catch (Exception ex){
            throw new VentaInvalida("No existe un inventario con el código de la sucursal: " + dto.getCodSucursal() + " o alguno de los productos no está disponible");
        }

        if (!inventarioValido.isEstado())
            throw new VentaInvalida(inventarioValido.getMensaje());

        BigDecimal montoTotal = productoFeignClient.calcularPrecioVentaTotal(dto.getProductos())
            .getBody();
        
        if (dto.getMontoTotal() != null && dto.getMontoTotal().compareTo(montoTotal) != 0)
            throw new VentaInvalida("Monto ingresado distinto al monto calculado. Monto ingresado: $" + dto.getMontoTotal() + ". Monto total: $" + montoTotal);

        log.info("Agrega la venta");

        Venta venta = ventaRepository.save(new Venta(
            dto.getCodVenta(),
            dto.getCodSucursal(),
            dto.getRun(),
            dto.getFechaVenta(),
            montoTotal));

        log.info("Agrega los detalles de ventas");
        for (DetalleVenta dv : detalleVentas) {
            dv.setVenta(venta);
            detalleVentaRepository.save(dv);
        }

        return VentaMapper.toRegistroVenta(venta, detalleVentas);
    }

    public List<DetalleVenta> convertirDetalleVenta(Map<String, Integer> productos) {

        log.info("Conversión de productos a detalle ventas");
        List<DetalleVenta> detalleVentas = new ArrayList<>();

        for (Map.Entry<String, Integer> p : productos.entrySet()) {
            if (p.getValue() <= 0) throw new IllegalArgumentException("La cantidad ingresada por producto no puede ser igual o inferior a 0");

            DetalleVenta entidad = new DetalleVenta(p.getKey(), p.getValue());
            detalleVentas.add(entidad);
        }

        return detalleVentas;
    }

    // ==========================================
    // READ
    // ==========================================

    public Page<VentaDto> mostrarTodos(Pageable pageable) {
        return ventaRepository.findAll(pageable)
            .map(VentaMapper::toDto);
    }

    public RegistroVenta buscarPorCodVenta(Long codVenta) {
        log.info("Inicia búsqueda de venta por codVenta");
        log.debug("codVenta: {}", codVenta);
    
        Venta venta = ventaRepository.findByCodVenta(codVenta)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la venta con el código: " + codVenta));

        List<DetalleVenta> detallesVenta = detalleVentaRepository.findByVenta_CodVenta(codVenta);
        
        return VentaMapper.toRegistroVenta(venta, detallesVenta);
    }

    public Page<VentaDto> buscarPorFechas(LocalDate inicio, LocalDate termino, Pageable pageable) {
        log.info("Inicia búsqueda de ventas entre fechas indicadas");

        log.info("Valida que la fecha ingresada sea válida");
        log.debug("Inicio: {} Término: {}", inicio, termino);

        if (!inicio.isBefore(termino))
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de término");

        if (inicio.isBefore(LocalDate.parse("1998-01-01")))
            throw new IllegalArgumentException("No hay registros antes de la fecha 1998");

        if (termino.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("La fecha de término no puede ser posterior a la actual");

        log.info("Devuelve page de ventas");
        return ventaRepository.findByFechaVentaBetween(inicio, termino, pageable)
            .map(VentaMapper::toDto);
    }

    public Page<VentaDto> buscarPorDia(LocalDate dia, Pageable pageable) {
        log.info("Inicia búsqueda de ventas por día");
        log.debug("día: {}", dia);
        
        log.info("Valida que la fecha ingresada sea igual o anterior a la actual");
        if (dia.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("La fecha ingresada no puede ser posterior a la actual");

        if (dia.isBefore(LocalDate.parse("1998-01-01")))
            throw new IllegalArgumentException("No hay registros antes de la fecha 1998");

        log.info("Devuelve page de ventas");
        return ventaRepository.findByFechaVenta(dia, pageable)
            .map(VentaMapper::toDto);
    }

    // ==========================================
    // UPDATE
    // ==========================================

    public void actualizarVenta(VentaDto dto) {
        log.info("Inicia actualización de dto");
        log.debug("dto: {}", dto);
        
        if (!ventaRepository.findByCodVenta(dto.getCodVenta()).isPresent())
            throw new ResourceNotFoundException("No se encuentra la venta con el código: " + dto.getCodVenta());

        Venta venta = VentaMapper.toModel(dto);
        ventaRepository.save(venta);
    }

    // ==========================================
    // DELETE
    // ==========================================

    public void eliminarVenta(Long codVenta) {
        log.info("Inicia eliminación de la venta");
        log.debug("codVenta: {}", codVenta);

        Venta venta = ventaRepository.findByCodVenta(codVenta)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la venta con el código: " + codVenta));

        ventaRepository.delete(venta);
    }
}
