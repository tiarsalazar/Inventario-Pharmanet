package com.pharmanet.usuario_service.service;

import org.springframework.stereotype.Service;

import com.pharmanet.usuario_service.dto.UsuarioDTO;
import com.pharmanet.usuario_service.dto.UsuarioMapper;
import com.pharmanet.usuario_service.entity.Usuario;
import com.pharmanet.usuario_service.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    // operaciones CRUD

    public Usuario agregarUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = UsuarioMapper.toModel(usuarioDTO);
        usuarioRepository.save(usuario, usuario.getUsuarioId());

        Usuario usserDTO = U
    }
}
