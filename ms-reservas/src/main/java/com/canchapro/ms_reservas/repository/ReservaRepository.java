package com.canchapro.ms_reservas.repository;

import com.canchapro.ms_reservas.entity.EstadoReserva;
import com.canchapro.ms_reservas.entity.Reserva;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuarioId(Long usuarioId);

    List<Reserva> findByCanchaId(Long canchaId);

    List<Reserva> findByEstado(EstadoReserva estado);

    List<Reserva> findByCanchaIdAndFecha(Long canchaId, LocalDate fecha);

    boolean existsByCanchaIdAndFechaAndHoraInicioAndEstadoIn(
            Long canchaId,
            LocalDate fecha,
            LocalTime horaInicio,
            Collection<EstadoReserva> estados
    );
}