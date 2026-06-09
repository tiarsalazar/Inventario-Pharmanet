package com.pharmanet.usuario_service.dto;

import com.pharmanet.usuario_service.entity.Usuario;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UsuarioMapper {

    public static UsuarioDTO toDTO(Usuario usuario) {
        log.info("Convierte una entidad en un objeto DTO");

        return new UsuarioDTO(usuario.getRun(),
            usuario.getNombreCompleto(),
            usuario.getCorreoInstitucional(),
            usuario.getTelefono(),
            usuario.getCodSucursal(),
            usuario.getProfesion().toUpperCase()
        );
    }

    public static Usuario toModel(UsuarioDTO usuarioDTO) {
        log.info("Convierte un objeto dto en una entidad");

        return new Usuario(usuarioDTO.getRun(),
            usuarioDTO.getNombreCompleto(),
            usuarioDTO.getCorreoInstitucional(),
            usuarioDTO.getTelefono(),
            usuarioDTO.getCodSucursal(),
            usuarioDTO.getProfesion().toUpperCase()
        );
    }

    public static Usuario update(Usuario actual, UsuarioDTO nuevo) {
        log.info("Actualiza la entidad actual con los datos enviados en el dto");
        
        actual.setRun(nuevo.getRun());
        actual.setNombreCompleto(nuevo.getNombreCompleto());
        actual.setCorreoInstitucional(nuevo.getCorreoInstitucional());
        actual.setTelefono(nuevo.getTelefono());
        actual.setCodSucursal(nuevo.getCodSucursal());
        actual.setProfesion(nuevo.getProfesion());

        return actual;
    }
}

