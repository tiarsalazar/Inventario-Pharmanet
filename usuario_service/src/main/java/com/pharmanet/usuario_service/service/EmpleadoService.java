package com.pharmanet.usuario_service.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pharmanet.usuario_service.client.SucursalFeignClient;
import com.pharmanet.usuario_service.dto.EmpleadoDTO;
import com.pharmanet.usuario_service.dto.EmpleadoMapper;
import com.pharmanet.usuario_service.entity.Empleado;
import com.pharmanet.usuario_service.entity.Usuario;
import com.pharmanet.usuario_service.exception.NotUniqueEmpleadoException;
import com.pharmanet.usuario_service.exception.ResourceNotFoundException;
import com.pharmanet.usuario_service.repository.EmpleadoRepository;
import com.pharmanet.usuario_service.repository.UsuarioRepository;

import feign.FeignException.FeignClientException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    private final UsuarioRepository usuarioRepository;

    private final SucursalFeignClient sucursalFeignClient;

    public EmpleadoDTO agregarEmpleado(EmpleadoDTO empleadoDTO) {
        log.info("Se inicia funcionalidad de agregar empleado");
        log.debug("empladoDTO: {}", empleadoDTO);

        log.info("Se verifica que no existan registros del empleado en el sistema");
        log.debug("RUN: {}", empleadoDTO.getRun());
        if (empleadoRepository.findByRun(empleadoDTO.getRun()).isPresent()) {
            throw new NotUniqueEmpleadoException("El empleado ya existe en la bd");
        }

        log.info("Se verifica la existencia de la FK de sucursal (código interno)");
        log.debug("codInterno: {}", empleadoDTO.getCodInterno());
        try {
            sucursalFeignClient.buscarSucursalPorCodInterno(empleadoDTO.getCodInterno());
        } catch (FeignClientException e) {
            log.info("No se encuentra sucursal {} asociada al empleado {}", empleadoDTO.getCodInterno(), empleadoDTO.getRun());
            throw new ResourceNotFoundException("No se encuentra la sucursal: " + empleadoDTO.getCodInterno());
        }

        log.info("Se transforma el dto a modelo");
        Empleado empleado = EmpleadoMapper.toModel(empleadoDTO);
        log.info("Se agrega empleado");
        empleadoRepository.save(empleado);

        Usuario usuario = agregarUsuario(empleado);

        empleado.setUsuario(usuario);

        log.info("Se transforma modelo a dto");
        return EmpleadoMapper.toDTO(empleado);
    }

    public Usuario agregarUsuario(Empleado empleado) {
        log.info("Se inicia funcionalidad de agregar usuario");

        // Nombre de Usuario: Primeras 3 letras del nombre, primeras 4 siglas del RUN más el año y mes de creación
        log.info("Se crea nombre de usuario");
        String nombreUsuario = empleado.getNombreCompleto().replace(" ", "").substring(0, 3).toLowerCase() +
            empleado.getRun().substring(0, 4) +
            LocalDate.now().getYear() +
            LocalDate.now().getMonthValue();

        // Contraseña: Le cuarta a sexta letra del nombre sin espacios y las 4 últimas siglas del RUN
        log.info("Se crea contraseña");
        String password = empleado.getNombreCompleto().replace(" ", "").substring(3, 6) +
            empleado.getRun().substring(empleado.getRun().length() - 4);

        log.info("Se inicia usuario nuevo");
        Usuario usuario = new Usuario(empleado, nombreUsuario, password);

        log.info("Se agrega usuario");
        usuarioRepository.save(usuario);

        return usuario;
    }

    public EmpleadoDTO buscarPorRun(String run) {
        log.info("Se inicia búsqueda del empleado por RUN");
        log.debug("RUN: {}", run);

        Empleado empleado = empleadoRepository.findByRun(run)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra el empleado: " + run));

        log.info("Se transforma modelo a dto");
        return EmpleadoMapper.toDTO(empleado);
    }

    public Page<EmpleadoDTO> buscarPorProfesion(String profesion, Pageable pageable) {
        log.info("Inicia búsqueda por profesion");
        log.debug("profesion: {}", profesion);
        return empleadoRepository.findByProfesion(profesion, pageable)
            .map(EmpleadoMapper::toDTO);
    }

    public Page<EmpleadoDTO> buscarPorSucursal(String codInterno, Pageable pageable) {
        log.info("Inicia búsqueda por código interno de sucursal");
        log.debug("codInterno: {}", codInterno);
        return empleadoRepository.findByCodInterno(codInterno, pageable)
            .map(EmpleadoMapper::toDTO);
    }

    public Page<EmpleadoDTO> mostrarTodos(Pageable pageable) {
        log.info("Inicia búsqueda de todos los empleados"); // cambiarlo a controller
        return empleadoRepository.findAll(pageable)
            .map(EmpleadoMapper::toDTO);
    }

    public void actualizarEmpleado(EmpleadoDTO empleadoDTO) {
        log.info("Inicia actualización del empleado");
        log.debug("empleadoDTO: {}", empleadoDTO);

        log.info("Se verifica que el empleado exista");
        Empleado empleadoVerificado = empleadoRepository.findByRun(empleadoDTO.getRun()).
            orElseThrow(() -> new ResourceNotFoundException("No se encuentra el empleado: " + empleadoDTO.getRun()));

        Empleado empleado = EmpleadoMapper.toModel(empleadoDTO);

        log.info("Se transfiere información");

        log.debug("idEmpleado: {}", empleadoVerificado.getId());
        empleado.setId(empleadoVerificado.getId());

        log.debug("direccion: {}", empleadoVerificado.getDireccion());
        empleado.setDireccion(empleadoVerificado.getDireccion());

        log.debug("comuna: {}", empleadoVerificado.getComuna());
        empleado.setComuna(empleadoVerificado.getComuna());

        log.debug("region: {}", empleadoVerificado.getRegion());
        empleado.setRegion(empleadoVerificado.getRegion());
        
        log.info("Se actualiza el empleado");
        empleadoRepository.save(empleado);
    }

    public void eliminarEmpleado(String run) {
        log.info("Inicia la funcionalidad de eliminar empleado");

        log.info("Verificar que el empleado exista");
        Empleado empleado = empleadoRepository.findByRun(run)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra el empleado: " + run));

        Usuario usuario = usuarioRepository.findById(empleado.getId()).orElse(null);
        if(usuario != null) {
            log.info("Se elimina usuario");
            log.info("id: {}", usuario.getEmpleado().getId());
            usuarioRepository.delete(usuario);
        } else {
            log.warn("No existe usuario de empleado: {}", run);
        }

        empleadoRepository.delete(empleado);

    }
}
