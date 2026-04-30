/* Esta clase modela la información de autenticación del usuario, su nombre de usuario y password */

package com.pharmanet.usuario_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Usuario {

    @Id
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String nombre_usuario;
    @Column(nullable = false, length = 20)
    private String password;
}
