package com.canchapro.auth_service.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @Email(message = "Correo inválido")
    @NotBlank(message = "Correo obligatorio")
    private String correo;

    @NotBlank(message = "Contraseña obligatoria")
    private String password;
}