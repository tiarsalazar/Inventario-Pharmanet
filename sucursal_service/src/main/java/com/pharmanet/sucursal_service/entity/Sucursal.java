package com.pharmanet.sucursal_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Column(name = "cod_sucursal", nullable = false, unique = true, length = 10)
    @Pattern(regexp = "^[A-Z0-9]+$")
    private String codSucursal;

    @Column(name = "nombre_sucursal", unique = true, length = 30)
    private String nombreSucursal;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_sucursal", nullable = false)
    private TipoSucursal tipoSucursal;

    @Column(nullable = false, length = 30)
    private String region;
    @Column(nullable = false, length = 40)
    private String comuna;
    @Column(nullable = false, length = 100)
    private String direccion;

    @Column(nullable = false, length = 15)
    private String estado = "activo"; // Sucursales activas por defecto

    public Sucursal(String codSucursal, String nombreSucursal, TipoSucursal tipoSucursal, String region, String comuna, String direccion) {
        this.codSucursal = codSucursal;
        this.nombreSucursal = nombreSucursal;
        this.tipoSucursal = tipoSucursal;
        this.region = region;
        this.comuna = comuna;
        this.direccion = direccion;
    }
}