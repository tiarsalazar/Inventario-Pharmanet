package com.pharmanet.inventario_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmanet.inventario_service.entity.Lote;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Long> {
    Optional<Lote> findByCodLote(String codLote);
    List<Lote> findByInventarioId(Long id);
}
