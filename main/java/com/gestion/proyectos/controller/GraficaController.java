package com.gestion.proyectos.controller;

import com.gestion.proyectos.model.Indicador;
import com.gestion.proyectos.service.GraficaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/graficas")
public class GraficaController {

    private final GraficaService graficaService;

    @Autowired
    public GraficaController(GraficaService graficaService) {
        this.graficaService = graficaService;
    }

    @GetMapping("/evolucion/{proyectoId}/{tipoIndicador}")
    public ResponseEntity<Resource> generarGraficaEvolucionIndicador(
            @PathVariable Long proyectoId,
            @PathVariable Indicador.TipoIndicador tipoIndicador) {
        try {
            String filePath = graficaService.generarGraficaEvolucionIndicador(proyectoId, tipoIndicador);
            Path path = Paths.get(filePath);
            Resource resource = new UrlResource(path.toUri());
            
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + path.getFileName().toString() + "\"")
                    .body(resource);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/comparativa/{tipoIndicador}")
    public ResponseEntity<Resource> generarGraficaComparativaProyectos(
            @PathVariable Indicador.TipoIndicador tipoIndicador,
            @RequestBody List<Long> proyectoIds) {
        try {
            String filePath = graficaService.generarGraficaComparativaProyectos(proyectoIds, tipoIndicador);
            Path path = Paths.get(filePath);
            Resource resource = new UrlResource(path.toUri());
            
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + path.getFileName().toString() + "\"")
                    .body(resource);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/distribucion-estados")
    public ResponseEntity<Resource> generarGraficaDistribucionEstadoProyectos() {
        try {
            String filePath = graficaService.generarGraficaDistribucionEstadoProyectos();
            Path path = Paths.get(filePath);
            Resource resource = new UrlResource(path.toUri());
            
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + path.getFileName().toString() + "\"")
                    .body(resource);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/evaluacion/{evaluacionId}")
    public ResponseEntity<Resource> generarGraficaRadarEvaluacion(@PathVariable Long evaluacionId) {
        try {
            String filePath = graficaService.generarGraficaRadarEvaluacion(evaluacionId);
            Path path = Paths.get(filePath);
            Resource resource = new UrlResource(path.toUri());
            
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + path.getFileName().toString() + "\"")
                    .body(resource);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
