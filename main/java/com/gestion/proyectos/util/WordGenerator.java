package com.gestion.proyectos.util;

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utilidad para generar documentos Word
 */
@Component
public class WordGenerator {

    /**
     * Genera un acta de constitución en formato Word
     * 
     * @param nombreProyecto Nombre del proyecto
     * @param codigo Código del proyecto
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin planificada
     * @param patrocinador Patrocinador del proyecto
     * @param gerente Gerente del proyecto
     * @param vision Visión del proyecto
     * @param alcance Alcance del proyecto
     * @param objetivos Objetivos del proyecto
     * @param justificacion Justificación del proyecto
     * @param supuestos Supuestos del proyecto
     * @param restricciones Restricciones del proyecto
     * @param hitos Hitos principales del proyecto
     * @param riesgos Riesgos iniciales identificados
     * @param interesados Interesados clave del proyecto
     * @param presupuestoAsignado Presupuesto asignado
     * @param presupuestoEstimado Presupuesto estimado
     * @param aprobadoPor Persona que aprueba el acta
     * @param fechaAprobacion Fecha de aprobación
     * @return Ruta del archivo generado
     * @throws IOException Si ocurre un error al generar el archivo
     */
    public String generarActaConstitucion(
            String nombreProyecto,
            String codigo,
            Date fechaInicio,
            Date fechaFin,
            String patrocinador,
            String gerente,
            String vision,
            String alcance,
            String objetivos,
            String justificacion,
            String supuestos,
            String restricciones,
            String hitos,
            String riesgos,
            String interesados,
            String presupuestoAsignado,
            String presupuestoEstimado,
            String aprobadoPor,
            Date fechaAprobacion) throws IOException {
        
        // Crear directorio si no existe
        Path dirPath = Paths.get("documentos");
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        
        // Nombre del archivo
        String fileName = "Acta_Constitucion_" + codigo + ".docx";
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
        
        addField(document, "Nombre del Proyecto:", nombreProyecto);
        addField(document, "Código:", codigo);
        addField(document, "Fecha de Inicio:", formatDate(fechaInicio));
        addField(document, "Fecha de Fin Planificada:", formatDate(fechaFin));
        addField(document, "Patrocinador:", patrocinador);
        addField(document, "Gerente del Proyecto:", gerente);
        
        // Visión y Alcance
        addSectionTitle(document, "2. VISIÓN DEL PROYECTO");
        addParagraph(document, vision);
        
        addSectionTitle(document, "3. ALCANCE DEL PROYECTO");
        addParagraph(document, alcance);
        
        // Objetivos
        addSectionTitle(document, "4. OBJETIVOS DEL PROYECTO");
        addParagraph(document, objetivos);
        
        // Justificación
        addSectionTitle(document, "5. JUSTIFICACIÓN DEL PROYECTO");
        addParagraph(document, justificacion);
        
        // Supuestos y Restricciones
        addSectionTitle(document, "6. SUPUESTOS");
        addParagraph(document, supuestos);
        
        addSectionTitle(document, "7. RESTRICCIONES");
        addParagraph(document, restricciones);
        
        // Hitos principales
        addSectionTitle(document, "8. HITOS PRINCIPALES");
        addParagraph(document, hitos);
        
        // Riesgos iniciales
        addSectionTitle(document, "9. RIESGOS INICIALES IDENTIFICADOS");
        addParagraph(document, riesgos);
        
        // Interesados
        addSectionTitle(document, "10. INTERESADOS CLAVE");
        addParagraph(document, interesados);
        
        // Presupuesto
        addSectionTitle(document, "11. PRESUPUESTO INICIAL");
        addField(document, "Presupuesto Asignado:", presupuestoAsignado);
        addField(document, "Presupuesto Estimado:", presupuestoEstimado);
        
        // Aprobación
        addSectionTitle(document, "12. APROBACIÓN");
        addField(document, "Aprobado por:", aprobadoPor);
        addField(document, "Fecha de Aprobación:", formatDate(fechaAprobacion));
        
        // Guardar documento
        FileOutputStream out = new FileOutputStream(filePath);
        document.write(out);
        out.close();
        document.close();
        
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
    
    private String formatDate(Date date) {
        if (date == null) {
            return "No especificada";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
}
