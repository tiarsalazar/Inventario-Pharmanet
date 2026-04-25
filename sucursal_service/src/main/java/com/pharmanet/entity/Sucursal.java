package com.pharmanet.entity;

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
    @Column(name = "id_sucursal")
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String codInterno;

    @Column(unique = true, length = 30)
    private String nombreSucursal;
    @Column(nullable = false, length = 20)
    private String tipoSucursal;

    @Column(nullable = false, length = 20)
    private String region;
    @Column(nullable = false, length = 25)
    private String comuna;
    @Column(nullable = false, length = 100)
    private String direccion;

    @Column(nullable = false, length = 15)
    private String estado = "activo"; // Las sucursales se construyen activas por defecto

    public Sucursal(String codInterno, String nombreSucursal, String tipoSucursal, String region, String comuna, String direccion) {
        this.codInterno = codInterno;
        this.nombreSucursal = nombreSucursal;
        this.tipoSucursal = tipoSucursal;
        this.region = region;
        this.comuna = comuna;
        this.direccion = direccion;
    }
}
