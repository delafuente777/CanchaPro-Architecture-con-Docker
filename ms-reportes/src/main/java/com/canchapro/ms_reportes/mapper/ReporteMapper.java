package com.canchapro.ms_reportes.mapper;

import com.canchapro.ms_reportes.dto.ReporteResponseDTO;
import com.canchapro.ms_reportes.entity.Reporte;

import java.util.Objects;

public class ReporteMapper {

    private ReporteMapper() {
    }

    public static ReporteResponseDTO toResponseDTO(
            Reporte reporte
    ) {

        Reporte reporteValidado = Objects.requireNonNull(
                reporte,
                "El reporte no puede ser null"
        );

        return ReporteResponseDTO.builder()
                .id(reporteValidado.getId())
                .titulo(reporteValidado.getTitulo())
                .tipo(reporteValidado.getTipo())
                .descripcion(reporteValidado.getDescripcion())
                .totalRegistros(reporteValidado.getTotalRegistros())
                .estado(reporteValidado.getEstado())
                .fechaGeneracion(reporteValidado.getFechaGeneracion())
                .build();
    }
}