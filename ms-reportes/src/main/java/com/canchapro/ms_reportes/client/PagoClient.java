package com.canchapro.ms_reportes.client;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "MS-PAGOS")
public interface PagoClient {

    @GetMapping("/api/pagos")
    List<Object> listarPagos();
}
