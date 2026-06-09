package com.pharmanet.usuario_service.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pharmanet.usuario_service.client.SucursalFeignClient;
import com.pharmanet.usuario_service.dto.UsuarioDTO;
import com.pharmanet.usuario_service.dto.UsuarioMapper;
import com.pharmanet.usuario_service.dto.MsResponse.UsuarioRequest;
import com.pharmanet.usuario_service.dto.MsResponse.UsuarioResponse;
import com.pharmanet.usuario_service.entity.Usuario;
import com.pharmanet.usuario_service.exception.ResourceAlreadyExistsException;
import com.pharmanet.usuario_service.exception.ResourceNotFoundException;
import com.pharmanet.usuario_service.repository.UsuarioRepository;

import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final SucursalFeignClient sucursalFeignClient;

    public UsuarioDTO agregarUsuario(UsuarioDTO dto) {
        log.info("Se inicia funcionalidad de agregar usuario");
        log.debug("DTO: {}", dto);

        log.info("Se verifica que no existan registros del usuario en el sistema");
        log.debug("RUN: {}", dto.getRun());
        if (usuarioRepository.findByRun(dto.getRun()).isPresent()) {
            throw new ResourceAlreadyExistsException("Ya existe el usuario con el run: " + dto.getRun());
        }

        log.info("Se verifica la existencia de la sucursal");
        log.debug("codSucursal: {}", dto.getCodSucursal());
        try {
            sucursalFeignClient.buscarSucursal(dto.getCodSucursal());
        } catch (FeignException.InternalServerError e) {
            log.info("No se encuentra sucursal {} asociada al usuario {}", dto.getCodSucursal(), dto.getRun());
            throw new ResourceNotFoundException("No se encuentra la sucursal con el código: " + dto.getCodSucursal());
        }

        log.info("Se transforma el dto a modelo");
        Usuario usuario = UsuarioMapper.toModel(dto);
        log.info("Se agrega usuario");
        usuarioRepository.save(usuario);

        log.info("Se transforma modelo a dto");
        return UsuarioMapper.toDTO(usuario);
    }

    public UsuarioDTO buscarPorRun(String run) {
        log.info("Se inicia búsqueda del usuario por RUN");
        log.debug("RUN: {}", run);

        Usuario usuario = usuarioRepository.findByRun(run)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra el usuario: " + run));

        log.info("Se transforma modelo a dto");
        return UsuarioMapper.toDTO(usuario);
    }

    public Page<UsuarioDTO> buscarPorProfesion(String profesion, Pageable pageable) {
        log.info("Inicia búsqueda por profesion");
        profesion = profesion.toUpperCase();
        log.debug("profesion: {}", profesion);

        return usuarioRepository.findByProfesion(profesion, pageable)
            .map(UsuarioMapper::toDTO);
    }

    public Page<UsuarioDTO> buscarPorSucursal(String codSucursal, Pageable pageable) {
        log.info("Inicia búsqueda por código de la sucursal");
        codSucursal.toUpperCase();
        log.debug("codSucursal: {}", codSucursal);

        return usuarioRepository.findByCodSucursal(codSucursal, pageable)
            .map(UsuarioMapper::toDTO);
    }

    public Page<UsuarioDTO> mostrarTodos(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
            .map(UsuarioMapper::toDTO);
    }

    public void actualizarUsuario(UsuarioDTO dto) {
        log.info("Inicia actualización del usuario");
        log.debug("usuarioDTO: {}", dto);

        log.info("Se verifica que el usuario exista");
        Usuario entidad = usuarioRepository.findByRun(dto.getRun())
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra el usuario con el run: " + dto.getRun()));

        UsuarioMapper.update(entidad, dto);

        log.debug("actualizada: {}", entidad);
        
        usuarioRepository.save(entidad);
    }

    public void eliminarUsuario(String run) {
        log.info("Inicia la funcionalidad de eliminar usuario");

        log.info("Verificar que el usuario exista");
        Usuario usuario = usuarioRepository.findByRun(run)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra el usuario con el run: " + run));

        usuarioRepository.delete(usuario);
    }

    public UsuarioResponse validarUsuarioVenta(UsuarioRequest request) {
        log.info("Inicia validación de venta");
        log.debug("run: {}, codSucursal: {}, receta: {}", request.getRunVendedor(), request.getCodSucursal(), request.getReceta());

        String run = request.getRunVendedor();
        String codSucursal = request.getCodSucursal().toUpperCase();
        String receta = request.getReceta().toUpperCase();

        log.info("Verifica existencia de usuario");
        Usuario entidad = usuarioRepository.findByRun(run)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        log.info("Valida autorización del usuario");
        if (!entidad.getProfesion().equals("TEC FARMACIA")
            && !entidad.getProfesion().equals("ANALISTA QUIMICO")) {
            log.warn("El usuario no está autorizado para vender el/los producto/s. Profesión: {}", entidad.getProfesion());
            return new UsuarioResponse(false, "El usuario con el run: " + run + " no está autorizado para vender el/los productos.");
        }

        log.info("Valida que el usuario ingresado se encuentra en la sucursal indicada");
        if (!codSucursal.equals(entidad.getCodSucursal())) {
            log.warn("El usuario {} no se encuentra en la sucursal {}", run, codSucursal);
            return new UsuarioResponse(false, "El usuario no se encuentra en la sucursal con el código: " + codSucursal);
        }

        if (!receta.equals("SIN_RECETA")) {
            log.info("Valida presencia de un analista químico en la farmacia");
        
            List<Usuario> analistaQuimico = usuarioRepository.findByProfesionAndCodSucursal("ANALISTA QUIMICO", codSucursal);
            if (analistaQuimico.isEmpty()) {
                log.warn("No hay ANALISTA QUÍMICO en la sucursal con el código: {}", codSucursal);
                return new UsuarioResponse(false, "No hay ANALISTA QUÍMICO en la sucursal con el código: " + codSucursal);
            }
        }
        
        log.info("Validación de usuario aprobada");
        return new UsuarioResponse(true, "La venta del vendedor con el run: " + run + " es válida");
    }
}
