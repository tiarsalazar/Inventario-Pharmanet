package com.pharmanet.usuario_service.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoDTO {

    @Column(nullable = false, unique = true, length = 12)
    private String run;
    @Column(nullable = false, length = 80)
    private String nombre_completo;

    @Column(nullable = false, unique = true, length = 30)
    private String correo;
    @Column(nullable = false, length = 12)
    private String telefono;

    @Column(nullable = false, length = 30)
    private String profesion;
}
