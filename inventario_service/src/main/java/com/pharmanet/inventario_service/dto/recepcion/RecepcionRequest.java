package com.pharmanet.inventario_service.dto.recepcion;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecepcionRequest {
    @NotBlank(message = "Código sucursal es obligatorio")
    private String codSucursal;
    @NotNull(message = "Lista de detalles es obligatoria")
    @NotEmpty(message = "Debe incluir almenos un detalle")
    @Valid
    private List<DetalleRecepcionRequest> detalles;
}
