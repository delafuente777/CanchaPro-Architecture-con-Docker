package com.canchapro.ms_pagos.service;

import com.canchapro.ms_pagos.dto.CambiarEstadoPagoDTO;
import com.canchapro.ms_pagos.dto.PagoRequestDTO;
import com.canchapro.ms_pagos.dto.PagoResponseDTO;
import com.canchapro.ms_pagos.entity.EstadoPago;
import com.canchapro.ms_pagos.entity.Pago;
import com.canchapro.ms_pagos.exception.PagoException;
import com.canchapro.ms_pagos.exception.PagoNoEncontradoException;
import com.canchapro.ms_pagos.repository.PagoRepository;
import com.canchapro.ms_pagos.util.PagoMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PagoService {

    private final PagoRepository repository;

    public List<PagoResponseDTO> listarTodos() {

        log.info("Listando todos los pagos");

        return repository.findAll()
                .stream()
                .map(PagoMapper::toResponseDTO)
                .toList();
    }

    public PagoResponseDTO buscarPorId(Long id) {

        Long pagoId = Objects.requireNonNull(
                id,
                "El ID del pago no puede ser null"
        );

        Pago pago = repository.findById(pagoId)
                .orElseThrow(
                        () -> new PagoNoEncontradoException(
                                "No existe pago con ID: " + pagoId
                        )
                );

        return PagoMapper.toResponseDTO(pago);
    }

    public PagoResponseDTO registrarPago(
            PagoRequestDTO request
    ) {

        PagoRequestDTO requestValidado = Objects.requireNonNull(
                request,
                "La solicitud de pago no puede ser null"
        );

        log.info(
                "Registrando pago para reserva ID {}",
                requestValidado.getReservaId()
        );

        if (repository.existsByReservaIdAndEstado(
                requestValidado.getReservaId(),
                EstadoPago.APROBADO
        )) {
            throw new PagoException(
                    "La reserva ya tiene un pago aprobado"
            );
        }

        Pago pago = Pago.builder()
                .reservaId(requestValidado.getReservaId())
                .monto(requestValidado.getMonto())
                .metodoPago(requestValidado.getMetodoPago())
                .estado(EstadoPago.APROBADO)
                .fechaPago(LocalDateTime.now())
                .build();

        Pago guardado = repository.save(
                Objects.requireNonNull(
                        pago,
                        "El pago no puede ser null"
                )
        );

        log.info(
                "Pago registrado correctamente con ID {}",
                guardado.getId()
        );

        return PagoMapper.toResponseDTO(guardado);
    }

    public List<PagoResponseDTO> historialPorReserva(Long reservaId) {

        Long reservaIdValidada = Objects.requireNonNull(
                reservaId,
                "El ID de la reserva no puede ser null"
        );

        return repository.findByReservaId(reservaIdValidada)
                .stream()
                .map(PagoMapper::toResponseDTO)
                .toList();
    }

    public PagoResponseDTO validarPago(Long reservaId) {

        Long reservaIdValidada = Objects.requireNonNull(
                reservaId,
                "El ID de la reserva no puede ser null"
        );

        Pago pago = repository
                .findFirstByReservaIdOrderByFechaPagoDesc(
                        reservaIdValidada
                )
                .orElseThrow(
                        () -> new PagoNoEncontradoException(
                                "No existen pagos para la reserva ID: "
                                        + reservaIdValidada
                        )
                );

        if (!EstadoPago.APROBADO.equals(pago.getEstado())) {
            throw new PagoException(
                    "El pago de la reserva no está aprobado"
            );
        }

        return PagoMapper.toResponseDTO(pago);
    }

    public PagoResponseDTO cambiarEstado(
            Long id,
            CambiarEstadoPagoDTO request
    ) {

        Long pagoId = Objects.requireNonNull(
                id,
                "El ID del pago no puede ser null"
        );

        CambiarEstadoPagoDTO requestValidado = Objects.requireNonNull(
                request,
                "La solicitud de cambio de estado no puede ser null"
        );

        Pago pago = repository.findById(pagoId)
                .orElseThrow(
                        () -> new PagoNoEncontradoException(
                                "No existe pago con ID: " + pagoId
                        )
                );

        pago.setEstado(requestValidado.getEstado());

        Pago actualizado = repository.save(pago);

        log.info(
                "Estado del pago ID {} cambiado a {}",
                pagoId,
                requestValidado.getEstado()
        );

        return PagoMapper.toResponseDTO(actualizado);
    }
        public void eliminar(Long id) {

        Long pagoId = Objects.requireNonNull(
                id,
                "El ID del pago no puede ser null"
        );

        if (!repository.existsById(pagoId)) {
                throw new PagoNoEncontradoException(
                        "No existe pago con ID: " + pagoId
                );
        }

        repository.deleteById(pagoId);

        log.info(
                "Pago eliminado correctamente con ID {}",
                pagoId
        );
        }
}