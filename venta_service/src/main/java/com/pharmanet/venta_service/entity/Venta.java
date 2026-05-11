package com.pharmanet.venta_service.entity;


// ATENTA A COMO SE INGRESO EL CODIGO DE PRODUCTO EN MICROSERVICE CATALOGO
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Column(name = "cod_prod", nullable = false)
    private String codProd; // PUEDE EDITARSE EL NOMBRE

    @Column(name = "cod_interno", nullable = false)
    private String codInterno;

    @Column(nullable = false)
    private int cantidad;

    @Column(name = "run_vendedor", nullable = false)
    private String runVendedor;

    @Column(name = "fecha_venta", nullable = false)
    private LocalDate fechaVenta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estadoPago;

    public Venta(String codProd, String codInterno, int cantidad, String runVendedor, LocalDate fechaVenta) {
        this.codProd = codProd;
        this.codInterno = codInterno;
        this.cantidad = cantidad;
        this.runVendedor = runVendedor;
        this.fechaVenta = fechaVenta;
    }
}
