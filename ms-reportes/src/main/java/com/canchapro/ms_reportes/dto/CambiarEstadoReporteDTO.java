package com.canchapro.ms_reportes.dto;

import com.canchapro.ms_reportes.entity.EstadoReporte;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CambiarEstadoReporteDTO {

    @NotNull(message = "El estado del reporte es obligatorio")
    private EstadoReporte estado;
}