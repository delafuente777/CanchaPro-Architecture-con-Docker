package com.canchapro.ms_disponibilidad.mapper;

import com.canchapro.ms_disponibilidad.dto.DisponibilidadResponseDTO;
import com.canchapro.ms_disponibilidad.entity.Disponibilidad;

import java.util.Objects;

public class DisponibilidadMapper {

    private DisponibilidadMapper() {
    }

    public static DisponibilidadResponseDTO toResponseDTO(
            Disponibilidad disponibilidad
    ) {

        Disponibilidad disponibilidadValidada = Objects.requireNonNull(
                disponibilidad,
                "La disponibilidad no puede ser null"
        );

        return DisponibilidadResponseDTO.builder()
                .id(disponibilidadValidada.getId())
                .canchaId(disponibilidadValidada.getCanchaId())
                .fecha(disponibilidadValidada.getFecha())
                .horaInicio(disponibilidadValidada.getHoraInicio())
                .horaFin(disponibilidadValidada.getHoraFin())
                .estado(disponibilidadValidada.getEstado())
                .build();
    }
}