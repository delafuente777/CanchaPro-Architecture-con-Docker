package com.canchapro.ms_reservas.controller;

import com.canchapro.ms_reservas.dto.CambiarEstadoReservaDTO;
import com.canchapro.ms_reservas.dto.ReservaRequestDTO;
import com.canchapro.ms_reservas.dto.ReservaResponseDTO;
import com.canchapro.ms_reservas.entity.EstadoReserva;
import com.canchapro.ms_reservas.service.ReservaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;

import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Reservas",
        description = "Gestion de reservas de canchas"
)
public class ReservaController {

    private final ReservaService service;

    @Operation(summary = "Listar reservas")
    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> listarTodas() {

        return ResponseEntity.ok(
                service.listarTodas()
        );
    }

    @Operation(summary = "Buscar reserva por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> buscarPorId(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id
    ) {

        return ResponseEntity.ok(
                service.buscarPorId(id)
        );
    }

    @Operation(summary = "Crear reserva")
    @PostMapping
    public ResponseEntity<ReservaResponseDTO> crear(
            @Valid
            @RequestBody ReservaRequestDTO request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.crear(request));
    }

    @Operation(summary = "Actualizar reserva")
    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> actualizar(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id,

            @Valid
            @RequestBody ReservaRequestDTO request
    ) {

        return ResponseEntity.ok(
                service.actualizar(id, request)
        );
    }

    @Operation(summary = "Eliminar reserva")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id
    ) {

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cancelar reserva")
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ReservaResponseDTO> cancelar(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id
    ) {

        return ResponseEntity.ok(
                service.cancelar(id)
        );
    }

    @Operation(summary = "Cambiar estado de reserva")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ReservaResponseDTO> cambiarEstado(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id,

            @Valid
            @RequestBody CambiarEstadoReservaDTO request
    ) {

        return ResponseEntity.ok(
                service.cambiarEstado(id, request)
        );
    }

    @Operation(summary = "Confirmar pago de reserva")
    @PatchMapping("/{id}/confirmar-pago")
    public ResponseEntity<ReservaResponseDTO> confirmarPago(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id
    ) {

        return ResponseEntity.ok(
                service.confirmarPago(id)
        );
    }

    @Operation(summary = "Listar reservas por usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ReservaResponseDTO>> listarPorUsuario(
            @PathVariable
            @Positive(message = "El ID de usuario debe ser mayor que cero")
            Long usuarioId
    ) {

        return ResponseEntity.ok(
                service.listarPorUsuario(usuarioId)
        );
    }

    @Operation(summary = "Listar reservas por cancha")
    @GetMapping("/cancha/{canchaId}")
    public ResponseEntity<List<ReservaResponseDTO>> listarPorCancha(
            @PathVariable
            @Positive(message = "El ID de cancha debe ser mayor que cero")
            Long canchaId
    ) {

        return ResponseEntity.ok(
                service.listarPorCancha(canchaId)
        );
    }

    @Operation(summary = "Listar reservas por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ReservaResponseDTO>> listarPorEstado(
            @PathVariable EstadoReserva estado
    ) {

        return ResponseEntity.ok(
                service.listarPorEstado(estado)
        );
    }

    @Operation(summary = "Listar reservas por cancha y fecha")
    @GetMapping("/cancha/{canchaId}/fecha/{fecha}")
    public ResponseEntity<List<ReservaResponseDTO>> listarPorCanchaYFecha(
            @PathVariable
            @Positive(message = "El ID de cancha debe ser mayor que cero")
            Long canchaId,

            @PathVariable
            @FutureOrPresent(message = "La fecha no puede ser anterior a hoy")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha
    ) {

        return ResponseEntity.ok(
                service.listarPorCanchaYFecha(
                        canchaId,
                        fecha
                )
        );
    }
}