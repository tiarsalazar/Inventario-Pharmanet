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
    Page<Recepcion> findByRutProveedorAndCodSucursal(String rutProveedor, String codSucursal, Pageable pageable);
    Page<Recepcion> findByCodSucursal(String codSucursal, Pageable pageable);
    Page<Recepcion> findByOrdenCompraAndCodSucursal(String ordenCompra, String codSucursal, Pageable pageable);
    Page<Recepcion> findByCodSucursalAndFechaIngresoBetween(String codSucursal, LocalDateTime inicio, LocalDateTime fin, Pageable pageable);
    Page<Recepcion> findByRunUsuarioAndCodSucursal(String runUsuario, String codSucursal, Pageable pageable);
}
