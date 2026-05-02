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
public class EmpleadoDTO {

    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(max = 10, message = "Máximo 10 caracteres")
    @Pattern(regexp = "(?i)[K0-9]+-", message = "Sin puntos y con guión")
    private String run; // RUN sin puntos y con guión

    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(max  = 80, message = "Máximo 80 caracteres")
    private String nombreCompleto;

    @NotBlank(message = "Esta campo no puede estar vacío")
    @Size(max = 30, message = "Máximo 30 caracteres")
    private String correoInstitucional;

    @NotBlank(message = "Esta campo no puede estar vacío")
    @Size(max = 12, message = "Máximo 12 caracteres")
    private String telefono;

    @NotNull(message = "Este campo no puede estar vacío")
    private String codInterno;

    @NotBlank(message = "Esta campo no puede estar vacío")
    @Size(max = 30, message = "Máximo 30 caracteres")
    private String profesion;
}
