package com.canchapro.ms_reportes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reportes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título del reporte es obligatorio")
    @Size(min = 3, max = 120, message = "El título debe tener entre 3 y 120 caracteres")
    @Column(nullable = false, length = 120)
    private String titulo;

    @NotNull(message = "El tipo de reporte es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoReporte tipo;

    @NotBlank(message = "La descripción del reporte es obligatoria")
    @Size(min = 5, max = 500, message = "La descripción debe tener entre 5 y 500 caracteres")
    @Column(nullable = false, length = 500)
    private String descripcion;

    @NotNull(message = "El total de registros es obligatorio")
    @PositiveOrZero(message = "El total de registros no puede ser negativo")
    @Column(nullable = false)
    private Integer totalRegistros;

    @NotNull(message = "El estado del reporte es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReporte estado;

    @Column(nullable = false)
    private LocalDateTime fechaGeneracion;
}