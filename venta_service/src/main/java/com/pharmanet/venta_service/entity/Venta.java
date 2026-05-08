package com.pharmanet.venta_service.entity;


// ATENTA A COMO SE INGRESO EL CODIGO DE PRODUCTO EN MICROSERVICE CATALOGO
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Column(name = "codProd", nullable = false)
    private String codProd; // PUEDE EDITARSE EL NOMBRE

    @Column(name = "cod_interno", nullable = false)
    private String codInterno;

    @Column(nullable = false)
    private int cantidad;

    @Column(name = "fecha_venta", nullable = false)
    private LocalDate fechaVenta;

    @Column(nullable = false)
    private boolean estado;

    public Venta(String codProd, String codInterno, int cantidad) {
        this.codProd = codProd;
        this.codInterno = codInterno;
        this.cantidad = cantidad;
        this.fechaVenta = LocalDate.now();
        this.estado = false;
    }
}
