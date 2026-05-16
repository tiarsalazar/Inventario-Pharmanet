package com.pharmanet.inventario_service.dto.inventario;

import java.util.List;

import com.pharmanet.inventario_service.dto.lote.LoteResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventarioResponse {
    private String sku;
    private String codSucursal;
    private Integer stockTotal;
    private List<LoteResponse> lotes;
}
