package com.pharmanet.venta_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidadoDto {

    private boolean estadoValidacion;
    private String message;
}
