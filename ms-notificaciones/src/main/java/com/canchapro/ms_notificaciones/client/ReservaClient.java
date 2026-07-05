package com.canchapro.ms_notificaciones.client;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "MS-RESERVAS")
public interface ReservaClient {

    @GetMapping("/api/reservas/{id}")
    Object buscarReservaPorId(
            @PathVariable("id") Long id
    );
}