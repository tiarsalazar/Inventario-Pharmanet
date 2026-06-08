package com.pharmanet.usuario_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequest {

    // RUN sin puntos y con guión
    @NotBlank(message = "Este campo no puede estar vacío")
    private String runVendedor;

    private String codSucursal;

    private String receta;
}
