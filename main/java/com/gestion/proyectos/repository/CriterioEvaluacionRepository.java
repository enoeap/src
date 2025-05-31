package com.gestion.proyectos.repository;

import com.gestion.proyectos.model.CriterioEvaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CriterioEvaluacionRepository extends JpaRepository<CriterioEvaluacion, Long> {
    
    List<CriterioEvaluacion> findByCategoria(CriterioEvaluacion.CategoriaCriterio categoria);
    
    List<CriterioEvaluacion> findByActivo(Boolean activo);
    
    List<CriterioEvaluacion> findByCategoriaAndActivo(CriterioEvaluacion.CategoriaCriterio categoria, Boolean activo);
}
