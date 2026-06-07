package com.pharmanet.sucursal_service.entity;

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
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "sucursal")
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sucursal_id")
    private Long id;

    @Column(name = "cod_sucursal", nullable = false, unique = true, length = 8)
    private String codSucursal;

    @Column(name = "nombre_sucursal", unique = true, length = 30)
    private String nombreSucursal;

    @Column(name = "cod_region", nullable = false, length = 2)
    private String codRegion;
    @Column(name = "cod_comuna", nullable = false)
    private Integer codComuna;
    @Column(nullable = false, length = 100)
    private String direccion;

    @Column(nullable = false, length = 15)
    private String estado = "activo"; // Sucursales activas por defecto

    public Sucursal(String codSucursal, String nombreSucursal, String codRegion, Integer codComuna, String direccion) {
        this.codSucursal = codSucursal;
        this.nombreSucursal = nombreSucursal;
        this.codRegion = codRegion;
        this.codComuna = codComuna;
        this.direccion = direccion;
    }
}