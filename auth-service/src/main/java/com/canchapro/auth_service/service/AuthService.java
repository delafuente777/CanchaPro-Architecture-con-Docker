package com.canchapro.auth_service.service;

import com.canchapro.auth_service.dto.AuthResponseDTO;
import com.canchapro.auth_service.dto.LoginRequestDTO;
import com.canchapro.auth_service.dto.RegisterRequestDTO;
import com.canchapro.auth_service.entity.User;
import com.canchapro.auth_service.exception.InvalidCredentialsException;
import com.canchapro.auth_service.exception.UserAlreadyExistsException;
import com.canchapro.auth_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponseDTO register(
            RegisterRequestDTO request
    ) {

        if (repository.existsByCorreo(
                request.getCorreo()
        )) {

            throw new UserAlreadyExistsException(
                    "Correo ya registrado"
            );
        }

        User user = User.builder()
                .nombre(request.getNombre())
                .correo(request.getCorreo())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .telefono(request.getTelefono())
                .role(request.getRole())
                .build();

        repository.save(user);

        log.info(
                "Usuario registrado: {}",
                user.getCorreo()
        );

        String token =
                jwtService.generateToken(user);

        return new AuthResponseDTO(
                token,
                "Usuario registrado correctamente"
        );
    }

    public AuthResponseDTO login(
            LoginRequestDTO request
    ) {

        User user = repository.findByCorreo(
                        request.getCorreo()
                )
                .orElseThrow(
                        () -> new InvalidCredentialsException(
                                "Correo o contraseña incorrectos"
                        )
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {

            throw new InvalidCredentialsException(
                    "Correo o contraseña incorrectos"
            );
        }

        String token =
                jwtService.generateToken(user);

        log.info(
                "Login exitoso: {}",
                user.getCorreo()
        );

        return new AuthResponseDTO(
                token,
                "Login exitoso"
        );
    }
}