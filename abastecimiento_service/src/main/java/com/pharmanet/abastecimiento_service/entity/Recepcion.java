package com.pharmanet.abastecimiento_service.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.pharmanet.abastecimiento_service.enums.EstadoRecepcion;
import com.pharmanet.abastecimiento_service.enums.TipoDocumento;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recepciones", uniqueConstraints = {
    @UniqueConstraint(
        name = "uk_recepcion_doc_proveedor",
        columnNames = {"rut_proveedor", "tipo_documento", "numero_documento"}
        )
    })
public class Recepcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recepcion_id")
    private Long id;
    @Column(name = "run_usuario", nullable = false, length = 10)
    private String runUsuario;
    @Column(name = "orden_compra", length = 30)
    private String ordenCompra;
    @Column(name = "codigo_sucursal", nullable = false, length = 10)
    private String codSucursal;
    @Column(name = "numero_documento", nullable = false, length = 30)
    private String numeroDocumento;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false, length = 30)
    private TipoDocumento tipoDocumento;
    @Column(name = "rut_proveedor", nullable = false, length = 15)
    private String rutProveedor;
    @Column(name = "nombre_proveedor", nullable = false, length = 100)
    private String nombreProveedor;
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDateTime fechaIngreso = LocalDateTime.now();
    @Column(name = "observaciones", length = 255)
    private String observaciones;
    @Column(name = "monto_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoTotal;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_recepcion", nullable = false)
    private EstadoRecepcion estado = EstadoRecepcion.PENDIENTE;

    @OneToMany(mappedBy = "recepcion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleRecepcion> detalles = new ArrayList<>();

    public void addDetalle(DetalleRecepcion detalle){
        detalles.add(detalle);
        detalle.setRecepcion(this);
    }

    public void removeDetalle(DetalleRecepcion detalle){
        detalles.remove(detalle);
        detalle.setRecepcion(null);
    }
}
