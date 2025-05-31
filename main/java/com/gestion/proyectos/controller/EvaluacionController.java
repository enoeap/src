package com.gestion.proyectos.controller;

import com.gestion.proyectos.model.Evaluacion;
import com.gestion.proyectos.service.EvaluacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/evaluaciones")
public class EvaluacionController {

    private final EvaluacionService evaluacionService;

    @Autowired
    public EvaluacionController(EvaluacionService evaluacionService) {
        this.evaluacionService = evaluacionService;
    }

    @GetMapping
    public ResponseEntity<List<Evaluacion>> getAllEvaluaciones() {
        List<Evaluacion> evaluaciones = evaluacionService.findAll();
        return new ResponseEntity<>(evaluaciones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evaluacion> getEvaluacionById(@PathVariable Long id) {
        return evaluacionService.findById(id)
                .map(evaluacion -> new ResponseEntity<>(evaluacion, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<List<Evaluacion>> getEvaluacionesByProyectoId(@PathVariable Long proyectoId) {
        List<Evaluacion> evaluaciones = evaluacionService.findByProyectoId(proyectoId);
        return new ResponseEntity<>(evaluaciones, HttpStatus.OK);
    }

    @GetMapping("/evaluador/{evaluadorId}")
    public ResponseEntity<List<Evaluacion>> getEvaluacionesByEvaluadorId(@PathVariable Long evaluadorId) {
        List<Evaluacion> evaluaciones = evaluacionService.findByEvaluadorId(evaluadorId);
        return new ResponseEntity<>(evaluaciones, HttpStatus.OK);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Evaluacion>> getEvaluacionesByEstado(@PathVariable Evaluacion.EstadoEvaluacion estado) {
        List<Evaluacion> evaluaciones = evaluacionService.findByEstado(estado);
        return new ResponseEntity<>(evaluaciones, HttpStatus.OK);
    }

    @GetMapping("/proyecto/{proyectoId}/estado/{estado}")
    public ResponseEntity<List<Evaluacion>> getEvaluacionesByProyectoIdAndEstado(
            @PathVariable Long proyectoId, 
            @PathVariable Evaluacion.EstadoEvaluacion estado) {
        List<Evaluacion> evaluaciones = evaluacionService.findByProyectoIdAndEstado(proyectoId, estado);
        return new ResponseEntity<>(evaluaciones, HttpStatus.OK);
    }

    @GetMapping("/rango-fechas")
    public ResponseEntity<List<Evaluacion>> getEvaluacionesByRangoFechas(
            @RequestParam Date fechaInicio, 
            @RequestParam Date fechaFin) {
        List<Evaluacion> evaluaciones = evaluacionService.findByRangoFechas(fechaInicio, fechaFin);
        return new ResponseEntity<>(evaluaciones, HttpStatus.OK);
    }

    @GetMapping("/promedio/{proyectoId}")
    public ResponseEntity<Double> getPromedioPuntuacionProyecto(@PathVariable Long proyectoId) {
        Double promedio = evaluacionService.calcularPromedioPuntuacionProyecto(proyectoId);
        if (promedio != null) {
            return new ResponseEntity<>(promedio, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Evaluacion> createEvaluacion(@Valid @RequestBody Evaluacion evaluacion) {
        Evaluacion nuevaEvaluacion = evaluacionService.save(evaluacion);
        return new ResponseEntity<>(nuevaEvaluacion, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evaluacion> updateEvaluacion(@PathVariable Long id, @Valid @RequestBody Evaluacion evaluacion) {
        return evaluacionService.findById(id)
                .map(existingEvaluacion -> {
                    evaluacion.setId(id);
                    Evaluacion updatedEvaluacion = evaluacionService.save(evaluacion);
                    return new ResponseEntity<>(updatedEvaluacion, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}/estado/{estado}")
    public ResponseEntity<Evaluacion> updateEstadoEvaluacion(
            @PathVariable Long id, 
            @PathVariable Evaluacion.EstadoEvaluacion estado) {
        try {
            Evaluacion updatedEvaluacion = evaluacionService.cambiarEstado(id, estado);
            return new ResponseEntity<>(updatedEvaluacion, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluacion(@PathVariable Long id) {
        return evaluacionService.findById(id)
                .map(evaluacion -> {
                    evaluacionService.deleteById(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
