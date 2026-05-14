package com.pharmanet.producto_service.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String sku; // CÓDIGO DE PRODUCTO
    @Column(name = "nombre_comercial", nullable = false, length = 100)
    private String nombreComercial;
    
    @Column(name = "principio_activo", nullable = false, length = 80)
    private String principioActivo;
    @Column(length = 100)
    private String laboratorio;

    @Column(name = "precio_venta", nullable = false)
    private BigDecimal precioVenta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaseReceta receta;

    @Column(length = 100)
    private String presentacion;
    @Column(nullable = false, length = 30)
    private String concentracion;

    public Producto(String sku, String nombreComercial, String principioActivo, BigDecimal precioVenta, ClaseReceta receta, String concentracion) {
        this.sku = sku;
        this.nombreComercial = nombreComercial;
        this.principioActivo = principioActivo;
        this.precioVenta = precioVenta;
        this.receta = receta;
        this.concentracion = concentracion;
    }
}