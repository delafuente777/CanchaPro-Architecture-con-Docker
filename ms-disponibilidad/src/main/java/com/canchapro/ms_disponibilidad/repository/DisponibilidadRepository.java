package com.canchapro.ms_disponibilidad.repository;

import com.canchapro.ms_disponibilidad.entity.Disponibilidad;
import com.canchapro.ms_disponibilidad.entity.EstadoDisponibilidad;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {

    boolean existsByCanchaIdAndFechaAndHoraInicio(
            Long canchaId,
            LocalDate fecha,
            LocalTime horaInicio
    );

    Optional<Disponibilidad> findByCanchaIdAndFechaAndHoraInicio(
            Long canchaId,
            LocalDate fecha,
            LocalTime horaInicio
    );

    List<Disponibilidad> findByCanchaIdAndFecha(
            Long canchaId,
            LocalDate fecha
    );

    List<Disponibilidad> findByEstado(
            EstadoDisponibilidad estado
    );
}