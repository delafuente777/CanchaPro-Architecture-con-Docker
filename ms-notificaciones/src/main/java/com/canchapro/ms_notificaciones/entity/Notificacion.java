package com.canchapro.ms_notificaciones.entity;

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
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del usuario es obligatorio")
    @Positive(message = "El ID del usuario debe ser mayor que cero")
    @Column(nullable = false)
    private Long usuarioId;

    @Positive(message = "El ID de la reserva debe ser mayor que cero")
    private Long reservaId;

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String titulo;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(min = 5, max = 500, message = "El mensaje debe tener entre 5 y 500 caracteres")
    @Column(nullable = false, length = 500)
    private String mensaje;

    @NotNull(message = "El tipo de notificación es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoNotificacion tipo;

    @NotNull(message = "El estado de notificación es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoNotificacion estado;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaEnvio;
}