package com.pharmanet.inventario_service.dto.recepcion;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Estructura de entrada de detalles para Ingreso de stock a Inventario")
public class DetalleRecepcionRequest {
    @Schema(description = "Codigo interno unico del producto.", example = "PR00001")
    @NotBlank(message = "Sku es obligatorio")
    @Size(max = 30, message = "Sku no debe superar 30 caracteres")
    private String sku;
    @Schema(description = "Codigo del lote.", example = "LOT-PR00001-001")
    @NotBlank(message = "Lote es obligatorio")
    @Size(max = 30, message = "Lote no debe superar 30 caracteres")
    private String codLote;
    @Schema(description = "Cantidad del lote a ingresar", example = "15")
    @NotNull(message = "Cantidad es obligatoria")
    @Min(value = 1, message = "Cantidad debe ser superior a 0")
    private Integer cantidad;
    @Schema(description = "Fecha de vencimiento del lote", example = "2027-12-31")
    @NotNull(message = "Fecha vencimiento es obligatoria")
    @Future(message = "Fecha vencimiento debe ser futura")
    private LocalDate fechaVencimiento;
}
