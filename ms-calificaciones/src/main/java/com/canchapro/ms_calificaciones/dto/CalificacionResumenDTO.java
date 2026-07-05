package com.canchapro.ms_calificaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalificacionResumenDTO {

    private Long canchaId;
    private Double promedio;
    private Integer totalCalificaciones;
}