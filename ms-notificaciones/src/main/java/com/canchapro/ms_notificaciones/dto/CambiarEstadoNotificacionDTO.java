package com.canchapro.ms_notificaciones.dto;

import com.canchapro.ms_notificaciones.entity.EstadoNotificacion;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CambiarEstadoNotificacionDTO {

    @NotNull(message = "El estado de la notificación es obligatorio")
    private EstadoNotificacion estado;
}