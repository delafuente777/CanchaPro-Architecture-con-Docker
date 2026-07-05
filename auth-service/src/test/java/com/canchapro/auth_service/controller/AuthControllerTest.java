package com.canchapro.auth_service.controller;

import com.canchapro.auth_service.dto.AuthResponseDTO;
import com.canchapro.auth_service.exception.GlobalExceptionHandler;
import com.canchapro.auth_service.exception.InvalidCredentialsException;
import com.canchapro.auth_service.exception.UserAlreadyExistsException;
import com.canchapro.auth_service.service.AuthService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;

import org.springframework.security.authentication.TestingAuthenticationToken;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {

    private MockMvc mockMvc;
    private AuthService service;

    @BeforeEach
    void setUp() {
        service = mock(AuthService.class);

        AuthController controller = new AuthController(service);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void registerDebeRetornarStatusOkCuandoRequestEsValido() throws Exception {
        String json = """
                {
                    "nombre": "Caden Nieto",
                    "correo": "caden.auth@canchapro.cl",
                    "password": "password123",
                    "telefono": "987654321",
                    "role": "CLIENTE"
                }
                """;

        when(service.register(any()))
                .thenReturn(new AuthResponseDTO(
                        "jwt-token-test",
                        "Usuario registrado correctamente"
                ));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(service).register(any());
    }

    @Test
    void registerDebeRetornarConflictCuandoCorreoYaExiste() throws Exception {
        String json = """
                {
                    "nombre": "Caden Nieto",
                    "correo": "caden.auth@canchapro.cl",
                    "password": "password123",
                    "telefono": "987654321",
                    "role": "CLIENTE"
                }
                """;

        when(service.register(any()))
                .thenThrow(new UserAlreadyExistsException(
                        "Correo ya registrado"
                ));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict());

        verify(service).register(any());
    }

    @Test
    void registerDebeRetornarBadRequestCuandoRequestEsInvalido() throws Exception {
        String json = """
                {
                    "nombre": "",
                    "correo": "correo-malo",
                    "password": "123",
                    "telefono": "abc",
                    "role": null
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginDebeRetornarStatusOkCuandoCredencialesSonValidas() throws Exception {
        String json = """
                {
                    "correo": "caden.auth@canchapro.cl",
                    "password": "password123"
                }
                """;

        when(service.login(any()))
                .thenReturn(new AuthResponseDTO(
                        "jwt-token-login",
                        "Login exitoso"
                ));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(service).login(any());
    }

    @Test
    void loginDebeRetornarUnauthorizedCuandoCredencialesSonInvalidas() throws Exception {
        String json = """
                {
                    "correo": "caden.auth@canchapro.cl",
                    "password": "password-mala"
                }
                """;

        when(service.login(any()))
                .thenThrow(new InvalidCredentialsException(
                        "Correo o contrasena incorrectos"
                ));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());

        verify(service).login(any());
    }

    @Test
    void loginDebeRetornarBadRequestCuandoRequestEsInvalido() throws Exception {
        String json = """
                {
                    "correo": "correo-malo",
                    "password": ""
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDebeRetornarUsuarioAutenticado() throws Exception {
        TestingAuthenticationToken authentication =
                new TestingAuthenticationToken(
                        "caden.auth@canchapro.cl",
                        null
                );

        mockMvc.perform(get("/api/auth/test")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "Token valido. Usuario autenticado: caden.auth@canchapro.cl"
                ));
    }

    @Test
    void adminDebeRetornarStatusOkEnPruebaStandalone() throws Exception {
        mockMvc.perform(get("/api/auth/admin"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "Acceso permitido solo para ADMIN"
                ));
    }
}