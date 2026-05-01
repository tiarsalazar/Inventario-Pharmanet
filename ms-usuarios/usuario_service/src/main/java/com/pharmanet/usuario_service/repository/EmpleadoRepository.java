package com.pharmanet.usuario_service.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmanet.usuario_service.entity.Empleado;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    Optional<Empleado> findByRun(String run);

    Page<Empleado> findByProfesionOrderByNombreAsc(String profesion);
}
