package com.canchapro.ms_usuarios.util;

import com.canchapro.ms_usuarios.dto.UsuarioResponseDTO;
import com.canchapro.ms_usuarios.entity.Usuario;

import java.util.Objects;

public class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static UsuarioResponseDTO toResponseDTO(
            Usuario usuario
    ) {

        Usuario usuarioValidado = Objects.requireNonNull(
                usuario,
                "El usuario no puede ser null"
        );

        return UsuarioResponseDTO.builder()
                .id(usuarioValidado.getId())
                .nombre(usuarioValidado.getNombre())
                .correo(usuarioValidado.getCorreo())
                .role(usuarioValidado.getRole())
                .telefono(usuarioValidado.getTelefono())
                .activo(usuarioValidado.getActivo())
                .build();
    }
}  