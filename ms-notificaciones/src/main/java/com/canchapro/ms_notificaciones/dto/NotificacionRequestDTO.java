package com.canchapro.ms_notificaciones.dto;

import com.canchapro.ms_notificaciones.entity.TipoNotificacion;

import jakarta.validation.constraints.NotBlank;
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
public class NotificacionRequestDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    @Positive(message = "El ID del usuario debe ser mayor que cero")
    private Long usuarioId;

    @Positive(message = "El ID de la reserva debe ser mayor que cero")
    private Long reservaId;

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    private String titulo;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(min = 5, max = 500, message = "El mensaje debe tener entre 5 y 500 caracteres")
    private String mensaje;

    @NotNull(message = "El tipo de notificación es obligatorio")
    private TipoNotificacion tipo;
}