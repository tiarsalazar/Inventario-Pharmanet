package com.pharmanet.usuario_service.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private int numRun;
    private String dvRun;

    private String apPaterno;
    private String apMaterno;
    private String nombres;

    private LocalDate fechaNacimiento;

    private String direccion; // puede ser null
    private String region;

    private String telefono;
    private String correo;

    private String profesion;

    /*@ManyToOne
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;*/
}
