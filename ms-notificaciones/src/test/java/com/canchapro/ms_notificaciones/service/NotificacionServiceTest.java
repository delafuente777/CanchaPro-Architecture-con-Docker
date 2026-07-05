package com.canchapro.ms_notificaciones.service;

import com.canchapro.ms_notificaciones.client.ReservaClient;
import com.canchapro.ms_notificaciones.client.UsuarioClient;
import com.canchapro.ms_notificaciones.dto.CambiarEstadoNotificacionDTO;
import com.canchapro.ms_notificaciones.dto.NotificacionRequestDTO;
import com.canchapro.ms_notificaciones.dto.NotificacionResponseDTO;
import com.canchapro.ms_notificaciones.entity.EstadoNotificacion;
import com.canchapro.ms_notificaciones.entity.Notificacion;
import com.canchapro.ms_notificaciones.entity.TipoNotificacion;
import com.canchapro.ms_notificaciones.exception.NotificacionException;
import com.canchapro.ms_notificaciones.exception.NotificacionNoEncontradaException;
import com.canchapro.ms_notificaciones.repository.NotificacionRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository repository;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private ReservaClient reservaClient;

    @InjectMocks
    private NotificacionService service;

    @Test
    void crearDebeGuardarNotificacionPendienteCuandoUsuarioYReservaSonValidos() {
        NotificacionRequestDTO request = new NotificacionRequestDTO(
                1L,
                1L,
                "Reserva creada",
                "Su reserva fue registrada correctamente",
                TipoNotificacion.RESERVA
        );

        Notificacion notificacionGuardada = Notificacion.builder()
                .id(1L)
                .usuarioId(1L)
                .reservaId(1L)
                .titulo("Reserva creada")
                .mensaje("Su reserva fue registrada correctamente")
                .tipo(TipoNotificacion.RESERVA)
                .estado(EstadoNotificacion.PENDIENTE)
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 18, 30))
                .fechaEnvio(null)
                .build();

        when(usuarioClient.buscarUsuarioPorId(1L))
                .thenReturn(new Object());

        when(reservaClient.buscarReservaPorId(1L))
                .thenReturn(new Object());

        when(repository.save(any(Notificacion.class)))
                .thenReturn(notificacionGuardada);

        NotificacionResponseDTO respuesta = service.crear(request);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals(1L, respuesta.getUsuarioId());
        assertEquals(1L, respuesta.getReservaId());
        assertEquals("Reserva creada", respuesta.getTitulo());
        assertEquals(TipoNotificacion.RESERVA, respuesta.getTipo());
        assertEquals(EstadoNotificacion.PENDIENTE, respuesta.getEstado());

        verify(usuarioClient).buscarUsuarioPorId(1L);
        verify(reservaClient).buscarReservaPorId(1L);
        verify(repository).save(any(Notificacion.class));
    }

    @Test
    void buscarPorIdDebeRetornarNotificacionCuandoExiste() {
        Notificacion notificacion = Notificacion.builder()
                .id(1L)
                .usuarioId(1L)
                .reservaId(1L)
                .titulo("Pago aprobado")
                .mensaje("Su pago fue aprobado")
                .tipo(TipoNotificacion.PAGO)
                .estado(EstadoNotificacion.ENVIADA)
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 18, 30))
                .fechaEnvio(LocalDateTime.of(2026, 7, 1, 18, 35))
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(notificacion));

        NotificacionResponseDTO respuesta = service.buscarPorId(1L);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals("Pago aprobado", respuesta.getTitulo());
        assertEquals(TipoNotificacion.PAGO, respuesta.getTipo());
        assertEquals(EstadoNotificacion.ENVIADA, respuesta.getEstado());

        verify(repository).findById(1L);
    }

    @Test
    void buscarPorIdDebeLanzarExcepcionCuandoNoExiste() {
        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotificacionNoEncontradaException.class,
                () -> service.buscarPorId(99L)
        );

        verify(repository).findById(99L);
    }

    @Test
    void marcarEnviadaDebeCambiarEstadoAEnviada() {
        Notificacion notificacion = Notificacion.builder()
                .id(1L)
                .usuarioId(1L)
                .reservaId(1L)
                .titulo("Recordatorio")
                .mensaje("Recuerde su reserva")
                .tipo(TipoNotificacion.RECORDATORIO)
                .estado(EstadoNotificacion.PENDIENTE)
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 18, 30))
                .fechaEnvio(null)
                .build();

        Notificacion enviada = Notificacion.builder()
                .id(1L)
                .usuarioId(1L)
                .reservaId(1L)
                .titulo("Recordatorio")
                .mensaje("Recuerde su reserva")
                .tipo(TipoNotificacion.RECORDATORIO)
                .estado(EstadoNotificacion.ENVIADA)
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 18, 30))
                .fechaEnvio(LocalDateTime.of(2026, 7, 1, 18, 35))
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(notificacion));

        when(repository.save(any(Notificacion.class)))
                .thenReturn(enviada);

        NotificacionResponseDTO respuesta = service.marcarEnviada(1L);

        assertNotNull(respuesta);
        assertEquals(EstadoNotificacion.ENVIADA, respuesta.getEstado());
        assertNotNull(respuesta.getFechaEnvio());

        verify(repository).findById(1L);
        verify(repository).save(any(Notificacion.class));
    }

    @Test
    void marcarEnviadaDebeLanzarExcepcionCuandoYaEstaLeida() {
        Notificacion notificacion = Notificacion.builder()
                .id(1L)
                .usuarioId(1L)
                .reservaId(1L)
                .titulo("Reserva")
                .mensaje("Mensaje de reserva")
                .tipo(TipoNotificacion.RESERVA)
                .estado(EstadoNotificacion.LEIDA)
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 18, 30))
                .fechaEnvio(LocalDateTime.of(2026, 7, 1, 18, 35))
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(notificacion));

        assertThrows(
                NotificacionException.class,
                () -> service.marcarEnviada(1L)
        );

        verify(repository).findById(1L);
        verify(repository, never()).save(any(Notificacion.class));
    }

    @Test
    void marcarLeidaDebeCambiarEstadoALeida() {
        Notificacion notificacion = Notificacion.builder()
                .id(1L)
                .usuarioId(1L)
                .reservaId(1L)
                .titulo("Reserva enviada")
                .mensaje("Su reserva fue enviada")
                .tipo(TipoNotificacion.RESERVA)
                .estado(EstadoNotificacion.ENVIADA)
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 18, 30))
                .fechaEnvio(LocalDateTime.of(2026, 7, 1, 18, 35))
                .build();

        Notificacion leida = Notificacion.builder()
                .id(1L)
                .usuarioId(1L)
                .reservaId(1L)
                .titulo("Reserva enviada")
                .mensaje("Su reserva fue enviada")
                .tipo(TipoNotificacion.RESERVA)
                .estado(EstadoNotificacion.LEIDA)
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 18, 30))
                .fechaEnvio(LocalDateTime.of(2026, 7, 1, 18, 35))
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(notificacion));

        when(repository.save(any(Notificacion.class)))
                .thenReturn(leida);

        NotificacionResponseDTO respuesta = service.marcarLeida(1L);

        assertNotNull(respuesta);
        assertEquals(EstadoNotificacion.LEIDA, respuesta.getEstado());

        verify(repository).findById(1L);
        verify(repository).save(any(Notificacion.class));
    }

    @Test
    void marcarFallidaDebeCambiarEstadoAFallidaCuandoNoEstaLeida() {
        Notificacion notificacion = Notificacion.builder()
                .id(1L)
                .usuarioId(1L)
                .reservaId(1L)
                .titulo("Sistema")
                .mensaje("Error al enviar notificacion")
                .tipo(TipoNotificacion.SISTEMA)
                .estado(EstadoNotificacion.PENDIENTE)
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 18, 30))
                .fechaEnvio(null)
                .build();

        Notificacion fallida = Notificacion.builder()
                .id(1L)
                .usuarioId(1L)
                .reservaId(1L)
                .titulo("Sistema")
                .mensaje("Error al enviar notificacion")
                .tipo(TipoNotificacion.SISTEMA)
                .estado(EstadoNotificacion.FALLIDA)
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 18, 30))
                .fechaEnvio(null)
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(notificacion));

        when(repository.save(any(Notificacion.class)))
                .thenReturn(fallida);

        NotificacionResponseDTO respuesta = service.marcarFallida(1L);

        assertNotNull(respuesta);
        assertEquals(EstadoNotificacion.FALLIDA, respuesta.getEstado());

        verify(repository).findById(1L);
        verify(repository).save(any(Notificacion.class));
    }

    @Test
    void marcarFallidaDebeLanzarExcepcionCuandoEstaLeida() {
        Notificacion notificacion = Notificacion.builder()
                .id(1L)
                .usuarioId(1L)
                .reservaId(1L)
                .titulo("Reserva")
                .mensaje("Mensaje leido")
                .tipo(TipoNotificacion.RESERVA)
                .estado(EstadoNotificacion.LEIDA)
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 18, 30))
                .fechaEnvio(LocalDateTime.of(2026, 7, 1, 18, 35))
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(notificacion));

        assertThrows(
                NotificacionException.class,
                () -> service.marcarFallida(1L)
        );

        verify(repository).findById(1L);
        verify(repository, never()).save(any(Notificacion.class));
    }

    @Test
    void cambiarEstadoDebeActualizarEstado() {
        Notificacion notificacion = Notificacion.builder()
                .id(1L)
                .usuarioId(1L)
                .reservaId(1L)
                .titulo("Reserva")
                .mensaje("Mensaje")
                .tipo(TipoNotificacion.RESERVA)
                .estado(EstadoNotificacion.PENDIENTE)
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 18, 30))
                .fechaEnvio(null)
                .build();

        Notificacion enviada = Notificacion.builder()
                .id(1L)
                .usuarioId(1L)
                .reservaId(1L)
                .titulo("Reserva")
                .mensaje("Mensaje")
                .tipo(TipoNotificacion.RESERVA)
                .estado(EstadoNotificacion.ENVIADA)
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 18, 30))
                .fechaEnvio(LocalDateTime.of(2026, 7, 1, 18, 35))
                .build();

        CambiarEstadoNotificacionDTO request = new CambiarEstadoNotificacionDTO(
                EstadoNotificacion.ENVIADA
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(notificacion));

        when(repository.save(any(Notificacion.class)))
                .thenReturn(enviada);

        NotificacionResponseDTO respuesta = service.cambiarEstado(1L, request);

        assertNotNull(respuesta);
        assertEquals(EstadoNotificacion.ENVIADA, respuesta.getEstado());

        verify(repository).findById(1L);
        verify(repository).save(any(Notificacion.class));
    }

    @Test
    void eliminarDebeEliminarNotificacionCuandoExiste() {
        when(repository.existsById(1L))
                .thenReturn(true);

        service.eliminar(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void eliminarDebeLanzarExcepcionCuandoNoExiste() {
        when(repository.existsById(99L))
                .thenReturn(false);

        assertThrows(
                NotificacionNoEncontradaException.class,
                () -> service.eliminar(99L)
        );

        verify(repository).existsById(99L);
        verify(repository, never()).deleteById(99L);
    }
}