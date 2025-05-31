package com.gestion.proyectos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "evaluaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyecto proyecto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluador_id", nullable = false)
    private Usuario evaluador;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date fechaEvaluacion;

    private String periodo;

    @Column(length = 2000)
    private String comentarioGeneral;

    private Double puntuacionTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEvaluacion estado;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date fechaCreacion;

    public enum EstadoEvaluacion {
        BORRADOR, COMPLETADA, REVISADA, APROBADA
    }
}
