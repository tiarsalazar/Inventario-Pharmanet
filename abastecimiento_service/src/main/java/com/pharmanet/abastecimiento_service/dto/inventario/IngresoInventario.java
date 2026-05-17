package com.pharmanet.abastecimiento_service.dto.inventario;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngresoInventario {
    private String codSucursal;
    private List<DetalleIngresoInventario> detalles;
}
