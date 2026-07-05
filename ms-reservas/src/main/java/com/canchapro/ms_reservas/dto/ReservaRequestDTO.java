package com.canchapro.ms_reservas.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaRequestDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    @Positive(message = "El ID del usuario debe ser mayor que cero")
    private Long usuarioId;

    @NotNull(message = "El ID de la cancha es obligatorio")
    @Positive(message = "El ID de la cancha debe ser mayor que cero")
    private Long canchaId;

    @NotNull(message = "La fecha de reserva es obligatoria")
    @FutureOrPresent(message = "La fecha no puede ser anterior a hoy")
    private LocalDate fecha;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor que cero")
    private Integer monto;
}