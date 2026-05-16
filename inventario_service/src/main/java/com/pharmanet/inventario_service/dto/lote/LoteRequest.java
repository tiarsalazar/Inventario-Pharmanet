package com.pharmanet.inventario_service.dto.lote;

import java.time.LocalDate;

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
public class LoteRequest {
    @NotBlank(message = "Sku es obligatorio")
    @Size(max = 30, message = "Sku no debe superar 30 caracteres")
    private String sku;
    @NotBlank(message = "Lote es obligatorio")
    @Min(value = 30, message = "Lote no debe superar 30 caracteres")
    private String codLote;
    @NotNull(message = "Cantidad es obligatoria")
    @Size(min = 1, message = "Cantidad debe ser superior a 0")
    private Integer cantidad;
    @NotNull(message = "Fecha vencimiento es obligatoria")
    @Future(message = "Fecha vencimiento debe ser futura")
    private LocalDate fechaVencimiento;
}
