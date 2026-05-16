package com.pharmanet.inventario_service.entity;

import java.time.LocalDate;

import com.pharmanet.inventario_service.enums.EstadoLote;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lotes")
public class Lote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lote_id")
    private Long id;
    @Column(name = "lote", nullable = false, length = 30)
    private String lote;
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoLote estado = EstadoLote.ACTIVO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventario_id", nullable = false)
    private Inventario inventario;
}
