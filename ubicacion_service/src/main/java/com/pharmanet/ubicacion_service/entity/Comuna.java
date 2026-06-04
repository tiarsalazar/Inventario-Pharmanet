package com.pharmanet.ubicacion_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "comuna")
public class Comuna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comuna_id")
    private Integer comunaId;

    @Column(name = "cod_comuna", nullable = false, unique = true)
    private Integer codComuna;

    @Column(nullable = false, unique = true, length = 30)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "cod_region", referencedColumnName = "cod_region", nullable = false)
    private Region region;
}
