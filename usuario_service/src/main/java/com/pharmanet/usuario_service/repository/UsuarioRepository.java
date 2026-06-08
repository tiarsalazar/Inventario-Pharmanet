package com.pharmanet.usuario_service.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmanet.usuario_service.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByRun(String run);

    Page<Usuario> findByProfesion(String profesion, Pageable pageable);

    Page<Usuario> findByCodSucursal(String codSucursal, Pageable pageable);

}
