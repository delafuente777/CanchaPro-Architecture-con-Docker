package com.canchapro.ms_reportes.config;

import io.swagger.v3.oas.models.OpenAPI;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title("CanchaPro Reportes API")
                                .description(
                                        "Microservicio para gestionar y generar reportes del sistema CanchaPro"
                                )
                                .version("1.0")
                                .contact(
                                        new Contact()
                                                .name("CanchaPro Team")
                                                .email("admin@canchapro.cl")
                                )
                );
    }
}