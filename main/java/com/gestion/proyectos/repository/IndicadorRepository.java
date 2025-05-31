package com.gestion.proyectos.repository;

import com.gestion.proyectos.model.Indicador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IndicadorRepository extends JpaRepository<Indicador, Long> {
    
    List<Indicador> findByProyectoId(Long proyectoId);
    
    List<Indicador> findByTipo(Indicador.TipoIndicador tipo);
    
    List<Indicador> findByProyectoIdAndTipo(Long proyectoId, Indicador.TipoIndicador tipo);
    
    @Query("SELECT i FROM Indicador i WHERE i.fechaMedicion BETWEEN :fechaInicio AND :fechaFin")
    List<Indicador> findByRangoFechas(Date fechaInicio, Date fechaFin);
    
    @Query("SELECT i FROM Indicador i WHERE i.proyecto.id = :proyectoId AND i.fechaMedicion = (SELECT MAX(i2.fechaMedicion) FROM Indicador i2 WHERE i2.proyecto.id = :proyectoId AND i2.tipo = :tipo)")
    Indicador findUltimoIndicadorByProyectoAndTipo(Long proyectoId, Indicador.TipoIndicador tipo);
}
