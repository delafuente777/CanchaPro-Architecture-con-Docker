package com.canchapro.ms_canchas.service;

import com.canchapro.ms_canchas.dto.CanchaRequestDTO;
import com.canchapro.ms_canchas.dto.CanchaResponseDTO;
import com.canchapro.ms_canchas.entity.Cancha;
import com.canchapro.ms_canchas.entity.EstadoCancha;
import com.canchapro.ms_canchas.entity.TipoCancha;
import com.canchapro.ms_canchas.exception.CanchaNoEncontradaException;
import com.canchapro.ms_canchas.exception.CanchaYaExisteException;
import com.canchapro.ms_canchas.repository.CanchaRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CanchaServiceTest {

    @Mock
    private CanchaRepository repository;

    @InjectMocks
    private CanchaService service;

    @Test
    void crearDebeGuardarCanchaCuandoNoExisteDuplicado() {
        CanchaRequestDTO request = new CanchaRequestDTO(
                "Cancha Test",
                TipoCancha.FUTBOLITO,
                "Sede Central",
                25000,
                EstadoCancha.DISPONIBLE
        );

        Cancha canchaGuardada = Cancha.builder()
                .id(1L)
                .nombre("Cancha Test")
                .tipo(TipoCancha.FUTBOLITO)
                .ubicacion("Sede Central")
                .precio(25000)
                .estado(EstadoCancha.DISPONIBLE)
                .build();

        when(repository.existsByNombreAndUbicacion("Cancha Test", "Sede Central"))
                .thenReturn(false);

        when(repository.save(any(Cancha.class)))
                .thenReturn(canchaGuardada);

        CanchaResponseDTO respuesta = service.crear(request);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals("Cancha Test", respuesta.getNombre());
        assertEquals(TipoCancha.FUTBOLITO, respuesta.getTipo());
        assertEquals("Sede Central", respuesta.getUbicacion());
        assertEquals(25000, respuesta.getPrecio());
        assertEquals(EstadoCancha.DISPONIBLE, respuesta.getEstado());

        verify(repository).existsByNombreAndUbicacion("Cancha Test", "Sede Central");
        verify(repository).save(any(Cancha.class));
    }

    @Test
    void crearDebeLanzarExcepcionCuandoCanchaYaExiste() {
        CanchaRequestDTO request = new CanchaRequestDTO(
                "Cancha Test",
                TipoCancha.FUTBOLITO,
                "Sede Central",
                25000,
                EstadoCancha.DISPONIBLE
        );

        when(repository.existsByNombreAndUbicacion("Cancha Test", "Sede Central"))
                .thenReturn(true);

        assertThrows(
                CanchaYaExisteException.class,
                () -> service.crear(request)
        );

        verify(repository).existsByNombreAndUbicacion("Cancha Test", "Sede Central");
    }

    @Test
    void buscarPorIdDebeRetornarCanchaCuandoExiste() {
        Cancha cancha = Cancha.builder()
                .id(1L)
                .nombre("Cancha Test")
                .tipo(TipoCancha.FUTBOLITO)
                .ubicacion("Sede Central")
                .precio(25000)
                .estado(EstadoCancha.DISPONIBLE)
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(cancha));

        CanchaResponseDTO respuesta = service.buscarPorId(1L);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals("Cancha Test", respuesta.getNombre());
        assertEquals(TipoCancha.FUTBOLITO, respuesta.getTipo());
        assertEquals(EstadoCancha.DISPONIBLE, respuesta.getEstado());

        verify(repository).findById(1L);
    }

    @Test
    void buscarPorIdDebeLanzarExcepcionCuandoNoExiste() {
        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                CanchaNoEncontradaException.class,
                () -> service.buscarPorId(99L)
        );

        verify(repository).findById(99L);
    }

    @Test
    void cambiarEstadoDebeActualizarEstadoDeCancha() {
        Cancha cancha = Cancha.builder()
                .id(1L)
                .nombre("Cancha Test")
                .tipo(TipoCancha.FUTBOLITO)
                .ubicacion("Sede Central")
                .precio(25000)
                .estado(EstadoCancha.DISPONIBLE)
                .build();

        Cancha canchaActualizada = Cancha.builder()
                .id(1L)
                .nombre("Cancha Test")
                .tipo(TipoCancha.FUTBOLITO)
                .ubicacion("Sede Central")
                .precio(25000)
                .estado(EstadoCancha.DISPONIBLE)
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(cancha));

        when(repository.save(any(Cancha.class)))
                .thenReturn(canchaActualizada);

        CanchaResponseDTO respuesta = service.cambiarEstado(1L, EstadoCancha.DISPONIBLE);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals(EstadoCancha.DISPONIBLE, respuesta.getEstado());

        verify(repository).findById(1L);
        verify(repository).save(any(Cancha.class));
    }
}