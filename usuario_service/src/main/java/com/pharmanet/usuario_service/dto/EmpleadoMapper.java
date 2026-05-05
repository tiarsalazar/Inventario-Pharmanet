package com.pharmanet.usuario_service.dto;

import com.pharmanet.usuario_service.entity.Empleado;

public class EmpleadoMapper {

    public static EmpleadoDTO toDTO(Empleado empleado) {
        return new EmpleadoDTO(empleado.getRun(),
            empleado.getNombreCompleto(),
            empleado.getCorreoInstitucional(),
            empleado.getTelefono(),
            empleado.getCodInterno(),
            empleado.getProfesion().toLowerCase()
        );
    }

    public static Empleado toModel(EmpleadoDTO empleadoDTO) {
        return new Empleado(empleadoDTO.getRun(),
            empleadoDTO.getNombreCompleto(),
            empleadoDTO.getCorreoInstitucional(),
            empleadoDTO.getTelefono(),
            empleadoDTO.getCodInterno(),
            empleadoDTO.getProfesion().toLowerCase()
        );
    }
}
