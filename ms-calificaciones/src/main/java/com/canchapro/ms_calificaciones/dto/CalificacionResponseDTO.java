package com.canchapro.ms_calificaciones.dto;

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
public class CalificacionResponseDTO {

    private Long id;
    private Long usuarioId;
    private Long canchaId;
    private Long reservaId;
    private Integer puntuacion;
    private String comentario;
    private LocalDateTime fechaCreacion;
}