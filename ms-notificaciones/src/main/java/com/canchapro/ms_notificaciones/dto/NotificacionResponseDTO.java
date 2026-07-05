package com.canchapro.ms_notificaciones.dto;

import com.canchapro.ms_notificaciones.entity.EstadoNotificacion;
import com.canchapro.ms_notificaciones.entity.TipoNotificacion;

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
public class NotificacionResponseDTO {

    private Long id;
    private Long usuarioId;
    private Long reservaId;
    private String titulo;
    private String mensaje;
    private TipoNotificacion tipo;
    private EstadoNotificacion estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEnvio;
}