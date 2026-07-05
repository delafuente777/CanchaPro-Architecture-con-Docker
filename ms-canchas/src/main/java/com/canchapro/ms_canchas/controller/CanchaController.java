package com.canchapro.ms_canchas.controller;

import com.canchapro.ms_canchas.dto.CanchaRequestDTO;
import com.canchapro.ms_canchas.dto.CanchaResponseDTO;
import com.canchapro.ms_canchas.entity.EstadoCancha;
import com.canchapro.ms_canchas.entity.TipoCancha;
import com.canchapro.ms_canchas.service.CanchaService;

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
@RequestMapping("/api/canchas")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Canchas",
        description = "CRUD y administracion de canchas deportivas"
)
public class CanchaController {

    private final CanchaService service;

    @Operation(
            summary = "Listar canchas",
            description = "Obtiene todas las canchas registradas"
    )
    @GetMapping
    public ResponseEntity<List<CanchaResponseDTO>> listarTodas() {

        return ResponseEntity.ok(
                service.listarTodas()
        );
    }

    @Operation(
            summary = "Buscar cancha por ID",
            description = "Obtiene una cancha especifica mediante su ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<CanchaResponseDTO> buscarPorId(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id
    ) {

        return ResponseEntity.ok(
                service.buscarPorId(id)
        );
    }

    @Operation(
            summary = "Listar canchas por estado",
            description = "Filtra canchas por estado"
    )
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<CanchaResponseDTO>> listarPorEstado(
            @PathVariable EstadoCancha estado
    ) {

        return ResponseEntity.ok(
                service.listarPorEstado(estado)
        );
    }

    @Operation(
            summary = "Listar canchas por tipo",
            description = "Filtra canchas por tipo deportivo"
    )
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<CanchaResponseDTO>> listarPorTipo(
            @PathVariable TipoCancha tipo
    ) {

        return ResponseEntity.ok(
                service.listarPorTipo(tipo)
        );
    }

    @Operation(
            summary = "Crear cancha",
            description = "Crea una nueva cancha deportiva"
    )
    @PostMapping
    public ResponseEntity<CanchaResponseDTO> crear(
            @Valid
            @RequestBody CanchaRequestDTO request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.crear(request));
    }

    @Operation(
            summary = "Actualizar cancha",
            description = "Actualiza completamente los datos de una cancha"
    )
    @PutMapping("/{id}")
    public ResponseEntity<CanchaResponseDTO> actualizar(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id,

            @Valid
            @RequestBody CanchaRequestDTO request
    ) {

        return ResponseEntity.ok(
                service.actualizar(id, request)
        );
    }

    @Operation(
            summary = "Eliminar cancha",
            description = "Elimina fisicamente una cancha por ID"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id
    ) {

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Cambiar estado de cancha",
            description = "Actualiza unicamente el estado de una cancha"
    )
    @PatchMapping("/{id}/estado/{estado}")
    public ResponseEntity<CanchaResponseDTO> cambiarEstado(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id,

            @PathVariable EstadoCancha estado
    ) {

        return ResponseEntity.ok(
                service.cambiarEstado(id, estado)
        );
    }

    @Operation(
            summary = "Validar disponibilidad de cancha",
            description = "Valida si una cancha existe y esta en estado DISPONIBLE"
    )
    @GetMapping("/{id}/validar-disponible")
    public ResponseEntity<String> validarDisponible(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id
    ) {

        service.validarCanchaDisponible(id);

        return ResponseEntity.ok(
                "Cancha disponible"
        );
    }
}
