package com.pharmanet.abastecimiento_service.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmanet.abastecimiento_service.entity.Recepcion;
import com.pharmanet.abastecimiento_service.enums.TipoDocumento;

@Repository
public interface RecepcionRepository extends JpaRepository<Recepcion, Long>{
    boolean existsByRutProveedorAndTipoDocumentoAndNumeroDocumento(
        String rutProveedor, TipoDocumento tipoDocumento, String numeroDocumento);
    Page<Recepcion> findByRutProveedor(String rutProveedor, Pageable pageable);
    Page<Recepcion> findByCodSucursal(String codSucursal, Pageable pageable);
    Page<Recepcion> findByOrdenCompra(String ordenCompra, Pageable pageable);
    Page<Recepcion> findByFechaIngresoBetween(LocalDateTime inicio, LocalDateTime fin, Pageable pageable);
    Page<Recepcion> findByRunUsuario(String runUsuario, Pageable pageable);
}
