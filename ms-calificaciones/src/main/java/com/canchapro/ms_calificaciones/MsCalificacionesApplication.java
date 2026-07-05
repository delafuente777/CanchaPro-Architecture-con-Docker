package com.canchapro.ms_calificaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsCalificacionesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsCalificacionesApplication.class, args);
    }
}