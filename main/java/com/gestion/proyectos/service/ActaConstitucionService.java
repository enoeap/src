package com.gestion.proyectos.service;

import com.gestion.proyectos.model.ActaConstitucion;
import com.gestion.proyectos.model.Documento;
import com.gestion.proyectos.model.Proyecto;
import com.gestion.proyectos.model.Usuario;
import com.gestion.proyectos.repository.ActaConstitucionRepository;
import com.gestion.proyectos.repository.DocumentoRepository;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Optional;

@Service
public class ActaConstitucionService {

    private final ActaConstitucionRepository actaConstitucionRepository;
    private final DocumentoRepository documentoRepository;
    private final ProyectoService proyectoService;

    @Autowired
    public ActaConstitucionService(ActaConstitucionRepository actaConstitucionRepository, 
                                  DocumentoRepository documentoRepository,
                                  ProyectoService proyectoService) {
        this.actaConstitucionRepository = actaConstitucionRepository;
        this.documentoRepository = documentoRepository;
        this.proyectoService = proyectoService;
    }

    public Optional<ActaConstitucion> findById(Long id) {
        return actaConstitucionRepository.findById(id);
    }

    public Optional<ActaConstitucion> findByDocumentoId(Long documentoId) {
        return actaConstitucionRepository.findByDocumentoId(documentoId);
    }

    public Optional<ActaConstitucion> findByProyectoId(Long proyectoId) {
        return actaConstitucionRepository.findByDocumentoProyectoId(proyectoId);
    }

    @Transactional
    public ActaConstitucion save(ActaConstitucion actaConstitucion) {
        return actaConstitucionRepository.save(actaConstitucion);
    }

    @Transactional
    public ActaConstitucion crearActaConstitucion(ActaConstitucion actaConstitucion, Documento documento) {
        Documento savedDocumento = documentoRepository.save(documento);
        actaConstitucion.setDocumento(savedDocumento);
        return actaConstitucionRepository.save(actaConstitucion);
    }

    @Transactional
    public void deleteById(Long id) {
        actaConstitucionRepository.deleteById(id);
    }

    /**
     * Genera un documento Word con el acta de constitución del proyecto
     * @param actaConstitucionId ID del acta de constitución
     * @return Ruta del archivo generado
     * @throws IOException Si ocurre un error al generar el archivo
     */
    public String generarDocumentoWord(Long actaConstitucionId) throws IOException {
        Optional<ActaConstitucion> actaOpt = actaConstitucionRepository.findById(actaConstitucionId);
        if (!actaOpt.isPresent()) {
            throw new RuntimeException("Acta de constitución no encontrada con ID: " + actaConstitucionId);
        }
        
        ActaConstitucion acta = actaOpt.get();
        Documento documento = acta.getDocumento();
        Proyecto proyecto = documento.getProyecto();
        
        // Crear directorio si no existe
        Path dirPath = Paths.get("documentos");
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        
        // Nombre del archivo
        String fileName = "Acta_Constitucion_" + proyecto.getCodigo() + ".docx";
        String filePath = dirPath.resolve(fileName).toString();
        
        // Crear documento Word
        XWPFDocument document = new XWPFDocument();
        
        // Estilos para el documento
        XWPFParagraph titleParagraph = document.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setText("ACTA DE CONSTITUCIÓN DEL PROYECTO");
        titleRun.setBold(true);
        titleRun.setFontSize(16);
        titleRun.addBreak();
        
        // Información del proyecto
        addSectionTitle(document, "1. INFORMACIÓN DEL PROYECTO");
        
        addField(document, "Nombre del Proyecto:", proyecto.getNombre());
        addField(document, "Código:", proyecto.getCodigo());
        addField(document, "Fecha de Inicio:", formatDate(proyecto.getFechaInicio()));
        addField(document, "Fecha de Fin Planificada:", formatDate(proyecto.getFechaFinPlanificada()));
        addField(document, "Patrocinador:", proyecto.getPatrocinador());
        addField(document, "Gerente del Proyecto:", proyecto.getGerente().getNombre() + " " + proyecto.getGerente().getApellido());
        
        // Visión y Alcance
        addSectionTitle(document, "2. VISIÓN DEL PROYECTO");
        addParagraph(document, acta.getVision());
        
        addSectionTitle(document, "3. ALCANCE DEL PROYECTO");
        addParagraph(document, acta.getAlcance());
        
        // Objetivos
        addSectionTitle(document, "4. OBJETIVOS DEL PROYECTO");
        addParagraph(document, proyecto.getObjetivos());
        
        // Justificación
        addSectionTitle(document, "5. JUSTIFICACIÓN DEL PROYECTO");
        addParagraph(document, proyecto.getJustificacion());
        
        // Supuestos y Restricciones
        addSectionTitle(document, "6. SUPUESTOS");
        addParagraph(document, acta.getSupuestos());
        
        addSectionTitle(document, "7. RESTRICCIONES");
        addParagraph(document, acta.getRestricciones());
        
        // Hitos principales
        addSectionTitle(document, "8. HITOS PRINCIPALES");
        addParagraph(document, acta.getHitos());
        
        // Riesgos iniciales
        addSectionTitle(document, "9. RIESGOS INICIALES IDENTIFICADOS");
        addParagraph(document, acta.getRiesgosIniciales());
        
        // Interesados
        addSectionTitle(document, "10. INTERESADOS CLAVE");
        addParagraph(document, acta.getInteresados());
        
        // Presupuesto
        addSectionTitle(document, "11. PRESUPUESTO INICIAL");
        addField(document, "Presupuesto Asignado:", proyecto.getPresupuestoAsignado() + " USD");
        addField(document, "Presupuesto Estimado:", acta.getPresupuestoInicial() + " USD");
        
        // Aprobación
        addSectionTitle(document, "12. APROBACIÓN");
        addField(document, "Aprobado por:", acta.getAprobadoPor());
        addField(document, "Fecha de Aprobación:", formatDate(acta.getFechaAprobacion()));
        
        // Guardar documento
        FileOutputStream out = new FileOutputStream(new File(filePath));
        document.write(out);
        out.close();
        document.close();
        
        // Actualizar ruta del archivo en el documento
        documento.setRutaArchivo(filePath);
        documentoRepository.save(documento);
        
        return filePath;
    }
    
    private void addSectionTitle(XWPFDocument document, String title) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setSpacingAfter(200);
        paragraph.setSpacingBefore(500);
        XWPFRun run = paragraph.createRun();
        run.setText(title);
        run.setBold(true);
        run.setFontSize(14);
        run.addBreak();
    }
    
    private void addField(XWPFDocument document, String fieldName, String fieldValue) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setIndentationLeft(720); // 0.5 inch
        
        XWPFRun fieldRun = paragraph.createRun();
        fieldRun.setText(fieldName);
        fieldRun.setBold(true);
        fieldRun.setFontSize(11);
        
        XWPFRun valueRun = paragraph.createRun();
        valueRun.setText(" " + fieldValue);
        valueRun.setFontSize(11);
        valueRun.addBreak();
    }
    
    private void addParagraph(XWPFDocument document, String text) {
        if (text == null || text.isEmpty()) {
            text = "No especificado";
        }
        
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setIndentationLeft(720); // 0.5 inch
        paragraph.setSpacingAfter(200);
        
        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setFontSize(11);
        run.addBreak();
    }
    
    private String formatDate(java.util.Date date) {
        if (date == null) {
            return "No especificada";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
}
