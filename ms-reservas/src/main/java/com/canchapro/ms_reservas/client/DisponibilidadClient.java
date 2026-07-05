package com.canchapro.ms_reservas.client;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "MS-DISPONIBILIDAD")
public interface DisponibilidadClient {

    @GetMapping("/api/disponibilidad/consultar")
    Boolean consultarDisponible(
            @RequestParam("canchaId") Long canchaId,
            @RequestParam("fecha") String fecha,
            @RequestParam("horaInicio") String horaInicio
    );

    @PatchMapping("/api/disponibilidad/reservar")
    Object reservarHorario(
            @RequestParam("canchaId") Long canchaId,
            @RequestParam("fecha") String fecha,
            @RequestParam("horaInicio") String horaInicio
    );

    @PatchMapping("/api/disponibilidad/liberar")
    Object liberarHorario(
            @RequestParam("canchaId") Long canchaId,
            @RequestParam("fecha") String fecha,
            @RequestParam("horaInicio") String horaInicio
    );
}