package com.canchapro.ms_reservas.mapper;

import com.canchapro.ms_reservas.dto.ReservaResponseDTO;
import com.canchapro.ms_reservas.entity.Reserva;

import java.util.Objects;

public class ReservaMapper {

    private ReservaMapper() {
    }

    public static ReservaResponseDTO toResponseDTO(
            Reserva reserva
    ) {

        Reserva reservaValidada = Objects.requireNonNull(
                reserva,
                "La reserva no puede ser null"
        );

        return ReservaResponseDTO.builder()
                .id(reservaValidada.getId())
                .usuarioId(reservaValidada.getUsuarioId())
                .canchaId(reservaValidada.getCanchaId())
                .fecha(reservaValidada.getFecha())
                .horaInicio(reservaValidada.getHoraInicio())
                .horaFin(reservaValidada.getHoraFin())
                .monto(reservaValidada.getMonto())
                .estado(reservaValidada.getEstado())
                .fechaCreacion(reservaValidada.getFechaCreacion())
                .build();
    }
}