package com.canchapro.ms_reservas.client;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "MS-PAGOS")
public interface PagoClient {

    @GetMapping("/api/pagos/validar/{reservaId}")
    Object validarPago(
            @PathVariable("reservaId") Long reservaId
    );
}