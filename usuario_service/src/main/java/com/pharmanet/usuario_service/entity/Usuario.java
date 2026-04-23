package com.pharmanet.usuario_service.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usuarioId;

    @Column(name = "numrun", nullable = false)
    private int numRun;
    @Column(name = "dvrun", nullable = false, length = 1)
    private String dvRun;

    @Column(name = "appaterno", nullable = false, length = 20)
    private String apPaterno;
    @Column(name = "apmaterno", nullable = false, length = 20)
    private String apMaterno;
    @Column(nullable = false, length = 30)
    private String nombres;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @Column(length = 100)
    private String direccion;
    @Column(nullable = false)
    private String region;

    @Column(nullable = false, length = 12)
    private String telefono;
    @Column(nullable = false, unique = true, length = 50)
    private String correo;

    @Column(nullable = false, length = 25)
    private String profesion;
    @ManyToOne
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;
}