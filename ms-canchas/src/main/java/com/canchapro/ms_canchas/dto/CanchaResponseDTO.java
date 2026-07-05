package com.canchapro.ms_canchas.dto;

import com.canchapro.ms_canchas.entity.EstadoCancha;
import com.canchapro.ms_canchas.entity.TipoCancha;

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
public class CanchaResponseDTO {

    private Long id;
    private String nombre;
    private TipoCancha tipo;
    private String ubicacion;
    private Integer precio;
    private EstadoCancha estado;
}