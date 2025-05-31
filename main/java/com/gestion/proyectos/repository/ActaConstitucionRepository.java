package com.gestion.proyectos.repository;

import com.gestion.proyectos.model.ActaConstitucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActaConstitucionRepository extends JpaRepository<ActaConstitucion, Long> {
    
    Optional<ActaConstitucion> findByDocumentoId(Long documentoId);
    
    Optional<ActaConstitucion> findByDocumentoProyectoId(Long proyectoId);
}
