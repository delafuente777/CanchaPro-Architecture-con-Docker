package com.canchapro.ms_calificaciones.client;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "MS-CANCHAS")
public interface CanchaClient {

    @GetMapping("/api/canchas/{id}")
    Object buscarCanchaPorId(
            @PathVariable("id") Long id
    );
}