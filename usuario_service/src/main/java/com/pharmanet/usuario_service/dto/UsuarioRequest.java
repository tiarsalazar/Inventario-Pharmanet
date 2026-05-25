package com.pharmanet.usuario_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequest {

    @NotBlank(message = "Este campo no puede estar vacío")
    private String runVendedor; // RUN sin puntos y con guión

    private String codSucursal;

    private String receta;
}
