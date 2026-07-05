package com.canchapro.ms_calificaciones.service;

import com.canchapro.ms_calificaciones.client.CanchaClient;
import com.canchapro.ms_calificaciones.client.ReservaClient;
import com.canchapro.ms_calificaciones.client.UsuarioClient;
import com.canchapro.ms_calificaciones.dto.CalificacionRequestDTO;
import com.canchapro.ms_calificaciones.dto.CalificacionResponseDTO;
import com.canchapro.ms_calificaciones.dto.CalificacionResumenDTO;
import com.canchapro.ms_calificaciones.entity.Calificacion;
import com.canchapro.ms_calificaciones.exception.CalificacionException;
import com.canchapro.ms_calificaciones.exception.CalificacionNoEncontradaException;
import com.canchapro.ms_calificaciones.repository.CalificacionRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalificacionServiceTest {

    @Mock
    private CalificacionRepository repository;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private CanchaClient canchaClient;

    @Mock
    private ReservaClient reservaClient;

    @InjectMocks
    private CalificacionService service;

    @Test
    void crearDebeGuardarCalificacionCuandoDatosSonValidosYReservaNoTieneCalificacion() {
        CalificacionRequestDTO request = new CalificacionRequestDTO(
                1L,
                1L,
                1L,
                5,
                "Excelente cancha"
        );

        Calificacion calificacionGuardada = Calificacion.builder()
                .id(1L)
                .usuarioId(1L)
                .canchaId(1L)
                .reservaId(1L)
                .puntuacion(5)
                .comentario("Excelente cancha")
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 20, 0))
                .build();

        when(usuarioClient.buscarUsuarioPorId(1L))
                .thenReturn(new Object());

        when(canchaClient.buscarCanchaPorId(1L))
                .thenReturn(new Object());

        when(reservaClient.buscarReservaPorId(1L))
                .thenReturn(new Object());

        when(repository.existsByReservaId(1L))
                .thenReturn(false);

        when(repository.save(any(Calificacion.class)))
                .thenReturn(calificacionGuardada);

        CalificacionResponseDTO respuesta = service.crear(request);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals(1L, respuesta.getUsuarioId());
        assertEquals(1L, respuesta.getCanchaId());
        assertEquals(1L, respuesta.getReservaId());
        assertEquals(5, respuesta.getPuntuacion());
        assertEquals("Excelente cancha", respuesta.getComentario());

        verify(usuarioClient).buscarUsuarioPorId(1L);
        verify(canchaClient).buscarCanchaPorId(1L);
        verify(reservaClient).buscarReservaPorId(1L);
        verify(repository).existsByReservaId(1L);
        verify(repository).save(any(Calificacion.class));
    }

    @Test
    void crearDebeLanzarExcepcionCuandoReservaYaTieneCalificacion() {
        CalificacionRequestDTO request = new CalificacionRequestDTO(
                1L,
                1L,
                1L,
                5,
                "Excelente cancha"
        );

        when(usuarioClient.buscarUsuarioPorId(1L))
                .thenReturn(new Object());

        when(canchaClient.buscarCanchaPorId(1L))
                .thenReturn(new Object());

        when(reservaClient.buscarReservaPorId(1L))
                .thenReturn(new Object());

        when(repository.existsByReservaId(1L))
                .thenReturn(true);

        assertThrows(
                CalificacionException.class,
                () -> service.crear(request)
        );

        verify(usuarioClient).buscarUsuarioPorId(1L);
        verify(canchaClient).buscarCanchaPorId(1L);
        verify(reservaClient).buscarReservaPorId(1L);
        verify(repository).existsByReservaId(1L);
        verify(repository, never()).save(any(Calificacion.class));
    }

    @Test
    void buscarPorIdDebeRetornarCalificacionCuandoExiste() {
        Calificacion calificacion = Calificacion.builder()
                .id(1L)
                .usuarioId(1L)
                .canchaId(1L)
                .reservaId(1L)
                .puntuacion(4)
                .comentario("Buena experiencia")
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 20, 0))
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(calificacion));

        CalificacionResponseDTO respuesta = service.buscarPorId(1L);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals(4, respuesta.getPuntuacion());
        assertEquals("Buena experiencia", respuesta.getComentario());

        verify(repository).findById(1L);
    }

    @Test
    void buscarPorIdDebeLanzarExcepcionCuandoNoExiste() {
        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                CalificacionNoEncontradaException.class,
                () -> service.buscarPorId(99L)
        );

        verify(repository).findById(99L);
    }

    @Test
    void buscarPorReservaDebeRetornarCalificacionCuandoExiste() {
        Calificacion calificacion = Calificacion.builder()
                .id(1L)
                .usuarioId(1L)
                .canchaId(1L)
                .reservaId(10L)
                .puntuacion(5)
                .comentario("Muy buena")
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 20, 0))
                .build();

        when(repository.findByReservaId(10L))
                .thenReturn(Optional.of(calificacion));

        CalificacionResponseDTO respuesta = service.buscarPorReserva(10L);

        assertNotNull(respuesta);
        assertEquals(10L, respuesta.getReservaId());
        assertEquals(5, respuesta.getPuntuacion());

        verify(repository).findByReservaId(10L);
    }

    @Test
    void obtenerResumenPorCanchaDebeCalcularPromedioYTotal() {
        Calificacion calificacionUno = Calificacion.builder()
                .id(1L)
                .usuarioId(1L)
                .canchaId(1L)
                .reservaId(1L)
                .puntuacion(5)
                .comentario("Excelente")
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 20, 0))
                .build();

        Calificacion calificacionDos = Calificacion.builder()
                .id(2L)
                .usuarioId(2L)
                .canchaId(1L)
                .reservaId(2L)
                .puntuacion(3)
                .comentario("Normal")
                .fechaCreacion(LocalDateTime.of(2026, 7, 2, 20, 0))
                .build();

        when(repository.findByCanchaId(1L))
                .thenReturn(List.of(calificacionUno, calificacionDos));

        CalificacionResumenDTO resumen = service.obtenerResumenPorCancha(1L);

        assertNotNull(resumen);
        assertEquals(1L, resumen.getCanchaId());
        assertEquals(4.0, resumen.getPromedio());
        assertEquals(2, resumen.getTotalCalificaciones());

        verify(repository).findByCanchaId(1L);
    }

    @Test
    void actualizarDebeModificarCalificacionCuandoExisteYNoHayDuplicadoDeReserva() {
        CalificacionRequestDTO request = new CalificacionRequestDTO(
                1L,
                1L,
                1L,
                4,
                "Comentario actualizado"
        );

        Calificacion calificacionExistente = Calificacion.builder()
                .id(1L)
                .usuarioId(1L)
                .canchaId(1L)
                .reservaId(1L)
                .puntuacion(5)
                .comentario("Comentario original")
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 20, 0))
                .build();

        Calificacion calificacionActualizada = Calificacion.builder()
                .id(1L)
                .usuarioId(1L)
                .canchaId(1L)
                .reservaId(1L)
                .puntuacion(4)
                .comentario("Comentario actualizado")
                .fechaCreacion(LocalDateTime.of(2026, 7, 1, 20, 0))
                .build();

        when(usuarioClient.buscarUsuarioPorId(1L))
                .thenReturn(new Object());

        when(canchaClient.buscarCanchaPorId(1L))
                .thenReturn(new Object());

        when(reservaClient.buscarReservaPorId(1L))
                .thenReturn(new Object());

        when(repository.findById(1L))
                .thenReturn(Optional.of(calificacionExistente));

        when(repository.findByReservaId(1L))
                .thenReturn(Optional.empty());

        when(repository.save(any(Calificacion.class)))
                .thenReturn(calificacionActualizada);

        CalificacionResponseDTO respuesta = service.actualizar(1L, request);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals(4, respuesta.getPuntuacion());
        assertEquals("Comentario actualizado", respuesta.getComentario());

        verify(usuarioClient).buscarUsuarioPorId(1L);
        verify(canchaClient).buscarCanchaPorId(1L);
        verify(reservaClient).buscarReservaPorId(1L);
        verify(repository).findById(1L);
        verify(repository).findByReservaId(1L);
        verify(repository).save(any(Calificacion.class));
    }

    @Test
    void eliminarDebeEliminarCalificacionCuandoExiste() {
        when(repository.existsById(1L))
                .thenReturn(true);

        service.eliminar(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void eliminarDebeLanzarExcepcionCuandoCalificacionNoExiste() {
        when(repository.existsById(99L))
                .thenReturn(false);

        assertThrows(
                CalificacionNoEncontradaException.class,
                () -> service.eliminar(99L)
        );

        verify(repository).existsById(99L);
        verify(repository, never()).deleteById(99L);
    }
}