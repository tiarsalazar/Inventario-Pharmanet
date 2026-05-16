package com.pharmanet.inventario_service.entity;

import java.util.ArrayList;
import java.util.List;

import com.pharmanet.inventario_service.enums.EstadoLote;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(
    name = "inventario",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_sku_sucursal",
        columnNames = {"sku", "cod_sucursal"}))
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "sku", nullable = false, length = 30)
    private String sku;
    @Column(name = "cod_sucursal", nullable = false, length = 10)
    private String codSucursal;
    @Column(name = "stock_total", nullable = false)
    private Integer stockTotal;

    @OneToMany(mappedBy = "inventario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Lote> lotes = new ArrayList<>();


    public void addLote(Lote lote){
        lotes.add(lote);
        lote.setInventario(this);
    }
    public void removeLote(Lote lote){
        lotes.remove(lote);
        lote.setInventario(null);
    }

    public void recalcularStock(){
        this.stockTotal = lotes.stream()
        .filter(lote -> lote.getEstado() == EstadoLote.ACTIVO)
        .mapToInt(Lote::getCantidad)
        .sum();
    }
}
