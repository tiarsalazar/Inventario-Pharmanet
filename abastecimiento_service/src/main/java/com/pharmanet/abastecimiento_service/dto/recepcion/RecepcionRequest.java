package com.pharmanet.abastecimiento_service.dto.recepcion;

import java.util.List;

import com.pharmanet.abastecimiento_service.dto.detallerecepcion.DetalleRecepcionRequest;
import com.pharmanet.abastecimiento_service.enums.TipoDocumento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de entrada para recepcion.")
public class RecepcionRequest {
    @NotBlank(message = "Numero de documento es obligatorio.")
    @Size(max = 30, message = "Numero de documento no puede superar 30 caracteres.")
    private String numeroDocumento;
    @NotNull(message = "Tipo documento es obligatorio.")
    private TipoDocumento tipoDocumento;
    @NotBlank(message = "RUT proveedor es obligatorio.")
    @Size(max = 15, message = "RUT proveedor no puede superar 15 caracteres.")
    private String rutProveedor;
    @Valid 
    @NotEmpty(message = "Debe ingresar al menos un detalle.")
    private List<DetalleRecepcionRequest> detalles;
}
