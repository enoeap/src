package com.gestion.proyectos.repository;

import com.gestion.proyectos.model.Miembro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MiembroRepository extends JpaRepository<Miembro, Long> {
    
    List<Miembro> findByProyectoId(Long proyectoId);
    
    List<Miembro> findByUsuarioId(Long usuarioId);
    
    List<Miembro> findByProyectoIdAndActivo(Long proyectoId, Boolean activo);
    
    Optional<Miembro> findByProyectoIdAndUsuarioId(Long proyectoId, Long usuarioId);
    
    @Query("SELECT m FROM Miembro m WHERE m.proyecto.id = :proyectoId AND m.rol = :rol")
    List<Miembro> findByProyectoIdAndRol(Long proyectoId, String rol);
    
    @Query("SELECT COUNT(m) FROM Miembro m WHERE m.proyecto.id = :proyectoId AND m.activo = true")
    Integer countActiveMiembrosByProyectoId(Long proyectoId);
}
