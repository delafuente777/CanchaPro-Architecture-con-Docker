package com.canchapro.ms_reservas.dto;

import com.canchapro.ms_reservas.entity.EstadoReserva;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaResponseDTO {

    private Long id;
    private Long usuarioId;
    private Long canchaId;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer monto;
    private EstadoReserva estado;
    private LocalDateTime fechaCreacion;
}