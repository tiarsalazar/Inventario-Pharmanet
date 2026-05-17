package com.pharmanet.abastecimiento_service.dto.recepcion;

import java.util.List;

import com.pharmanet.abastecimiento_service.dto.detallerecepcion.DetalleRecepcionRequest;
import com.pharmanet.abastecimiento_service.enums.TipoDocumento;

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
public class RecepcionRequest {
    @Size(max = 30, message = "Orden de compra no puede superar 30 caracteres.")
    private String ordenCompra;
    @NotBlank(message = "Codigo de sucursal es obligatorio.")
    @Size(max = 10, message = "Codigo sucursal no puede superar 10 caracteres.")
    private String codSucursal;
    @NotBlank(message = "Numero de documento es obligatorio.")
    @Size(max = 30, message = "Numero de documento no puede superar 30 caracteres.")
    private String numeroDocumento;
    @NotNull(message = "Tipo documento es obligatorio.")
    private TipoDocumento tipoDocumento;
    @NotBlank(message = "RUT proveedor es obligatorio.")
    @Size(max = 15, message = "RUT proveedor no puede superar 15 caracteres.")
    private String rutProveedor;
    @NotBlank(message = "Nombre proveedor es obligatorio.")
    @Size(max = 100, message = "Nombre proveedor no debe superar 100 caracteres.")
    private String nombreProveedor;
    @Size(max = 255, message = "Observacion no puede superar 255 caracteres.")
    private String observaciones;
    @Valid 
    @NotEmpty(message = "Debe ingresar al menos un detalle.")
    private List<DetalleRecepcionRequest> detalles;
}
