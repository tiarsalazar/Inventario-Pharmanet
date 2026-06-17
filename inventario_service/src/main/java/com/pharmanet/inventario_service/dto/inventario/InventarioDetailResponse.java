package com.pharmanet.inventario_service.dto.inventario;

import java.util.List;

import com.pharmanet.inventario_service.dto.lote.LoteResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Estructura de respuesta con los datos de Inventario.")
public class InventarioDetailResponse {
    @Schema(description = "Codigo interno unico del producto.", example = "PR00001")
    private String sku;
    @Schema(description = "Codigo interno unico de una Sucursal", example = "SU0001")
    private String codSucursal;
    @Schema(description = "Cantidad total de productos activos", example = "20")
    private Integer stockTotal;
    @Schema(description = "Listado con el detalle de lotes de un Inventario.")
    private List<LoteResponse> lotes;
}
