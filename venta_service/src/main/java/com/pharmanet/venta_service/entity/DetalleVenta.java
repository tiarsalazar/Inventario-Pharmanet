package com.pharmanet.venta_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "detalle_venta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detalle_venta_id")
    private Long id;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    @Size(min = 1, message = "Mínimo 1")
    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "cod_venta", nullable = false)
    private Venta venta;

    public DetalleVenta(String sku, Integer cantidad) {
        this.sku = sku;
        this.cantidad = cantidad;
    }
}
