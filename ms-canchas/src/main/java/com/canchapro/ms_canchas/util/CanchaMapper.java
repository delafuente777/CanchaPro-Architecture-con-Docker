package com.canchapro.ms_canchas.util;

import com.canchapro.ms_canchas.dto.CanchaResponseDTO;
import com.canchapro.ms_canchas.entity.Cancha;

import java.util.Objects;

public class CanchaMapper {

    private CanchaMapper() {
    }

    public static CanchaResponseDTO toResponseDTO(
            Cancha cancha
    ) {

        Cancha canchaValidada = Objects.requireNonNull(
                cancha,
                "La cancha no puede ser null"
        );

        return CanchaResponseDTO.builder()
                .id(canchaValidada.getId())
                .nombre(canchaValidada.getNombre())
                .tipo(canchaValidada.getTipo())
                .ubicacion(canchaValidada.getUbicacion())
                .precio(canchaValidada.getPrecio())
                .estado(canchaValidada.getEstado())
                .build();
    }
}