package com.gestion.proyectos.service;

import com.gestion.proyectos.model.Indicador;
import com.gestion.proyectos.repository.IndicadorRepository;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GraficaService {

    private final IndicadorRepository indicadorRepository;
    private final ProyectoService proyectoService;

    @Autowired
    public GraficaService(IndicadorRepository indicadorRepository, ProyectoService proyectoService) {
        this.indicadorRepository = indicadorRepository;
        this.proyectoService = proyectoService;
    }

    /**
     * Genera una gráfica de línea para la evolución de un indicador en el tiempo
     * @param proyectoId ID del proyecto
     * @param tipoIndicador Tipo de indicador
     * @return Ruta del archivo de imagen generado
     * @throws IOException Si ocurre un error al generar la imagen
     */
    public String generarGraficaEvolucionIndicador(Long proyectoId, Indicador.TipoIndicador tipoIndicador) throws IOException {
        List<Indicador> indicadores = indicadorRepository.findByProyectoIdAndTipo(proyectoId, tipoIndicador);
        
        if (indicadores.isEmpty()) {
            throw new RuntimeException("No hay indicadores del tipo " + tipoIndicador + " para el proyecto con ID: " + proyectoId);
        }
        
        // Crear dataset para la gráfica
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Ordenar indicadores por fecha
        indicadores.sort((i1, i2) -> i1.getFechaMedicion().compareTo(i2.getFechaMedicion()));
        
        // Agregar datos al dataset
        for (Indicador indicador : indicadores) {
            String fecha = new java.text.SimpleDateFormat("dd/MM/yyyy").format(indicador.getFechaMedicion());
            dataset.addValue(indicador.getValor(), "Valor", fecha);
            if (indicador.getMeta() != null) {
                dataset.addValue(indicador.getMeta(), "Meta", fecha);
            }
        }
        
        // Crear gráfica
        JFreeChart chart = ChartFactory.createLineChart(
                "Evolución de " + tipoIndicador.name(),
                "Fecha",
                "Valor",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        
        // Guardar gráfica como imagen
        return guardarGrafica(chart, "evolucion_" + tipoIndicador.name() + "_" + proyectoId);
    }
    
    /**
     * Genera una gráfica de barras para comparar indicadores entre proyectos
     * @param proyectoIds Lista de IDs de proyectos a comparar
     * @param tipoIndicador Tipo de indicador
     * @return Ruta del archivo de imagen generado
     * @throws IOException Si ocurre un error al generar la imagen
     */
    public String generarGraficaComparativaProyectos(List<Long> proyectoIds, Indicador.TipoIndicador tipoIndicador) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        for (Long proyectoId : proyectoIds) {
            Indicador indicador = indicadorRepository.findUltimoIndicadorByProyectoAndTipo(proyectoId, tipoIndicador);
            if (indicador != null) {
                String nombreProyecto = proyectoService.findById(proyectoId)
                        .map(p -> p.getCodigo() + " - " + p.getNombre())
                        .orElse("Proyecto " + proyectoId);
                dataset.addValue(indicador.getValor(), tipoIndicador.name(), nombreProyecto);
            }
        }
        
        if (dataset.getRowCount() == 0) {
            throw new RuntimeException("No hay datos disponibles para los proyectos seleccionados");
        }
        
        // Crear gráfica
        JFreeChart chart = ChartFactory.createBarChart(
                "Comparativa de " + tipoIndicador.name() + " entre Proyectos",
                "Proyecto",
                "Valor",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        
        // Guardar gráfica como imagen
        return guardarGrafica(chart, "comparativa_" + tipoIndicador.name());
    }
    
    /**
     * Genera una gráfica de pastel para la distribución de proyectos por estado
     * @return Ruta del archivo de imagen generado
     * @throws IOException Si ocurre un error al generar la imagen
     */
    public String generarGraficaDistribucionEstadoProyectos() throws IOException {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        // Contar proyectos por estado
        Map<String, Long> conteoEstados = proyectoService.findAll().stream()
                .collect(Collectors.groupingBy(p -> p.getEstado().name(), Collectors.counting()));
        
        // Agregar datos al dataset
        conteoEstados.forEach(dataset::setValue);
        
        // Crear gráfica
        JFreeChart chart = ChartFactory.createPieChart(
                "Distribución de Proyectos por Estado",
                dataset,
                true,
                true,
                false
        );
        
        // Guardar gráfica como imagen
        return guardarGrafica(chart, "distribucion_estados");
    }
    
    /**
     * Genera una gráfica radar para visualizar las puntuaciones de evaluación por categoría
     * @param evaluacionService Servicio de evaluaciones
     * @param evaluacionId ID de la evaluación
     * @return Ruta del archivo de imagen generado
     * @throws IOException Si ocurre un error al generar la imagen
     */
    public String generarGraficaRadarEvaluacion(Long evaluacionId) throws IOException {
        // Implementación de gráfica radar con JFreeChart
        // (Nota: JFreeChart no tiene soporte nativo para gráficas radar, 
        // se implementaría con una extensión o librería adicional)
        
        // Por ahora, generamos una gráfica de barras como alternativa
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Aquí se agregarían los datos de la evaluación por categoría
        // Este es un ejemplo simplificado
        dataset.addValue(4.5, "Puntuación", "Alcance");
        dataset.addValue(3.8, "Puntuación", "Tiempo");
        dataset.addValue(4.2, "Puntuación", "Costo");
        dataset.addValue(3.5, "Puntuación", "Calidad");
        dataset.addValue(4.0, "Puntuación", "Riesgo");
        
        // Crear gráfica
        JFreeChart chart = ChartFactory.createBarChart(
                "Evaluación por Categoría",
                "Categoría",
                "Puntuación",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        
        // Guardar gráfica como imagen
        return guardarGrafica(chart, "evaluacion_" + evaluacionId);
    }
    
    /**
     * Método auxiliar para guardar una gráfica como imagen
     * @param chart Gráfica a guardar
     * @param nombreBase Nombre base para el archivo
     * @return Ruta del archivo generado
     * @throws IOException Si ocurre un error al guardar la imagen
     */
    private String guardarGrafica(JFreeChart chart, String nombreBase) throws IOException {
        // Crear directorio si no existe
        Path dirPath = Paths.get("graficas");
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        
        // Nombre del archivo
        String fileName = nombreBase + "_" + System.currentTimeMillis() + ".png";
        String filePath = dirPath.resolve(fileName).toString();
        
        // Guardar gráfica como imagen PNG
        ChartUtils.saveChartAsPNG(new File(filePath), chart, 800, 600);
        
        return filePath;
    }
}
