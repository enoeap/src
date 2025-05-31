package com.gestion.proyectos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "indicadores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Indicador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyecto proyecto;

    @Column(nullable = false)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoIndicador tipo;

    @Column(nullable = false)
    private Double valor;

    private Double meta;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date fechaMedicion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TendenciaIndicador tendencia;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date fechaCreacion;

    public enum TipoIndicador {
        SPI, CPI, ROI, OTRO
    }

    public enum TendenciaIndicador {
        POSITIVA, NEUTRAL, NEGATIVA
    }
}
