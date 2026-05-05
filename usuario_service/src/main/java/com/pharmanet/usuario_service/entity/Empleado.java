/* Esta clase modela a los empleados del negocio. Su información personal y contacto */

package com.pharmanet.usuario_service.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "empleado")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    @Pattern(regexp = "^[0-9]{7,8}-[0-9Kk]$", message = "RUN inválido")
    private String run; // RUN sin puntos y con guión
    @Column(name = "nombre_completo", nullable = false, length = 80)
    private String nombreCompleto;

    @Column(name = "correo_institucional", nullable = false, unique = true, length = 30)
    private String correoInstitucional;
    @Column(nullable = false, length = 12)
    private String telefono;

    @Column(name = "cod_interno", nullable = false)
    private String codInterno;
    @Column(nullable = false, length = 30)
    private String profesion;
    
    @Column(length = 100)
    private String direccion;
    @Column(length = 30)
    private String comuna;
    @Column(length = 30)
    private String region;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.REMOVE)
    private Usuario usuario;

    public Empleado(String run, String nombreCompleto, String correoInstitucional, String telefono, String codInterno, String profesion) {
        this.run = run;
        this.nombreCompleto = nombreCompleto;
        this.correoInstitucional = correoInstitucional;
        this.telefono = telefono;
        this.codInterno = codInterno;
        this.profesion = profesion;
    }
}