package com.canchapro.ms_usuarios.controller;

import com.canchapro.ms_usuarios.dto.UsuarioRequestDTO;
import com.canchapro.ms_usuarios.dto.UsuarioResponseDTO;
import com.canchapro.ms_usuarios.service.UsuarioService;

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
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Usuarios",
        description = "CRUD de usuarios del sistema CanchaPro"
)
public class UsuarioController {

    private final UsuarioService service;

    @Operation(
            summary = "Listar usuarios",
            description = "Obtiene todos los usuarios registrados"
    )
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {

        return ResponseEntity.ok(
                service.listarTodos()
        );
    }

    @Operation(
            summary = "Buscar usuario por ID",
            description = "Obtiene un usuario especifico mediante su ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id
    ) {

        return ResponseEntity.ok(
                service.buscarPorId(id)
        );
    }

    @Operation(
            summary = "Crear usuario",
            description = "Crea un nuevo usuario en el sistema"
    )
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(
            @Valid
            @RequestBody UsuarioRequestDTO request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.crear(request));
    }

    @Operation(
            summary = "Actualizar usuario",
            description = "Actualiza completamente los datos de un usuario"
    )
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id,

            @Valid
            @RequestBody UsuarioRequestDTO request
    ) {

        return ResponseEntity.ok(
                service.actualizar(id, request)
        );
    }

    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina fisicamente un usuario por ID"
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
            summary = "Desactivar usuario",
            description = "Desactiva logicamente un usuario sin eliminarlo"
    )
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<UsuarioResponseDTO> desactivar(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id
    ) {

        return ResponseEntity.ok(
                service.desactivar(id)
        );
    }
}