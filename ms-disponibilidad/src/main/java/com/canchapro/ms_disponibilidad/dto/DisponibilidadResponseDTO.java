package com.canchapro.ms_disponibilidad.dto;

import com.canchapro.ms_disponibilidad.entity.EstadoDisponibilidad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisponibilidadResponseDTO {

    private Long id;
    private Long canchaId;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private EstadoDisponibilidad estado;
}