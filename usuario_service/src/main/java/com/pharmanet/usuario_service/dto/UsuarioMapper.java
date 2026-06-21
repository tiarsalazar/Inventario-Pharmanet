package com.pharmanet.usuario_service.dto;

import com.pharmanet.usuario_service.entity.Usuario;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UsuarioMapper {

    public static UsuarioDTO toDTO(Usuario usuario) {
        log.info("Convierte una entidad en un objeto DTO");

        UsuarioDTO dto = new UsuarioDTO();
        dto.setRun(usuario.getRun());
        dto.setNombreCompleto(usuario.getNombreCompleto());
        dto.setCorreoInstitucional(usuario.getCorreoInstitucional());
        dto.setTelefono(usuario.getTelefono());
        dto.setCodSucursal(usuario.getCodSucursal());
        dto.setProfesion(usuario.getProfesion());
        
        log.debug("dto: {}", dto);

        return dto;
    }

    public static Usuario toModel(UsuarioDTO dto) {
        log.info("Convierte un objeto dto en una entidad");

        Usuario entidad = new Usuario();
        entidad.setRun(dto.getRun());
        entidad.setNombreCompleto(dto.getNombreCompleto());
        entidad.setCorreoInstitucional(dto.getCorreoInstitucional().toLowerCase());
        entidad.setTelefono(dto.getTelefono());
        entidad.setCodSucursal(dto.getCodSucursal());
        entidad.setProfesion(dto.getProfesion().toUpperCase());
        
        log.debug("entidad: {}", entidad);

        return entidad;
    }

    public static Usuario update(Usuario actual, UsuarioDTO nuevo) {
        log.info("Actualiza la entidad actual con los datos enviados en el dto");
        
        actual.setRun(nuevo.getRun());
        actual.setNombreCompleto(nuevo.getNombreCompleto());
        actual.setCorreoInstitucional(nuevo.getCorreoInstitucional().toLowerCase());
        actual.setTelefono(nuevo.getTelefono());
        actual.setCodSucursal(nuevo.getCodSucursal());
        actual.setProfesion(nuevo.getProfesion().toUpperCase());

        return actual;
    }
}

