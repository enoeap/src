package com.gestion.proyectos.service;

import com.gestion.proyectos.model.Evaluacion;
import com.gestion.proyectos.repository.EvaluacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EvaluacionService {

    private final EvaluacionRepository evaluacionRepository;

    @Autowired
    public EvaluacionService(EvaluacionRepository evaluacionRepository) {
        this.evaluacionRepository = evaluacionRepository;
    }

    public List<Evaluacion> findAll() {
        return evaluacionRepository.findAll();
    }

    public Optional<Evaluacion> findById(Long id) {
        return evaluacionRepository.findById(id);
    }

    public List<Evaluacion> findByProyectoId(Long proyectoId) {
        return evaluacionRepository.findByProyectoId(proyectoId);
    }

    public List<Evaluacion> findByEvaluadorId(Long evaluadorId) {
        return evaluacionRepository.findByEvaluadorId(evaluadorId);
    }

    public List<Evaluacion> findByEstado(Evaluacion.EstadoEvaluacion estado) {
        return evaluacionRepository.findByEstado(estado);
    }

    public List<Evaluacion> findByProyectoIdAndEstado(Long proyectoId, Evaluacion.EstadoEvaluacion estado) {
        return evaluacionRepository.findByProyectoIdAndEstado(proyectoId, estado);
    }

    public List<Evaluacion> findByRangoFechas(Date fechaInicio, Date fechaFin) {
        return evaluacionRepository.findByRangoFechas(fechaInicio, fechaFin);
    }

    public Double calcularPromedioPuntuacionProyecto(Long proyectoId) {
        return evaluacionRepository.calcularPromedioPuntuacionProyecto(proyectoId);
    }

    @Transactional
    public Evaluacion save(Evaluacion evaluacion) {
        return evaluacionRepository.save(evaluacion);
    }

    @Transactional
    public void deleteById(Long id) {
        evaluacionRepository.deleteById(id);
    }

    @Transactional
    public Evaluacion cambiarEstado(Long id, Evaluacion.EstadoEvaluacion estado) {
        Optional<Evaluacion> evaluacionOpt = evaluacionRepository.findById(id);
        if (evaluacionOpt.isPresent()) {
            Evaluacion evaluacion = evaluacionOpt.get();
            evaluacion.setEstado(estado);
            return evaluacionRepository.save(evaluacion);
        }
        throw new RuntimeException("Evaluación no encontrada con ID: " + id);
    }
}
