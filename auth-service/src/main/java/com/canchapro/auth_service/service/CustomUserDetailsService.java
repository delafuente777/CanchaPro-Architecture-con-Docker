package com.canchapro.auth_service.service;

import com.canchapro.auth_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(
            String correo
    ) throws UsernameNotFoundException {

        return repository.findByCorreo(correo)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                "Usuario no encontrado"
                        )
                );
    }
}