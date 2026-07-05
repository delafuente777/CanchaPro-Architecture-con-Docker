package com.canchapro.ms_reservas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del usuario es obligatorio")
    @Positive(message = "El ID del usuario debe ser mayor que cero")
    @Column(nullable = false)
    private Long usuarioId;

    @NotNull(message = "El ID de la cancha es obligatorio")
    @Positive(message = "El ID de la cancha debe ser mayor que cero")
    @Column(nullable = false)
    private Long canchaId;

    @NotNull(message = "La fecha de reserva es obligatoria")
    @FutureOrPresent(message = "La fecha no puede ser anterior a hoy")
    @Column(nullable = false)
    private LocalDate fecha;

    @NotNull(message = "La hora de inicio es obligatoria")
    @Column(nullable = false)
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    @Column(nullable = false)
    private LocalTime horaFin;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor que cero")
    @Column(nullable = false)
    private Integer monto;

    @NotNull(message = "El estado de la reserva es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReserva estado;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
}