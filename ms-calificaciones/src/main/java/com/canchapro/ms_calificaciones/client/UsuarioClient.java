package com.canchapro.ms_calificaciones.client;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "MS-USUARIOS")
public interface UsuarioClient {

    @GetMapping("/api/usuarios/{id}")
    Object buscarUsuarioPorId(
            @PathVariable("id") Long id
    );
}