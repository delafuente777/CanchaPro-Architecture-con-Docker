package com.canchapro.ms_calificaciones.repository;

import com.canchapro.ms_calificaciones.entity.Calificacion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {

    List<Calificacion> findByUsuarioId(Long usuarioId);

    List<Calificacion> findByCanchaId(Long canchaId);

    Optional<Calificacion> findByReservaId(Long reservaId);

    boolean existsByReservaId(Long reservaId);
}