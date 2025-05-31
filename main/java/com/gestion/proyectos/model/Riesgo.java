package com.gestion.proyectos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "riesgos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Riesgo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyecto proyecto;

    @Column(length = 1000, nullable = false)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProbabilidadRiesgo probabilidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImpactoRiesgo impacto;

    @Column(nullable = false)
    private Double nivelRiesgo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstrategiaRiesgo estrategia;

    @Column(length = 1000)
    private String planRespuesta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoRiesgo estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id")
    private Usuario responsable;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date fechaIdentificacion;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date fechaActualizacion;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date fechaCreacion;

    public enum ProbabilidadRiesgo {
        MUY_BAJA, BAJA, MEDIA, ALTA, MUY_ALTA
    }

    public enum ImpactoRiesgo {
        MUY_BAJO, BAJO, MEDIO, ALTO, MUY_ALTO
    }

    public enum EstrategiaRiesgo {
        EVITAR, TRANSFERIR, MITIGAR, ACEPTAR
    }

    public enum EstadoRiesgo {
        IDENTIFICADO, EN_SEGUIMIENTO, MATERIALIZADO, CERRADO
    }
}
