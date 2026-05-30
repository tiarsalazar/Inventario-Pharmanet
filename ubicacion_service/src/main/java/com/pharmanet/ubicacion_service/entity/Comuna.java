package com.pharmanet.ubicacion_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
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

    @Column(nullable = false, length = 30)
    @Size(min = 4, message = "Este campo no puede tener menos de 4 carácteres.")
    private String descripcion;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "region", nullable = false)
    private Region region;
}
