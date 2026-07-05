package com.canchapro.ms_calificaciones.mapper;

import com.canchapro.ms_calificaciones.dto.CalificacionResponseDTO;
import com.canchapro.ms_calificaciones.entity.Calificacion;

import java.util.Objects;

public class CalificacionMapper {

    private CalificacionMapper() {
    }

    public static CalificacionResponseDTO toResponseDTO(
            Calificacion calificacion
    ) {

        Calificacion calificacionValidada = Objects.requireNonNull(
                calificacion,
                "La calificación no puede ser null"
        );

        return CalificacionResponseDTO.builder()
                .id(calificacionValidada.getId())
                .usuarioId(calificacionValidada.getUsuarioId())
                .canchaId(calificacionValidada.getCanchaId())
                .reservaId(calificacionValidada.getReservaId())
                .puntuacion(calificacionValidada.getPuntuacion())
                .comentario(calificacionValidada.getComentario())
                .fechaCreacion(calificacionValidada.getFechaCreacion())
                .build();
    }
}