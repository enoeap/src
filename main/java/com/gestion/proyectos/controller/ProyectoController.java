package com.gestion.proyectos.controller;

import com.gestion.proyectos.model.Proyecto;
import com.gestion.proyectos.service.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;

    @Autowired
    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @GetMapping
    public ResponseEntity<List<Proyecto>> getAllProyectos() {
        List<Proyecto> proyectos = proyectoService.findAll();
        return new ResponseEntity<>(proyectos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> getProyectoById(@PathVariable Long id) {
        return proyectoService.findById(id)
                .map(proyecto -> new ResponseEntity<>(proyecto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Proyecto> getProyectoByCodigo(@PathVariable String codigo) {
        return proyectoService.findByCodigo(codigo)
                .map(proyecto -> new ResponseEntity<>(proyecto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Proyecto>> getProyectosByEstado(@PathVariable Proyecto.EstadoProyecto estado) {
        List<Proyecto> proyectos = proyectoService.findByEstado(estado);
        return new ResponseEntity<>(proyectos, HttpStatus.OK);
    }

    @GetMapping("/fase/{fase}")
    public ResponseEntity<List<Proyecto>> getProyectosByFase(@PathVariable Proyecto.FaseProyecto fase) {
        List<Proyecto> proyectos = proyectoService.findByFase(fase);
        return new ResponseEntity<>(proyectos, HttpStatus.OK);
    }

    @GetMapping("/gerente/{gerenteId}")
    public ResponseEntity<List<Proyecto>> getProyectosByGerenteId(@PathVariable Long gerenteId) {
        List<Proyecto> proyectos = proyectoService.findByGerenteId(gerenteId);
        return new ResponseEntity<>(proyectos, HttpStatus.OK);
    }

    @GetMapping("/retrasados")
    public ResponseEntity<List<Proyecto>> getProyectosRetrasados() {
        List<Proyecto> proyectos = proyectoService.findProyectosRetrasados();
        return new ResponseEntity<>(proyectos, HttpStatus.OK);
    }

    @GetMapping("/sobrecosto")
    public ResponseEntity<List<Proyecto>> getProyectosSobreCosto() {
        List<Proyecto> proyectos = proyectoService.findProyectosSobreCosto();
        return new ResponseEntity<>(proyectos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Proyecto> createProyecto(@Valid @RequestBody Proyecto proyecto) {
        Proyecto nuevoProyecto = proyectoService.save(proyecto);
        return new ResponseEntity<>(nuevoProyecto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proyecto> updateProyecto(@PathVariable Long id, @Valid @RequestBody Proyecto proyecto) {
        return proyectoService.findById(id)
                .map(existingProyecto -> {
                    proyecto.setId(id);
                    Proyecto updatedProyecto = proyectoService.save(proyecto);
                    return new ResponseEntity<>(updatedProyecto, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}/estado/{estado}")
    public ResponseEntity<Proyecto> updateEstadoProyecto(@PathVariable Long id, @PathVariable Proyecto.EstadoProyecto estado) {
        try {
            Proyecto updatedProyecto = proyectoService.actualizarEstado(id, estado);
            return new ResponseEntity<>(updatedProyecto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/fase/{fase}")
    public ResponseEntity<Proyecto> updateFaseProyecto(@PathVariable Long id, @PathVariable Proyecto.FaseProyecto fase) {
        try {
            Proyecto updatedProyecto = proyectoService.actualizarFase(id, fase);
            return new ResponseEntity<>(updatedProyecto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProyecto(@PathVariable Long id) {
        return proyectoService.findById(id)
                .map(proyecto -> {
                    proyectoService.deleteById(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
