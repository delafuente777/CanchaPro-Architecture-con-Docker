package com.canchapro.auth_service.controller;

import com.canchapro.auth_service.dto.AuthResponseDTO;
import com.canchapro.auth_service.dto.LoginRequestDTO;
import com.canchapro.auth_service.dto.RegisterRequestDTO;
import com.canchapro.auth_service.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(
        name = "Autenticacion",
        description = "Endpoints para registro, login y validacion JWT"
)
public class AuthController {

    private final AuthService service;

    @Operation(
            summary = "Registrar usuario",
            description = "Registra un nuevo usuario y devuelve un token JWT"
    )
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(
            @Valid
            @RequestBody RegisterRequestDTO request
    ) {

        return ResponseEntity.ok(
                service.register(request)
        );
    }

    @Operation(
            summary = "Iniciar sesion",
            description = "Valida credenciales y devuelve un token JWT"
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid
            @RequestBody LoginRequestDTO request
    ) {

        return ResponseEntity.ok(
                service.login(request)
        );
    }

    @Operation(
            summary = "Endpoint protegido",
            description = "Permite verificar si un token JWT es valido"
    )
    @GetMapping("/test")
    public ResponseEntity<String> test(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                "Token valido. Usuario autenticado: "
                        + authentication.getName()
        );
    }

    @Operation(
            summary = "Endpoint solo ADMIN",
            description = "Permite probar autorizacion por rol ADMIN"
    )
    @GetMapping("/admin")
    public ResponseEntity<String> admin() {
        return ResponseEntity.ok(
                "Acceso permitido solo para ADMIN"
        );
    }
}