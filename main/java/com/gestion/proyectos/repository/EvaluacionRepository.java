package com.gestion.proyectos.repository;

import com.gestion.proyectos.model.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {
    
    List<Evaluacion> findByProyectoId(Long proyectoId);
    
    List<Evaluacion> findByEvaluadorId(Long evaluadorId);
    
    List<Evaluacion> findByEstado(Evaluacion.EstadoEvaluacion estado);
    
    List<Evaluacion> findByProyectoIdAndEstado(Long proyectoId, Evaluacion.EstadoEvaluacion estado);
    
    @Query("SELECT e FROM Evaluacion e WHERE e.fechaEvaluacion BETWEEN :fechaInicio AND :fechaFin")
    List<Evaluacion> findByRangoFechas(Date fechaInicio, Date fechaFin);
    
    @Query("SELECT AVG(e.puntuacionTotal) FROM Evaluacion e WHERE e.proyecto.id = :proyectoId AND e.estado = 'APROBADA'")
    Double calcularPromedioPuntuacionProyecto(Long proyectoId);
}
