package com.gestion.proyectos.repository;

import com.gestion.proyectos.model.Riesgo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiesgoRepository extends JpaRepository<Riesgo, Long> {
    
    List<Riesgo> findByProyectoId(Long proyectoId);
    
    List<Riesgo> findByEstado(Riesgo.EstadoRiesgo estado);
    
    List<Riesgo> findByProyectoIdAndEstado(Long proyectoId, Riesgo.EstadoRiesgo estado);
    
    List<Riesgo> findByResponsableId(Long responsableId);
    
    @Query("SELECT r FROM Riesgo r WHERE r.nivelRiesgo >= :umbral ORDER BY r.nivelRiesgo DESC")
    List<Riesgo> findRiesgosAltos(Double umbral);
    
    @Query("SELECT COUNT(r) FROM Riesgo r WHERE r.proyecto.id = :proyectoId AND r.estado = 'MATERIALIZADO'")
    Integer countRiesgosMaterializados(Long proyectoId);
}
