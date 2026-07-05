package com.canchapro.ms_notificaciones.mapper;

import com.canchapro.ms_notificaciones.dto.NotificacionResponseDTO;
import com.canchapro.ms_notificaciones.entity.Notificacion;

import java.util.Objects;

public class NotificacionMapper {

    private NotificacionMapper() {
    }

    public static NotificacionResponseDTO toResponseDTO(
            Notificacion notificacion
    ) {

        Notificacion notificacionValidada = Objects.requireNonNull(
                notificacion,
                "La notificación no puede ser null"
        );

        return NotificacionResponseDTO.builder()
                .id(notificacionValidada.getId())
                .usuarioId(notificacionValidada.getUsuarioId())
                .reservaId(notificacionValidada.getReservaId())
                .titulo(notificacionValidada.getTitulo())
                .mensaje(notificacionValidada.getMensaje())
                .tipo(notificacionValidada.getTipo())
                .estado(notificacionValidada.getEstado())
                .fechaCreacion(notificacionValidada.getFechaCreacion())
                .fechaEnvio(notificacionValidada.getFechaEnvio())
                .build();
    }
}