package com.canchapro.auth_service.dto;

import com.canchapro.auth_service.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100,
            message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 50,
            message = "La contraseña debe tener entre 8 y 50 caracteres")
    private String password;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(
            regexp = "^[0-9]{8,15}$",
            message = "El teléfono debe contener entre 8 y 15 dígitos"
    )
    private String telefono;

    @NotNull(message = "El rol es obligatorio")
    private Role role;
}