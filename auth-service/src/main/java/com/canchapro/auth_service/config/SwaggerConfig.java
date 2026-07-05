package com.canchapro.auth_service.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(
                        new Info()
                                .title("CanchaPro Auth Service API")
                                .description(
                                        "Microservicio de autenticación con JWT, BCrypt y roles"
                                )
                                .version("1.0")
                                .contact(
                                        new Contact()
                                                .name("CanchaPro Team")
                                                .email("admin@canchapro.cl")
                                )
                )
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(securitySchemeName)
                )
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }
}