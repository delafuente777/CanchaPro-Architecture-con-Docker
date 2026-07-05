package com.canchapro.ms_reportes.dto;

import com.canchapro.ms_reportes.entity.TipoReporte;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReporteRequestDTO {

    @NotBlank(message = "El título del reporte es obligatorio")
    @Size(min = 3, max = 120, message = "El título debe tener entre 3 y 120 caracteres")
    private String titulo;

    @NotNull(message = "El tipo de reporte es obligatorio")
    private TipoReporte tipo;

    @NotBlank(message = "La descripción del reporte es obligatoria")
    @Size(min = 5, max = 500, message = "La descripción debe tener entre 5 y 500 caracteres")
    private String descripcion;

    @NotNull(message = "El total de registros es obligatorio")
    @PositiveOrZero(message = "El total de registros no puede ser negativo")
    private Integer totalRegistros;
}