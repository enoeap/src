package com.gestion.proyectos.repository;

import com.gestion.proyectos.model.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    
    Optional<Proyecto> findByCodigo(String codigo);
    
    List<Proyecto> findByEstado(Proyecto.EstadoProyecto estado);
    
    List<Proyecto> findByFase(Proyecto.FaseProyecto fase);
    
    List<Proyecto> findByPrioridad(Proyecto.PrioridadProyecto prioridad);
    
    List<Proyecto> findByGerenteId(Long gerenteId);
    
    @Query("SELECT p FROM Proyecto p WHERE p.fechaFinPlanificada < CURRENT_DATE AND p.estado <> 'COMPLETADO' AND p.estado <> 'CANCELADO'")
    List<Proyecto> findProyectosRetrasados();
    
    @Query("SELECT p FROM Proyecto p WHERE p.presupuestoGastado > p.presupuestoAsignado")
    List<Proyecto> findProyectosSobreCosto();
}
