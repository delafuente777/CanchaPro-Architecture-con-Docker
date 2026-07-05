package com.canchapro.ms_reportes.client;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "MS-RESERVAS")
public interface ReservaClient {

    @GetMapping("/api/reservas")
    List<Object> listarReservas();
}