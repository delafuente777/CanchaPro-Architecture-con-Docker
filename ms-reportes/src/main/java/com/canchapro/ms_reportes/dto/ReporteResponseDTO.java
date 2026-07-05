package com.canchapro.ms_reportes.dto;

import com.canchapro.ms_reportes.entity.EstadoReporte;
import com.canchapro.ms_reportes.entity.TipoReporte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteResponseDTO {

    private Long id;
    private String titulo;
    private TipoReporte tipo;
    private String descripcion;
    private Integer totalRegistros;
    private EstadoReporte estado;
    private LocalDateTime fechaGeneracion;
}