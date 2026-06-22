package com.pharmanet.venta_service.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "venta")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "venta_id")
    private Long id;

    @Column(name = "cod_venta", nullable = false, unique = true)
    private Long codVenta;

    @Column(name = "cod_sucursal", nullable = false, unique = true)
    private String codSucursal;

    @Column(name = "run", nullable = false)
    @Pattern(regexp = "^[0-9]{7,8}-[0-9kK]$", message = "RUN inválido")
    private String run;
    
    @OneToMany(mappedBy = "venta", fetch = FetchType.LAZY)
    private List<DetalleVenta> detalleVentas;

    @Column(name = "fecha_venta", nullable = false)
    private LocalDate fechaVenta;

    @Column(name = "monto_total", nullable = false)
    private BigDecimal montoTotal;
    
    public Venta(Long codVenta, String codSucursal, String run, LocalDate fechaVenta, BigDecimal montoTotal) {
        this.codVenta = codVenta;
        this.codSucursal = codSucursal;
        this.run = run;
        this.fechaVenta = fechaVenta;
        this.montoTotal = montoTotal;
    }
}
