package com.gestion.proyectos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "actas_constitucion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ActaConstitucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_id", nullable = false)
    private Documento documento;

    @Column(length = 2000)
    private String vision;

    @Column(length = 2000)
    private String alcance;

    @Column(length = 2000)
    private String supuestos;

    @Column(length = 2000)
    private String restricciones;

    @Column(length = 2000)
    private String hitos;

    @Column(length = 2000)
    private String riesgosIniciales;

    @Column(length = 2000)
    private String interesados;

    @Column(precision = 19, scale = 2)
    private BigDecimal presupuestoInicial;

    private String aprobadoPor;

    @Temporal(TemporalType.DATE)
    private Date fechaAprobacion;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date fechaCreacion;
}
