package com.pharmanet.usuario_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
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
@Schema(name = "Usuario DTO", description = "Objeto usuario con la información accesible al cliente")
public class UsuarioDTO {

    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(max = 10, message = "Máximo 10 caracteres")
    @Pattern(regexp = "^[0-9]{7,8}-[0-9kK]$", message = "RUN inválido")
    @Schema(description = "Run del usuario", example = "1111111-1")
    private String run; // RUN sin puntos y con guión

    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(min = 12, max  = 80, message = "Entre 12 y 80 caracteres")
    @Schema(description = "Nombre completo", example = "Andrea Aravena Castro")
    private String nombreCompleto;

    @NotBlank(message = "Esta campo no puede estar vacío")
    @Size(max = 30, message = "Máximo 30 caracteres")
    @Email(message = "Debe ingresar un correo válido")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@pharmanet\\.cl$",
        message = "Formato inválido: Debe ser el correo institucional (@pharmanet.cl)"
    )
    @Schema(description = "Correo institucional", example = "fabiola123@pharmanet.cl")
    private String correoInstitucional;

    @NotBlank(message = "Esta campo no puede estar vacío")
    @Pattern(regexp = "^\\+569\\d{8}$", message = "Formato válido: +569XXXXXXXX")
    @Schema(description = "Teléfono", example = "+56912341234")
    private String telefono;

    @NotNull(message = "Este campo no puede estar vacío")
    @Schema(description = "Código de la sucursal", example = "SU0001")
    private String codSucursal;

    @NotBlank(message = "Esta campo no puede estar vacío")
    @Pattern(regexp =  "(?i)^(TEC FARMACIA|ANALISTA QUIMICO|DEVOPS)$",
        message = "La profesión debe ser TEC FARMACIA, ANALISTA QUIMICO, DEVOPS"
    )
    @Schema(description = "Profesión del usuario", example = "DEVOPS")
    private String profesion;
}
