package com.gestion.proyectos.repository;

import com.gestion.proyectos.model.DetalleEvaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleEvaluacionRepository extends JpaRepository<DetalleEvaluacion, Long> {
    
    List<DetalleEvaluacion> findByEvaluacionId(Long evaluacionId);
    
    List<DetalleEvaluacion> findByCriterioId(Long criterioId);
    
    @Query("SELECT d FROM DetalleEvaluacion d WHERE d.evaluacion.id = :evaluacionId AND d.criterio.categoria = :categoria")
    List<DetalleEvaluacion> findByEvaluacionIdAndCategoria(Long evaluacionId, String categoria);
    
    @Query("SELECT AVG(d.puntuacion) FROM DetalleEvaluacion d WHERE d.evaluacion.proyecto.id = :proyectoId AND d.criterio.id = :criterioId")
    Double calcularPromedioPuntuacionPorCriterio(Long proyectoId, Long criterioId);
}
