package com.pharmanet.usuario_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidadoVentaDTO {

    private boolean estadoValidacion;
    private String mensaje;
}
