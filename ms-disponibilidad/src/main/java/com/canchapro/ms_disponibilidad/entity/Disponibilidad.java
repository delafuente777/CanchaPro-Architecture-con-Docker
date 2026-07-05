package com.canchapro.ms_disponibilidad.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(
        name = "disponibilidades",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_disponibilidad_cancha_fecha_hora",
                        columnNames = {"cancha_id", "fecha", "hora_inicio"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Disponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID de la cancha es obligatorio")
    @Positive(message = "El ID de la cancha debe ser mayor que cero")
    @Column(name = "cancha_id", nullable = false)
    private Long canchaId;

    @NotNull(message = "La fecha es obligatoria")
    @FutureOrPresent(message = "La fecha no puede ser anterior a hoy")
    @Column(nullable = false)
    private LocalDate fecha;

    @NotNull(message = "La hora de inicio es obligatoria")
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDisponibilidad estado;
}