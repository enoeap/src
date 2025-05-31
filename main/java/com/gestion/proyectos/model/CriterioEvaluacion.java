package com.gestion.proyectos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "criterios_evaluacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CriterioEvaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaCriterio categoria;

    @Column(nullable = false)
    private Double peso;

    @Column(nullable = false)
    private Integer escalaMinima;

    @Column(nullable = false)
    private Integer escalaMaxima;

    @Column(nullable = false)
    private Boolean activo = true;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date fechaCreacion;

    public enum CategoriaCriterio {
        ALCANCE, TIEMPO, COSTO, CALIDAD, RIESGO, RECURSOS
    }
}
