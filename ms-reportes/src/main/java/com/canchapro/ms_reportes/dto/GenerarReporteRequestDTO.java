package com.canchapro.ms_reportes.dto;

import com.canchapro.ms_reportes.entity.TipoReporte;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerarReporteRequestDTO {

    @NotNull(message = "El tipo de reporte es obligatorio")
    private TipoReporte tipo;
}