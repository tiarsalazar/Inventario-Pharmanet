package com.pharmanet.usuario_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmanet.usuario_service.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

    Usuario findByNumRun(int numrun);

    List<Usuario> findByNombreContainingOrderByApPaternoAsc(String nombre);
    
    List<Usuario> findByProfesionOrderByApPaternoAsc(String profesion);

    List<Usuario> findBySucursal_IdOrderByApPaternoAsc(Long sucursal_id);
}
