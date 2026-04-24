package com.pharmanet.usuario_service.dto;

import com.pharmanet.usuario_service.entity.Usuario;

public class UsuarioMapper {

    public static UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario.getNumRun(),
            usuario.getDvRun(),
            usuario.getApPaterno(),
            usuario.getApMaterno(),
            usuario.getNombres(),
            usuario.getFechaNacimiento(),
            usuario.getDireccion(),
            usuario.getRegion(),
            usuario.getTelefono(),
            usuario.getCorreo(),
            usuario.getProfesion());
            //usuario.getSucursal

        return usuarioDTO;
    }

    public static Usuario toModel(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        
        usuario.setNumRun(usuarioDTO.getNumRun());
        usuario.setDvRun(usuarioDTO.getDvRun());
        usuario.setApMaterno(usuarioDTO.getApPaterno());
        usuario.setApMaterno(usuarioDTO.getApMaterno());
        usuario.setNombres(usuarioDTO.getNombres());
        usuario.setFechaNacimiento(usuarioDTO.getFechaNacimiento());
        usuario.setDireccion(usuarioDTO.getDireccion());
        usuario.setRegion(usuario.getRegion());
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setProfesion(usuarioDTO.getProfesion());
        //usuario.setSucursal(usuarioDTO.getSucursal());

        return usuario;
    }
}
