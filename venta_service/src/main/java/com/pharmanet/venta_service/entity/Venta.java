package com.pharmanet.venta_service.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Column(name = "cod_venta", nullable = false)
    private Long codVenta;

    @Column(nullable = false)
    private String sku;

    @Column(name = "cod_sucursal", nullable = false, unique = true)
    private String codSucursal;

    @Column(name = "run_vendedor", nullable = false)
    @Pattern(regexp = "^[0-9]{7,8}-[0-9kK]$", message = "RUN inválido")
    private String runVendedor;
    
    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "fecha_venta", nullable = false)
    private LocalDate fechaVenta;
    
    public Venta(Long codVenta, String sku, String codSucursal, int cantidad, String runVendedor, LocalDate fechaVenta) {
        this.codVenta = codVenta;
        this.sku = sku;
        this.codSucursal = codSucursal;
        this.cantidad = cantidad;
        this.runVendedor = runVendedor;
        this.fechaVenta = fechaVenta;
    }
}
