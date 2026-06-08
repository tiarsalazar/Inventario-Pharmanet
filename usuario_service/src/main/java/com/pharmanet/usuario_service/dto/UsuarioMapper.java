package com.pharmanet.usuario_service.dto;

import com.pharmanet.usuario_service.entity.Usuario;

public class UsuarioMapper {

    public static UsuarioDTO toDTO(Usuario usuario) {
        return new UsuarioDTO(usuario.getRun(),
            usuario.getNombreCompleto(),
            usuario.getCorreoInstitucional(),
            usuario.getTelefono(),
            usuario.getCodSucursal(),
            usuario.getProfesion().toUpperCase()
        );
    }

    public static Usuario toModel(UsuarioDTO usuarioDTO) {
        return new Usuario(usuarioDTO.getRun(),
            usuarioDTO.getNombreCompleto(),
            usuarioDTO.getCorreoInstitucional(),
            usuarioDTO.getTelefono(),
            usuarioDTO.getCodSucursal(),
            usuarioDTO.getProfesion().toUpperCase()
        );
    }
}
