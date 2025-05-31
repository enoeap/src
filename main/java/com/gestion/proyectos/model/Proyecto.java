package com.gestion.proyectos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "proyectos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigo;

    @Column(nullable = false)
    private String nombre;

    @Column(length = 2000)
    private String descripcion;

    @Column(length = 2000)
    private String objetivos;

    @Column(length = 2000)
    private String justificacion;

    @Temporal(TemporalType.DATE)
    private Date fechaInicio;

    @Temporal(TemporalType.DATE)
    private Date fechaFinPlanificada;

    @Temporal(TemporalType.DATE)
    private Date fechaFinReal;

    @Enumerated(EnumType.STRING)
    private EstadoProyecto estado;

    @Enumerated(EnumType.STRING)
    private FaseProyecto fase;

    @Column(precision = 19, scale = 2)
    private BigDecimal presupuestoAsignado;

    @Column(precision = 19, scale = 2)
    private BigDecimal presupuestoGastado;

    @Enumerated(EnumType.STRING)
    private PrioridadProyecto prioridad;

    private String patrocinador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gerente_id")
    private Usuario gerente;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date fechaCreacion;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date fechaModificacion;

    public enum EstadoProyecto {
        INICIADO, EN_PROGRESO, PAUSADO, CANCELADO, COMPLETADO
    }

    public enum FaseProyecto {
        INICIO, PLANIFICACION, EJECUCION, MONITOREO, CIERRE
    }

    public enum PrioridadProyecto {
        BAJA, MEDIA, ALTA, CRITICA
    }
}
