package com.pharmanet.usuario_service.service;

import java.util.Comparator;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pharmanet.usuario_service.dto.EmpleadoDTO;
import com.pharmanet.usuario_service.dto.EmpleadoMapper;
import com.pharmanet.usuario_service.entity.Empleado;
import com.pharmanet.usuario_service.exception.NotUniqueEmpleado;
import com.pharmanet.usuario_service.exception.ResourceNotFoundException;
import com.pharmanet.usuario_service.repository.EmpleadoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoDTO agregarEmpleado(EmpleadoDTO empleadoDTO) {
        log.info("Se inicia funcionalidad de agregar empleado");
        log.debug("empladoDTO: " + empleadoDTO);

        log.info("Se verifica que no existan registros del empleado en el sistema");
        log.debug("RUN: " + empleadoDTO.getRun());
        if (empleadoRepository.findByRun(empleadoDTO.getRun()).isPresent()) {
            throw new NotUniqueEmpleado("El empleado ya existe en la bd");
        }

        log.info("Se transforma el dto a modelo")
        Empleado empleado = EmpleadoMapper.toModel(empleadoDTO);
        log.info("Se agrega empleado");
        empleadoRepository.save(empleado);

        // Crear usuario

        // Setear usuario en empleado

        log.info("Se transforma modelo a dto");
        return EmpleadoMapper.toDTO(empleado);
    }

    public EmpleadoDTO buscarPorRun(String run) {
        log.info("Se inicia búsqueda del empleado por RUN");
        log.debug("RUN: " + run);

        Empleado empleado = empleadoRepository.findByRun(run)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra el empleado: " + run));

        log.info("Se transforma modelo a dto");
        return EmpleadoMapper.toDTO(empleado);
    }

    public Page<EmpleadoDTO> buscarPorProfesion(String profesion, Pageable pageable) {
        log.info("Inicia búsqueda por profesion");
        log.debug("profesion: " + profesion);
        return empleadoRepository.findByProfesion(profesion, pageable)
            .map(EmpleadoMapper::toDTO);
    }

    public Page<EmpleadoDTO> buscarPorSucursal(Long idSucursal, Pageable pageable) {
        log.info("Inicia búsqueda por ID de sucursal");
        log.debug("idSucursal: " + idSucursal);
        return empleadoRepository.findByIdSucursal(idSucursal, pageable)
            .map(EmpleadoMapper::toDTO);
    }

    public Page<EmpleadoDTO> mostrarTodos(Pageable pageable) {
        log.info("Inicia búsqueda de todos los empleados");
        return empleadoRepository.findAll(pageable)
            .map(EmpleadoMapper::toDTO);
    }

    public void actualizarEmpleado(EmpleadoDTO empleadoDTO) {
        log.info("Inicia actualización del empleado");
        log.debug("empleadoDTO: " + empleadoDTO);

        log.info("Se verifica que el empleado exista");
        Empleado empleadoVerificado = empleadoRepository.findByRun(empleadoDTO.getRun()).
            orElseThrow(() -> new ResourceNotFoundException("No se encuentra el empleado: " + empleadoDTO.getRun()));

        Empleado empleado = EmpleadoMapper.toModel(empleadoDTO);

        log.info("Se transfiere información");

        log.debug("idEmpleado: " + empleadoVerificado.getId());
        empleado.setId(empleadoVerificado.getId());

        log.debug("direccion: " + empleadoVerificado.getDireccion());
        empleado.setDireccion(empleadoVerificado.getDireccion());

        log.debug("comuna: " + empleadoVerificado.getComuna());
        empleado.setComuna(empleadoVerificado.getComuna());

        log.debug("region: " + empleadoVerificado.getRegion());
        empleado.setRegion(empleadoVerificado.getRegion());
        
        log.info("Se actualiza el empleado");
        empleadoRepository.save(EmpleadoMapper.toModel(empleadoDTO));
    }

    public void eliminarEmpleado(String run) {
        log.info("Inicia la funcionalidad de eliminar empleado");

        log.info("Verificar que el empleado exista");
        Empleado empleado = empleadoRepository.findByRun(run)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra el empleado: " + run));

        empleadoRepository.delete(empleado);

    }
}
