package com.canchapro.ms_disponibilidad.service;

import com.canchapro.ms_disponibilidad.dto.DisponibilidadRequestDTO;
import com.canchapro.ms_disponibilidad.dto.DisponibilidadResponseDTO;
import com.canchapro.ms_disponibilidad.entity.Disponibilidad;
import com.canchapro.ms_disponibilidad.entity.EstadoDisponibilidad;
import com.canchapro.ms_disponibilidad.exception.DisponibilidadNoEncontradaException;
import com.canchapro.ms_disponibilidad.exception.HorarioNoDisponibleException;
import com.canchapro.ms_disponibilidad.exception.HorarioYaExisteException;
import com.canchapro.ms_disponibilidad.repository.DisponibilidadRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DisponibilidadServiceTest {

    @Mock
    private DisponibilidadRepository repository;

    @InjectMocks
    private DisponibilidadService service;

    @Test
    void crearDebeGuardarDisponibilidadCuandoHorarioEsValidoYNoExiste() {
        LocalDate fecha = LocalDate.of(2026, 7, 1);
        LocalTime horaInicio = LocalTime.of(18, 0);
        LocalTime horaFin = LocalTime.of(19, 0);

        DisponibilidadRequestDTO request = new DisponibilidadRequestDTO(
                1L,
                fecha,
                horaInicio,
                horaFin,
                EstadoDisponibilidad.DISPONIBLE
        );

        Disponibilidad guardada = Disponibilidad.builder()
                .id(1L)
                .canchaId(1L)
                .fecha(fecha)
                .horaInicio(horaInicio)
                .horaFin(horaFin)
                .estado(EstadoDisponibilidad.DISPONIBLE)
                .build();

        when(repository.existsByCanchaIdAndFechaAndHoraInicio(1L, fecha, horaInicio))
                .thenReturn(false);

        when(repository.save(any(Disponibilidad.class)))
                .thenReturn(guardada);

        DisponibilidadResponseDTO respuesta = service.crear(request);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals(1L, respuesta.getCanchaId());
        assertEquals(fecha, respuesta.getFecha());
        assertEquals(horaInicio, respuesta.getHoraInicio());
        assertEquals(horaFin, respuesta.getHoraFin());
        assertEquals(EstadoDisponibilidad.DISPONIBLE, respuesta.getEstado());

        verify(repository).existsByCanchaIdAndFechaAndHoraInicio(1L, fecha, horaInicio);
        verify(repository).save(any(Disponibilidad.class));
    }

    @Test
    void crearDebeLanzarExcepcionCuandoHorarioYaExiste() {
        LocalDate fecha = LocalDate.of(2026, 7, 1);
        LocalTime horaInicio = LocalTime.of(18, 0);
        LocalTime horaFin = LocalTime.of(19, 0);

        DisponibilidadRequestDTO request = new DisponibilidadRequestDTO(
                1L,
                fecha,
                horaInicio,
                horaFin,
                EstadoDisponibilidad.DISPONIBLE
        );

        when(repository.existsByCanchaIdAndFechaAndHoraInicio(1L, fecha, horaInicio))
                .thenReturn(true);

        assertThrows(
                HorarioYaExisteException.class,
                () -> service.crear(request)
        );

        verify(repository).existsByCanchaIdAndFechaAndHoraInicio(1L, fecha, horaInicio);
        verify(repository, never()).save(any(Disponibilidad.class));
    }

    @Test
    void crearDebeLanzarExcepcionCuandoHoraFinNoEsPosteriorAHoraInicio() {
        LocalDate fecha = LocalDate.of(2026, 7, 1);
        LocalTime horaInicio = LocalTime.of(18, 0);
        LocalTime horaFin = LocalTime.of(18, 0);

        DisponibilidadRequestDTO request = new DisponibilidadRequestDTO(
                1L,
                fecha,
                horaInicio,
                horaFin,
                EstadoDisponibilidad.DISPONIBLE
        );

        assertThrows(
                HorarioNoDisponibleException.class,
                () -> service.crear(request)
        );

        verify(repository, never()).existsByCanchaIdAndFechaAndHoraInicio(1L, fecha, horaInicio);
        verify(repository, never()).save(any(Disponibilidad.class));
    }

    @Test
    void consultarDisponibleDebeRetornarTrueCuandoHorarioEstaDisponible() {
        LocalDate fecha = LocalDate.of(2026, 7, 1);
        LocalTime horaInicio = LocalTime.of(18, 0);

        Disponibilidad disponibilidad = Disponibilidad.builder()
                .id(1L)
                .canchaId(1L)
                .fecha(fecha)
                .horaInicio(horaInicio)
                .horaFin(LocalTime.of(19, 0))
                .estado(EstadoDisponibilidad.DISPONIBLE)
                .build();

        when(repository.findByCanchaIdAndFechaAndHoraInicio(1L, fecha, horaInicio))
                .thenReturn(Optional.of(disponibilidad));

        Boolean disponible = service.consultarDisponible(1L, fecha, horaInicio);

        assertTrue(disponible);

        verify(repository).findByCanchaIdAndFechaAndHoraInicio(1L, fecha, horaInicio);
    }

    @Test
    void consultarDisponibleDebeRetornarFalseCuandoNoExisteHorario() {
        LocalDate fecha = LocalDate.of(2026, 7, 1);
        LocalTime horaInicio = LocalTime.of(18, 0);

        when(repository.findByCanchaIdAndFechaAndHoraInicio(1L, fecha, horaInicio))
                .thenReturn(Optional.empty());

        Boolean disponible = service.consultarDisponible(1L, fecha, horaInicio);

        assertFalse(disponible);

        verify(repository).findByCanchaIdAndFechaAndHoraInicio(1L, fecha, horaInicio);
    }

    @Test
    void reservarHorarioDebeCambiarEstadoAReservadoCuandoEstaDisponible() {
        LocalDate fecha = LocalDate.of(2026, 7, 1);
        LocalTime horaInicio = LocalTime.of(18, 0);

        Disponibilidad disponibilidad = Disponibilidad.builder()
                .id(1L)
                .canchaId(1L)
                .fecha(fecha)
                .horaInicio(horaInicio)
                .horaFin(LocalTime.of(19, 0))
                .estado(EstadoDisponibilidad.DISPONIBLE)
                .build();

        Disponibilidad reservada = Disponibilidad.builder()
                .id(1L)
                .canchaId(1L)
                .fecha(fecha)
                .horaInicio(horaInicio)
                .horaFin(LocalTime.of(19, 0))
                .estado(EstadoDisponibilidad.RESERVADO)
                .build();

        when(repository.findByCanchaIdAndFechaAndHoraInicio(1L, fecha, horaInicio))
                .thenReturn(Optional.of(disponibilidad));

        when(repository.save(any(Disponibilidad.class)))
                .thenReturn(reservada);

        DisponibilidadResponseDTO respuesta = service.reservarHorario(1L, fecha, horaInicio);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals(EstadoDisponibilidad.RESERVADO, respuesta.getEstado());

        verify(repository).findByCanchaIdAndFechaAndHoraInicio(1L, fecha, horaInicio);
        verify(repository).save(any(Disponibilidad.class));
    }

    @Test
    void reservarHorarioDebeLanzarExcepcionCuandoNoExisteDisponibilidad() {
        LocalDate fecha = LocalDate.of(2026, 7, 1);
        LocalTime horaInicio = LocalTime.of(18, 0);

        when(repository.findByCanchaIdAndFechaAndHoraInicio(1L, fecha, horaInicio))
                .thenReturn(Optional.empty());

        assertThrows(
                DisponibilidadNoEncontradaException.class,
                () -> service.reservarHorario(1L, fecha, horaInicio)
        );

        verify(repository).findByCanchaIdAndFechaAndHoraInicio(1L, fecha, horaInicio);
        verify(repository, never()).save(any(Disponibilidad.class));
    }
}