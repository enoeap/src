package com.gestion.proyectos.repository;

import com.gestion.proyectos.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    
    List<Documento> findByProyectoId(Long proyectoId);
    
    List<Documento> findByTipo(Documento.TipoDocumento tipo);
    
    List<Documento> findByProyectoIdAndTipo(Long proyectoId, Documento.TipoDocumento tipo);
    
    List<Documento> findByCreadoPorId(Long usuarioId);
}
