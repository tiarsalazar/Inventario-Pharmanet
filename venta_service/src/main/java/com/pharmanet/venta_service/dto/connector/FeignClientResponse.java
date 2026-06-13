package com.pharmanet.venta_service.dto.connector;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeignClientResponse {

    private boolean estado;
    private String mensaje;
}
