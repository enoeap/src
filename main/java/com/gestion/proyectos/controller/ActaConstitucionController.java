package com.gestion.proyectos.controller;

import com.gestion.proyectos.model.ActaConstitucion;
import com.gestion.proyectos.model.Documento;
import com.gestion.proyectos.service.ActaConstitucionService;
import com.gestion.proyectos.service.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("/api/actas")
public class ActaConstitucionController {

    private final ActaConstitucionService actaConstitucionService;
    private final DocumentoService documentoService;

    @Autowired
    public ActaConstitucionController(ActaConstitucionService actaConstitucionService, DocumentoService documentoService) {
        this.actaConstitucionService = actaConstitucionService;
        this.documentoService = documentoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActaConstitucion> getActaById(@PathVariable Long id) {
        return actaConstitucionService.findById(id)
                .map(acta -> new ResponseEntity<>(acta, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<ActaConstitucion> getActaByProyectoId(@PathVariable Long proyectoId) {
        return actaConstitucionService.findByProyectoId(proyectoId)
                .map(acta -> new ResponseEntity<>(acta, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ActaConstitucion> createActa(@Valid @RequestBody ActaConstitucion actaConstitucion, @RequestParam Long proyectoId, @RequestParam Long usuarioId) {
        try {
            // Crear documento asociado al acta
            Documento documento = new Documento();
            documento.setProyectoId(proyectoId);
            documento.setTipo(Documento.TipoDocumento.ACTA_CONSTITUCION);
            documento.setNombre("Acta de Constitución");
            documento.setDescripcion("Acta de constitución del proyecto");
            documento.setVersion("1.0");
            documento.setCreadoPorId(usuarioId);
            documento.setModificadoPorId(usuarioId);
            
            // Guardar acta con documento
            ActaConstitucion nuevaActa = actaConstitucionService.crearActaConstitucion(actaConstitucion, documento);
            return new ResponseEntity<>(nuevaActa, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActaConstitucion> updateActa(@PathVariable Long id, @Valid @RequestBody ActaConstitucion actaConstitucion) {
        return actaConstitucionService.findById(id)
                .map(existingActa -> {
                    actaConstitucion.setId(id);
                    actaConstitucion.setDocumento(existingActa.getDocumento());
                    ActaConstitucion updatedActa = actaConstitucionService.save(actaConstitucion);
                    return new ResponseEntity<>(updatedActa, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActa(@PathVariable Long id) {
        return actaConstitucionService.findById(id)
                .map(acta -> {
                    actaConstitucionService.deleteById(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/generar-word")
    public ResponseEntity<Resource> generarDocumentoWord(@PathVariable Long id) {
        try {
            String filePath = actaConstitucionService.generarDocumentoWord(id);
            Path path = Paths.get(filePath);
            Resource resource = new UrlResource(path.toUri());
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + path.getFileName().toString() + "\"")
                    .body(resource);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
