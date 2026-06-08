package com.pharmanet.usuario_service.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pharmanet.usuario_service.client.SucursalFeignClient;
import com.pharmanet.usuario_service.dto.UsuarioDTO;
import com.pharmanet.usuario_service.dto.UsuarioMapper;
import com.pharmanet.usuario_service.dto.UsuarioRequest;
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
        log.info("Inicia búsqueda de todos los usuarios"); // cambiarlo a controller
        return usuarioRepository.findAll(pageable)
            .map(UsuarioMapper::toDTO);
    }

    public void actualizarUsuario(UsuarioDTO usuarioDTO) {
        log.info("Inicia actualización del usuario");
        log.debug("usuarioDTO: {}", usuarioDTO);

        log.info("Se verifica que el usuario exista");
        Usuario usuarioVerificado = usuarioRepository.findByRun(usuarioDTO.getRun())
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra el usuario: " + usuarioDTO.getRun()));

        Usuario usuario = UsuarioMapper.toModel(usuarioDTO);

        log.info("Se transfiere información");

        log.debug("idUsuario: {}", usuarioVerificado.getId());
        usuario.setId(usuarioVerificado.getId());
        
        log.info("Se actualiza el usuario");
        usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(String run) {
        log.info("Inicia la funcionalidad de eliminar usuario");

        log.info("Verificar que el usuario exista");
        Usuario usuario = usuarioRepository.findByRun(run)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra el usuario: " + run));

        usuarioRepository.delete(usuario);
    }

    public boolean validarUsuarioVenta(UsuarioRequest request) {
        log.info("Inicia validación de venta");
        log.debug("run: {}, codSucursal: {}, receta: {}", request.getRunVendedor(), request.getCodSucursal(), request.getReceta());

        String run = request.getRunVendedor();
        String codSucursal = request.getCodSucursal();
        String receta = request.getReceta();

        log.info("Verifica existencia de usuario");
        Usuario usuario = usuarioRepository.findByRun(run)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        log.info("Valida credenciales del usuario");
        if (!usuario.getProfesion().equalsIgnoreCase("TECNICO EN FARMACIA")
            && !usuario.getProfesion().equalsIgnoreCase("ANALISTA QUIMICO")) {
            log.warn("El usuario no tiene las credenciales para vender los producto/s. Profesion: {}", usuario.getProfesion());
            return false;
        }

        log.info("Valida que el usuario ingresado se encuentra en la sucursal indicada");
        if (!codSucursal.equals(usuario.getCodSucursal())) {
            log.warn("El usuario {} no se encuentra en la sucursal {}", run, codSucursal);return false;
        }

        if (!receta.equalsIgnoreCase("SIN RECETA")) {
            log.info("Valida presencia de un analista químico en la farmacia");
        
            List<Usuario> analistaQuimico = usuarioRepository.findByProfesion("analista quimico", null)
                .toList();
            if (analistaQuimico.isEmpty()) {
                log.warn("No hay ANALISTA QUÍMICO en la sucursal {}", codSucursal);
                return false;
            }
        }
        
        log.info("Validación de usuario aprobada");
        return true;
    }
}
