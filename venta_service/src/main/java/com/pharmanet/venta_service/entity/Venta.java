package com.pharmanet.venta_service.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Column(name = "cod_venta", nullable = false)
    private Long codVenta;

    @Column(name = "cod_sucursal", nullable = false, unique = true)
    private String codSucursal;

    @Column(name = "run", nullable = false)
    @Pattern(regexp = "^[0-9]{7,8}-[0-9kK]$", message = "RUN inválido")
    private String run;
    
    @OneToMany(mappedBy = "venta")
    @Column(nullable = false)
    private List<DetalleVenta> detalleVentas;

    @Column(name = "fecha_venta", nullable = false)
    private LocalDate fechaVenta;
    
    public Venta(Long codVenta, String codSucursal, String run, LocalDate fechaVenta) {
        this.codVenta = codVenta;
        this.codSucursal = codSucursal;
        this.run = run;
        this.fechaVenta = fechaVenta;
    }
}
