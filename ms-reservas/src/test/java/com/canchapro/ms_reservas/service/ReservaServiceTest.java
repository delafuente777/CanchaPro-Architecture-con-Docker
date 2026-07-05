package com.canchapro.ms_reservas.service;

import com.canchapro.ms_reservas.client.CanchaClient;
import com.canchapro.ms_reservas.client.DisponibilidadClient;
import com.canchapro.ms_reservas.client.PagoClient;
import com.canchapro.ms_reservas.client.UsuarioClient;
import com.canchapro.ms_reservas.dto.CambiarEstadoReservaDTO;
import com.canchapro.ms_reservas.dto.ReservaRequestDTO;
import com.canchapro.ms_reservas.dto.ReservaResponseDTO;
import com.canchapro.ms_reservas.entity.EstadoReserva;
import com.canchapro.ms_reservas.entity.Reserva;
import com.canchapro.ms_reservas.exception.ReservaException;
import com.canchapro.ms_reservas.exception.ReservaNoEncontradaException;
import com.canchapro.ms_reservas.repository.ReservaRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository repository;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private CanchaClient canchaClient;

    @Mock
    private DisponibilidadClient disponibilidadClient;

    @Mock
    private PagoClient pagoClient;

    @InjectMocks
    private ReservaService service;

    @Test
    void crearDebeGuardarReservaCuandoDatosSonValidosYHorarioDisponible() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime horaInicio = LocalTime.of(18, 0);
        LocalTime horaFin = LocalTime.of(19, 0);

        ReservaRequestDTO request = new ReservaRequestDTO(
                1L,
                1L,
                fecha,
                horaInicio,
                horaFin,
                25000
        );

        Reserva reservaGuardada = Reserva.builder()
                .id(1L)
                .usuarioId(1L)
                .canchaId(1L)
                .fecha(fecha)
                .horaInicio(horaInicio)
                .horaFin(horaFin)
                .monto(25000)
                .estado(EstadoReserva.PENDIENTE)
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 18, 30))
                .build();

        when(usuarioClient.buscarUsuarioPorId(1L))
                .thenReturn(new Object());

        when(canchaClient.buscarCanchaPorId(1L))
                .thenReturn(new Object());

        when(repository.existsByCanchaIdAndFechaAndHoraInicioAndEstadoIn(
                eq(1L),
                eq(fecha),
                eq(horaInicio),
                anyCollection()
        )).thenReturn(false);

        when(disponibilidadClient.consultarDisponible(
                1L,
                fecha.toString(),
                horaInicio.toString()
        )).thenReturn(true);

        when(disponibilidadClient.reservarHorario(
                1L,
                fecha.toString(),
                horaInicio.toString()
        )).thenReturn(new Object());

        when(repository.save(any(Reserva.class)))
                .thenReturn(reservaGuardada);

        ReservaResponseDTO respuesta = service.crear(request);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals(1L, respuesta.getUsuarioId());
        assertEquals(1L, respuesta.getCanchaId());
        assertEquals(fecha, respuesta.getFecha());
        assertEquals(horaInicio, respuesta.getHoraInicio());
        assertEquals(horaFin, respuesta.getHoraFin());
        assertEquals(25000, respuesta.getMonto());
        assertEquals(EstadoReserva.PENDIENTE, respuesta.getEstado());

        verify(usuarioClient).buscarUsuarioPorId(1L);
        verify(canchaClient).buscarCanchaPorId(1L);
        verify(repository).existsByCanchaIdAndFechaAndHoraInicioAndEstadoIn(
                eq(1L),
                eq(fecha),
                eq(horaInicio),
                anyCollection()
        );
        verify(disponibilidadClient).consultarDisponible(
                1L,
                fecha.toString(),
                horaInicio.toString()
        );
        verify(disponibilidadClient).reservarHorario(
                1L,
                fecha.toString(),
                horaInicio.toString()
        );
        verify(repository).save(any(Reserva.class));
    }

    @Test
    void crearDebeLanzarExcepcionCuandoYaExisteReservaActiva() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime horaInicio = LocalTime.of(18, 0);
        LocalTime horaFin = LocalTime.of(19, 0);

        ReservaRequestDTO request = new ReservaRequestDTO(
                1L,
                1L,
                fecha,
                horaInicio,
                horaFin,
                25000
        );

        when(usuarioClient.buscarUsuarioPorId(1L))
                .thenReturn(new Object());

        when(canchaClient.buscarCanchaPorId(1L))
                .thenReturn(new Object());

        when(repository.existsByCanchaIdAndFechaAndHoraInicioAndEstadoIn(
                eq(1L),
                eq(fecha),
                eq(horaInicio),
                anyCollection()
        )).thenReturn(true);

        assertThrows(
                ReservaException.class,
                () -> service.crear(request)
        );

        verify(usuarioClient).buscarUsuarioPorId(1L);
        verify(canchaClient).buscarCanchaPorId(1L);
        verify(repository).existsByCanchaIdAndFechaAndHoraInicioAndEstadoIn(
                eq(1L),
                eq(fecha),
                eq(horaInicio),
                anyCollection()
        );
        verify(disponibilidadClient, never()).consultarDisponible(
                1L,
                fecha.toString(),
                horaInicio.toString()
        );
        verify(repository, never()).save(any(Reserva.class));
    }

    @Test
    void crearDebeLanzarExcepcionCuandoHorarioNoEstaDisponible() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime horaInicio = LocalTime.of(18, 0);
        LocalTime horaFin = LocalTime.of(19, 0);

        ReservaRequestDTO request = new ReservaRequestDTO(
                1L,
                1L,
                fecha,
                horaInicio,
                horaFin,
                25000
        );

        when(usuarioClient.buscarUsuarioPorId(1L))
                .thenReturn(new Object());

        when(canchaClient.buscarCanchaPorId(1L))
                .thenReturn(new Object());

        when(repository.existsByCanchaIdAndFechaAndHoraInicioAndEstadoIn(
                eq(1L),
                eq(fecha),
                eq(horaInicio),
                anyCollection()
        )).thenReturn(false);

        when(disponibilidadClient.consultarDisponible(
                1L,
                fecha.toString(),
                horaInicio.toString()
        )).thenReturn(false);

        assertThrows(
                ReservaException.class,
                () -> service.crear(request)
        );

        verify(disponibilidadClient).consultarDisponible(
                1L,
                fecha.toString(),
                horaInicio.toString()
        );
        verify(disponibilidadClient, never()).reservarHorario(
                1L,
                fecha.toString(),
                horaInicio.toString()
        );
        verify(repository, never()).save(any(Reserva.class));
    }

    @Test
    void crearDebeLanzarExcepcionCuandoHoraFinNoEsPosteriorAHoraInicio() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime horaInicio = LocalTime.of(18, 0);
        LocalTime horaFin = LocalTime.of(18, 0);

        ReservaRequestDTO request = new ReservaRequestDTO(
                1L,
                1L,
                fecha,
                horaInicio,
                horaFin,
                25000
        );

        assertThrows(
                ReservaException.class,
                () -> service.crear(request)
        );

        verify(usuarioClient, never()).buscarUsuarioPorId(1L);
        verify(canchaClient, never()).buscarCanchaPorId(1L);
        verify(repository, never()).save(any(Reserva.class));
    }

    @Test
    void buscarPorIdDebeRetornarReservaCuandoExiste() {
        LocalDate fecha = LocalDate.now().plusDays(1);

        Reserva reserva = Reserva.builder()
                .id(1L)
                .usuarioId(1L)
                .canchaId(1L)
                .fecha(fecha)
                .horaInicio(LocalTime.of(18, 0))
                .horaFin(LocalTime.of(19, 0))
                .monto(25000)
                .estado(EstadoReserva.PENDIENTE)
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 18, 30))
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(reserva));

        ReservaResponseDTO respuesta = service.buscarPorId(1L);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals(1L, respuesta.getUsuarioId());
        assertEquals(1L, respuesta.getCanchaId());
        assertEquals(EstadoReserva.PENDIENTE, respuesta.getEstado());

        verify(repository).findById(1L);
    }

    @Test
    void buscarPorIdDebeLanzarExcepcionCuandoNoExiste() {
        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ReservaNoEncontradaException.class,
                () -> service.buscarPorId(99L)
        );

        verify(repository).findById(99L);
    }

    @Test
    void cancelarDebeCambiarEstadoACanceladaYLiberarHorario() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime horaInicio = LocalTime.of(18, 0);

        Reserva reserva = Reserva.builder()
                .id(1L)
                .usuarioId(1L)
                .canchaId(1L)
                .fecha(fecha)
                .horaInicio(horaInicio)
                .horaFin(LocalTime.of(19, 0))
                .monto(25000)
                .estado(EstadoReserva.PENDIENTE)
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 18, 30))
                .build();

        Reserva reservaCancelada = Reserva.builder()
                .id(1L)
                .usuarioId(1L)
                .canchaId(1L)
                .fecha(fecha)
                .horaInicio(horaInicio)
                .horaFin(LocalTime.of(19, 0))
                .monto(25000)
                .estado(EstadoReserva.CANCELADA)
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 18, 30))
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(reserva));

        when(disponibilidadClient.liberarHorario(
                1L,
                fecha.toString(),
                horaInicio.toString()
        )).thenReturn(new Object());

        when(repository.save(any(Reserva.class)))
                .thenReturn(reservaCancelada);

        ReservaResponseDTO respuesta = service.cancelar(1L);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals(EstadoReserva.CANCELADA, respuesta.getEstado());

        verify(repository).findById(1L);
        verify(disponibilidadClient).liberarHorario(
                1L,
                fecha.toString(),
                horaInicio.toString()
        );
        verify(repository).save(any(Reserva.class));
    }

    @Test
    void cancelarDebeLanzarExcepcionCuandoReservaEstaFinalizada() {
        LocalDate fecha = LocalDate.now().plusDays(1);

        Reserva reserva = Reserva.builder()
                .id(1L)
                .usuarioId(1L)
                .canchaId(1L)
                .fecha(fecha)
                .horaInicio(LocalTime.of(18, 0))
                .horaFin(LocalTime.of(19, 0))
                .monto(25000)
                .estado(EstadoReserva.FINALIZADA)
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 18, 30))
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(reserva));

        assertThrows(
                ReservaException.class,
                () -> service.cancelar(1L)
        );

        verify(repository).findById(1L);
        verify(disponibilidadClient, never()).liberarHorario(
                1L,
                fecha.toString(),
                LocalTime.of(18, 0).toString()
        );
        verify(repository, never()).save(any(Reserva.class));
    }

    @Test
    void confirmarPagoDebeCambiarEstadoAPagadaCuandoPagoEsValido() {
        LocalDate fecha = LocalDate.now().plusDays(1);

        Reserva reserva = Reserva.builder()
                .id(1L)
                .usuarioId(1L)
                .canchaId(1L)
                .fecha(fecha)
                .horaInicio(LocalTime.of(18, 0))
                .horaFin(LocalTime.of(19, 0))
                .monto(25000)
                .estado(EstadoReserva.CONFIRMADA)
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 18, 30))
                .build();

        Reserva reservaPagada = Reserva.builder()
                .id(1L)
                .usuarioId(1L)
                .canchaId(1L)
                .fecha(fecha)
                .horaInicio(LocalTime.of(18, 0))
                .horaFin(LocalTime.of(19, 0))
                .monto(25000)
                .estado(EstadoReserva.PAGADA)
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 18, 30))
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(reserva));

        when(pagoClient.validarPago(1L))
                .thenReturn(new Object());

        when(repository.save(any(Reserva.class)))
                .thenReturn(reservaPagada);

        ReservaResponseDTO respuesta = service.confirmarPago(1L);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals(EstadoReserva.PAGADA, respuesta.getEstado());

        verify(repository).findById(1L);
        verify(pagoClient).validarPago(1L);
        verify(repository).save(any(Reserva.class));
    }

    @Test
    void cambiarEstadoDebeActualizarEstadoDeReserva() {
        LocalDate fecha = LocalDate.now().plusDays(1);

        Reserva reserva = Reserva.builder()
                .id(1L)
                .usuarioId(1L)
                .canchaId(1L)
                .fecha(fecha)
                .horaInicio(LocalTime.of(18, 0))
                .horaFin(LocalTime.of(19, 0))
                .monto(25000)
                .estado(EstadoReserva.PENDIENTE)
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 18, 30))
                .build();

        Reserva reservaActualizada = Reserva.builder()
                .id(1L)
                .usuarioId(1L)
                .canchaId(1L)
                .fecha(fecha)
                .horaInicio(LocalTime.of(18, 0))
                .horaFin(LocalTime.of(19, 0))
                .monto(25000)
                .estado(EstadoReserva.CONFIRMADA)
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 18, 30))
                .build();

        CambiarEstadoReservaDTO request = new CambiarEstadoReservaDTO(
                EstadoReserva.CONFIRMADA
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(reserva));

        when(repository.save(any(Reserva.class)))
                .thenReturn(reservaActualizada);

        ReservaResponseDTO respuesta = service.cambiarEstado(1L, request);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals(EstadoReserva.CONFIRMADA, respuesta.getEstado());

        verify(repository).findById(1L);
        verify(repository).save(any(Reserva.class));
    }
}