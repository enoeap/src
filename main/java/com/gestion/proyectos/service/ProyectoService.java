package com.gestion.proyectos.service;

import com.gestion.proyectos.model.Proyecto;
import com.gestion.proyectos.repository.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;

    @Autowired
    public ProyectoService(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    public List<Proyecto> findAll() {
        return proyectoRepository.findAll();
    }

    public Optional<Proyecto> findById(Long id) {
        return proyectoRepository.findById(id);
    }

    public Optional<Proyecto> findByCodigo(String codigo) {
        return proyectoRepository.findByCodigo(codigo);
    }

    public List<Proyecto> findByEstado(Proyecto.EstadoProyecto estado) {
        return proyectoRepository.findByEstado(estado);
    }

    public List<Proyecto> findByFase(Proyecto.FaseProyecto fase) {
        return proyectoRepository.findByFase(fase);
    }

    public List<Proyecto> findByPrioridad(Proyecto.PrioridadProyecto prioridad) {
        return proyectoRepository.findByPrioridad(prioridad);
    }

    public List<Proyecto> findByGerenteId(Long gerenteId) {
        return proyectoRepository.findByGerenteId(gerenteId);
    }

    public List<Proyecto> findProyectosRetrasados() {
        return proyectoRepository.findProyectosRetrasados();
    }

    public List<Proyecto> findProyectosSobreCosto() {
        return proyectoRepository.findProyectosSobreCosto();
    }

    @Transactional
    public Proyecto save(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    @Transactional
    public void deleteById(Long id) {
        proyectoRepository.deleteById(id);
    }

    @Transactional
    public Proyecto actualizarEstado(Long id, Proyecto.EstadoProyecto estado) {
        Optional<Proyecto> proyectoOpt = proyectoRepository.findById(id);
        if (proyectoOpt.isPresent()) {
            Proyecto proyecto = proyectoOpt.get();
            proyecto.setEstado(estado);
            return proyectoRepository.save(proyecto);
        }
        throw new RuntimeException("Proyecto no encontrado con ID: " + id);
    }

    @Transactional
    public Proyecto actualizarFase(Long id, Proyecto.FaseProyecto fase) {
        Optional<Proyecto> proyectoOpt = proyectoRepository.findById(id);
        if (proyectoOpt.isPresent()) {
            Proyecto proyecto = proyectoOpt.get();
            proyecto.setFase(fase);
            return proyectoRepository.save(proyecto);
        }
        throw new RuntimeException("Proyecto no encontrado con ID: " + id);
    }
}
