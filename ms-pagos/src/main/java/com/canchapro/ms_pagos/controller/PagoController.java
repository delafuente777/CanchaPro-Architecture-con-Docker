package com.canchapro.ms_pagos.controller;

import com.canchapro.ms_pagos.dto.CambiarEstadoPagoDTO;
import com.canchapro.ms_pagos.dto.PagoRequestDTO;
import com.canchapro.ms_pagos.dto.PagoResponseDTO;
import com.canchapro.ms_pagos.service.PagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Pagos",
        description = "Gestion de pagos de reservas"
)
public class PagoController {

    private final PagoService service;

    @Operation(summary = "Listar pagos")
    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> listarTodos() {

        return ResponseEntity.ok(
                service.listarTodos()
        );
    }

    @Operation(summary = "Buscar pago por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> buscarPorId(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id
    ) {

        return ResponseEntity.ok(
                service.buscarPorId(id)
        );
    }

    @Operation(summary = "Registrar pago")
    @PostMapping
    public ResponseEntity<PagoResponseDTO> registrarPago(
            @Valid
            @RequestBody PagoRequestDTO request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.registrarPago(request));
    }

    @Operation(summary = "Historial de pagos por reserva")
    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<List<PagoResponseDTO>> historialPorReserva(
            @PathVariable
            @Positive(message = "El ID de reserva debe ser mayor que cero")
            Long reservaId
    ) {

        return ResponseEntity.ok(
                service.historialPorReserva(reservaId)
        );
    }

    @Operation(summary = "Validar pago aprobado de una reserva")
    @GetMapping("/validar/{reservaId}")
    public ResponseEntity<PagoResponseDTO> validarPago(
            @PathVariable
            @Positive(message = "El ID de reserva debe ser mayor que cero")
            Long reservaId
    ) {

        return ResponseEntity.ok(
                service.validarPago(reservaId)
        );
    }

    @Operation(summary = "Cambiar estado de pago")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<PagoResponseDTO> cambiarEstado(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id,

            @Valid
            @RequestBody CambiarEstadoPagoDTO request
    ) {

        return ResponseEntity.ok(
                service.cambiarEstado(id, request)
        );
    }

    @Operation(summary = "Eliminar pago")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id
    ) {

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}