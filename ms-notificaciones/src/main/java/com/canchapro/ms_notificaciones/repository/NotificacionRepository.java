package com.canchapro.ms_notificaciones.repository;

import com.canchapro.ms_notificaciones.entity.EstadoNotificacion;
import com.canchapro.ms_notificaciones.entity.Notificacion;
import com.canchapro.ms_notificaciones.entity.TipoNotificacion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByUsuarioId(Long usuarioId);

    List<Notificacion> findByReservaId(Long reservaId);

    List<Notificacion> findByEstado(EstadoNotificacion estado);

    List<Notificacion> findByTipo(TipoNotificacion tipo);

    List<Notificacion> findByUsuarioIdAndEstado(
            Long usuarioId,
            EstadoNotificacion estado
    );
}