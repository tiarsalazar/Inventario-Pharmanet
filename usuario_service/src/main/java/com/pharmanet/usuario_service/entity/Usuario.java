/* Esta clase modela la información de autenticación del usuario, su nombre de usuario y password */

package com.pharmanet.usuario_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
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
public class Usuario {

    @Id
    @MapsId
    @OneToOne
    @JoinColumn(name ="id")
    private Empleado empleado;

    @Column(name = "nombre_usuario", nullable = false, unique = true, length = 30)
    private String nombreUsuario;
    @Column(nullable = false, length = 60)
    private String password;
}
