package com.canchapro.ms_calificaciones.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalificacionRequestDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    @Positive(message = "El ID del usuario debe ser mayor que cero")
    private Long usuarioId;

    @NotNull(message = "El ID de la cancha es obligatorio")
    @Positive(message = "El ID de la cancha debe ser mayor que cero")
    private Long canchaId;

    @NotNull(message = "El ID de la reserva es obligatorio")
    @Positive(message = "El ID de la reserva debe ser mayor que cero")
    private Long reservaId;

    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 1, message = "La puntuación mínima es 1")
    @Max(value = 5, message = "La puntuación máxima es 5")
    private Integer puntuacion;

    @Size(max = 500, message = "El comentario no puede superar los 500 caracteres")
    private String comentario;
}