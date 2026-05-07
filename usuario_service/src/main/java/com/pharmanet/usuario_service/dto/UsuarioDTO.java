package com.pharmanet.usuario_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(max = 10, message = "Máximo 10 caracteres")
    @Pattern(regexp = "^[0-9]{7,8}-[0-9kK]$", message = "RUN inválido")
    private String run; // RUN sin puntos y con guión

    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(min = 12, max  = 80, message = "Entre 12 y 80 caracteres")
    private String nombreCompleto;

    @NotBlank(message = "Esta campo no puede estar vacío")
    @Size(max = 30, message = "Máximo 30 caracteres")
    private String correoInstitucional;

    @NotBlank(message = "Esta campo no puede estar vacío")
    @Pattern(regexp = "^\\+569\\d{8}$", message = "Formato válido: +569XXXXXXXX")
    private String telefono;

    @NotNull(message = "Este campo no puede estar vacío")
    private String codInterno;

    @NotBlank(message = "Esta campo no puede estar vacío")
    @Size(max = 30, message = "Máximo 30 caracteres")
    private String profesion;
}
