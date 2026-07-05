package com.canchapro.ms_calificaciones.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "calificaciones",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_calificacion_reserva",
                        columnNames = {"reserva_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del usuario es obligatorio")
    @Positive(message = "El ID del usuario debe ser mayor que cero")
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @NotNull(message = "El ID de la cancha es obligatorio")
    @Positive(message = "El ID de la cancha debe ser mayor que cero")
    @Column(name = "cancha_id", nullable = false)
    private Long canchaId;

    @NotNull(message = "El ID de la reserva es obligatorio")
    @Positive(message = "El ID de la reserva debe ser mayor que cero")
    @Column(name = "reserva_id", nullable = false)
    private Long reservaId;

    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 1, message = "La puntuación mínima es 1")
    @Max(value = 5, message = "La puntuación máxima es 5")
    @Column(nullable = false)
    private Integer puntuacion;

    @Size(max = 500, message = "El comentario no puede superar los 500 caracteres")
    @Column(length = 500)
    private String comentario;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
}