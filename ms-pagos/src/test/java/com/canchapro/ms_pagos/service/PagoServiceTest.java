package com.canchapro.ms_pagos.service;

import com.canchapro.ms_pagos.dto.CambiarEstadoPagoDTO;
import com.canchapro.ms_pagos.dto.PagoRequestDTO;
import com.canchapro.ms_pagos.dto.PagoResponseDTO;
import com.canchapro.ms_pagos.entity.EstadoPago;
import com.canchapro.ms_pagos.entity.MetodoPago;
import com.canchapro.ms_pagos.entity.Pago;
import com.canchapro.ms_pagos.exception.PagoException;
import com.canchapro.ms_pagos.exception.PagoNoEncontradoException;
import com.canchapro.ms_pagos.repository.PagoRepository;

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
class PagoServiceTest {

    @Mock
    private PagoRepository repository;

    @InjectMocks
    private PagoService service;

    @Test
    void registrarPagoDebeGuardarPagoCuandoReservaNoTienePagoAprobado() {
        PagoRequestDTO request = new PagoRequestDTO(
                1L,
                25000,
                MetodoPago.DEBITO
        );

        Pago pagoGuardado = Pago.builder()
                .id(1L)
                .reservaId(1L)
                .monto(25000)
                .metodoPago(MetodoPago.DEBITO)
                .estado(EstadoPago.APROBADO)
                .fechaPago(LocalDateTime.of(2026, 7, 1, 18, 30))
                .build();

        when(repository.existsByReservaIdAndEstado(1L, EstadoPago.APROBADO))
                .thenReturn(false);

        when(repository.save(any(Pago.class)))
                .thenReturn(pagoGuardado);

        PagoResponseDTO respuesta = service.registrarPago(request);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals(1L, respuesta.getReservaId());
        assertEquals(25000, respuesta.getMonto());
        assertEquals(MetodoPago.DEBITO, respuesta.getMetodoPago());
        assertEquals(EstadoPago.APROBADO, respuesta.getEstado());

        verify(repository).existsByReservaIdAndEstado(1L, EstadoPago.APROBADO);
        verify(repository).save(any(Pago.class));
    }

    @Test
    void registrarPagoDebeLanzarExcepcionCuandoReservaYaTienePagoAprobado() {
        PagoRequestDTO request = new PagoRequestDTO(
                1L,
                25000,
                MetodoPago.DEBITO
        );

        when(repository.existsByReservaIdAndEstado(1L, EstadoPago.APROBADO))
                .thenReturn(true);

        assertThrows(
                PagoException.class,
                () -> service.registrarPago(request)
        );

        verify(repository).existsByReservaIdAndEstado(1L, EstadoPago.APROBADO);
        verify(repository, never()).save(any(Pago.class));
    }

    @Test
    void buscarPorIdDebeRetornarPagoCuandoExiste() {
        Pago pago = Pago.builder()
                .id(1L)
                .reservaId(1L)
                .monto(25000)
                .metodoPago(MetodoPago.TRANSFERENCIA)
                .estado(EstadoPago.APROBADO)
                .fechaPago(LocalDateTime.of(2026, 7, 1, 18, 30))
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(pago));

        PagoResponseDTO respuesta = service.buscarPorId(1L);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals(1L, respuesta.getReservaId());
        assertEquals(25000, respuesta.getMonto());
        assertEquals(MetodoPago.TRANSFERENCIA, respuesta.getMetodoPago());
        assertEquals(EstadoPago.APROBADO, respuesta.getEstado());

        verify(repository).findById(1L);
    }

    @Test
    void buscarPorIdDebeLanzarExcepcionCuandoNoExiste() {
        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                PagoNoEncontradoException.class,
                () -> service.buscarPorId(99L)
        );

        verify(repository).findById(99L);
    }

    @Test
    void validarPagoDebeRetornarPagoCuandoUltimoPagoEstaAprobado() {
        Pago pago = Pago.builder()
                .id(1L)
                .reservaId(1L)
                .monto(25000)
                .metodoPago(MetodoPago.CREDITO)
                .estado(EstadoPago.APROBADO)
                .fechaPago(LocalDateTime.of(2026, 7, 1, 18, 30))
                .build();

        when(repository.findFirstByReservaIdOrderByFechaPagoDesc(1L))
                .thenReturn(Optional.of(pago));

        PagoResponseDTO respuesta = service.validarPago(1L);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getReservaId());
        assertEquals(EstadoPago.APROBADO, respuesta.getEstado());

        verify(repository).findFirstByReservaIdOrderByFechaPagoDesc(1L);
    }

    @Test
    void validarPagoDebeLanzarExcepcionCuandoPagoNoEstaAprobado() {
        Pago pago = Pago.builder()
                .id(1L)
                .reservaId(1L)
                .monto(25000)
                .metodoPago(MetodoPago.CREDITO)
                .estado(EstadoPago.RECHAZADO)
                .fechaPago(LocalDateTime.of(2026, 7, 1, 18, 30))
                .build();

        when(repository.findFirstByReservaIdOrderByFechaPagoDesc(1L))
                .thenReturn(Optional.of(pago));

        assertThrows(
                PagoException.class,
                () -> service.validarPago(1L)
        );

        verify(repository).findFirstByReservaIdOrderByFechaPagoDesc(1L);
    }

    @Test
    void cambiarEstadoDebeActualizarEstadoDelPago() {
        Pago pago = Pago.builder()
                .id(1L)
                .reservaId(1L)
                .monto(25000)
                .metodoPago(MetodoPago.EFECTIVO)
                .estado(EstadoPago.PENDIENTE)
                .fechaPago(LocalDateTime.of(2026, 7, 1, 18, 30))
                .build();

        Pago pagoActualizado = Pago.builder()
                .id(1L)
                .reservaId(1L)
                .monto(25000)
                .metodoPago(MetodoPago.EFECTIVO)
                .estado(EstadoPago.APROBADO)
                .fechaPago(LocalDateTime.of(2026, 7, 1, 18, 30))
                .build();

        CambiarEstadoPagoDTO request = new CambiarEstadoPagoDTO(
                EstadoPago.APROBADO
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(pago));

        when(repository.save(any(Pago.class)))
                .thenReturn(pagoActualizado);

        PagoResponseDTO respuesta = service.cambiarEstado(1L, request);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals(EstadoPago.APROBADO, respuesta.getEstado());

        verify(repository).findById(1L);
        verify(repository).save(any(Pago.class));
    }

    @Test
    void eliminarDebeEliminarPagoCuandoExiste() {
        when(repository.existsById(1L))
                .thenReturn(true);

        service.eliminar(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void eliminarDebeLanzarExcepcionCuandoPagoNoExiste() {
        when(repository.existsById(99L))
                .thenReturn(false);

        assertThrows(
                PagoNoEncontradoException.class,
                () -> service.eliminar(99L)
        );

        verify(repository).existsById(99L);
        verify(repository, never()).deleteById(99L);
    }
}