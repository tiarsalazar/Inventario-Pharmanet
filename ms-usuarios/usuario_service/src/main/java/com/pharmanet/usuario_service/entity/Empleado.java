/* Esta clase modela a los empleados del negocio. Su información personal y contacto */

package com.pharmanet.usuario_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "usuario")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String password;

    @Column(nullable = false, unique = true, length = 12)
    private String run;
    @Column(nullable = false, length = 80)
    private String nombre_completo;

    @Column(nullable = false, unique = true, length = 30)
    private String correo;
    @Column(nullable = false, length = 12)
    private String telefono;

    @Column(nullable = false, length = 30)
    private String profesion;
    
    @Column(nullable = false, length = 100)
    private String direccion;
    @Column(nullable = false, length = 30)
    private String comuna;
    @Column(nullable = false, length = 30)
    private String region;
}
