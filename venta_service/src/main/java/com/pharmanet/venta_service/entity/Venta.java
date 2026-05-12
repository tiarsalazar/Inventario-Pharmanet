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

    @Column(name = "cod_prod", nullable = false)
    private String codProd;

    @Column(name = "cod_inventario", nullable = false)
    private String codInventario;

    @Column(name = "run_vendedor", nullable = false)
    @Pattern(regexp = "^[0-9]{7,8}-[0-9kK]$", message = "RUN inválido")
    private String runVendedor;
    
    @Column(nullable = false)
    private int cantidad;

    @Column(name = "fecha_venta", nullable = false)
    private LocalDate fechaVenta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estadoPago;

    public Venta(Long id, String codProd, String codInventario, String runVendedor, int cantidad, LocalDate fechaVenta) {
        this.id = id;
        this.codProd = codProd;
        this.codInventario = codInventario;
        this.cantidad = cantidad;
        this.runVendedor = runVendedor;
        this.fechaVenta = fechaVenta;
    }
    
    public Venta(String codProd, String codInventario, int cantidad, String runVendedor, LocalDate fechaVenta) {
        this.codProd = codProd;
        this.codInventario = codInventario;
        this.cantidad = cantidad;
        this.runVendedor = runVendedor;
        this.fechaVenta = fechaVenta;
    }
}
