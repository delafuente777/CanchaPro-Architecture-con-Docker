package com.canchapro.ms_usuarios.service;

import com.canchapro.ms_usuarios.dto.UsuarioRequestDTO;
import com.canchapro.ms_usuarios.dto.UsuarioResponseDTO;
import com.canchapro.ms_usuarios.entity.Usuario;
import com.canchapro.ms_usuarios.exception.UsuarioNoEncontradoException;
import com.canchapro.ms_usuarios.exception.UsuarioYaExisteException;
import com.canchapro.ms_usuarios.repository.UsuarioRepository;
import com.canchapro.ms_usuarios.util.UsuarioMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public List<UsuarioResponseDTO> listarTodos() {

        log.info("Listando todos los usuarios");

        return repository.findAll()
                .stream()
                .map(UsuarioMapper::toResponseDTO)
                .toList();
    }

    public UsuarioResponseDTO buscarPorId(Long id) {

        Long usuarioId = Objects.requireNonNull(
                id,
                "El ID del usuario no puede ser null"
        );

        log.info("Buscando usuario con ID {}", usuarioId);

        Usuario usuario = repository.findById(usuarioId)
                .orElseThrow(
                        () -> new UsuarioNoEncontradoException(
                                "No existe usuario con ID: " + usuarioId
                        )
                );

        return UsuarioMapper.toResponseDTO(usuario);
    }

    public UsuarioResponseDTO crear(
            UsuarioRequestDTO request
    ) {

        UsuarioRequestDTO requestValidado = Objects.requireNonNull(
                request,
                "La solicitud de usuario no puede ser null"
        );

        log.info(
                "Creando usuario con correo {}",
                requestValidado.getCorreo()
        );

        if (repository.existsByCorreo(requestValidado.getCorreo())) {
            throw new UsuarioYaExisteException(
                    "Ya existe un usuario con el correo: "
                            + requestValidado.getCorreo()
            );
        }

        Usuario usuario = Usuario.builder()
                .nombre(requestValidado.getNombre())
                .correo(requestValidado.getCorreo())
                .password(
                        passwordEncoder.encode(
                                requestValidado.getPassword()
                        )
                )
                .role(requestValidado.getRole())
                .telefono(requestValidado.getTelefono())
                .activo(true)
                .build();

        Usuario guardado = repository.save(
                Objects.requireNonNull(
                        usuario,
                        "El usuario no puede ser null"
                )
        );

        log.info(
                "Usuario creado correctamente con ID {}",
                guardado.getId()
        );

        return UsuarioMapper.toResponseDTO(guardado);
    }

    public UsuarioResponseDTO actualizar(
            Long id,
            UsuarioRequestDTO request
    ) {

        Long usuarioId = Objects.requireNonNull(
                id,
                "El ID del usuario no puede ser null"
        );

        UsuarioRequestDTO requestValidado = Objects.requireNonNull(
                request,
                "La solicitud de usuario no puede ser null"
        );

        log.info("Actualizando usuario con ID {}", usuarioId);

        Usuario usuario = repository.findById(usuarioId)
                .orElseThrow(
                        () -> new UsuarioNoEncontradoException(
                                "No existe usuario con ID: " + usuarioId
                        )
                );

        repository.findByCorreo(requestValidado.getCorreo())
                .filter(usuarioExistente ->
                        !usuarioExistente.getId().equals(usuarioId)
                )
                .ifPresent(usuarioExistente -> {
                    throw new UsuarioYaExisteException(
                            "Ya existe otro usuario con el correo: "
                                    + requestValidado.getCorreo()
                    );
                });

        usuario.setNombre(requestValidado.getNombre());
        usuario.setCorreo(requestValidado.getCorreo());
        usuario.setPassword(
                passwordEncoder.encode(
                        requestValidado.getPassword()
                )
        );
        usuario.setRole(requestValidado.getRole());
        usuario.setTelefono(requestValidado.getTelefono());

        Usuario actualizado = repository.save(
                Objects.requireNonNull(
                        usuario,
                        "El usuario no puede ser null"
                )
        );

        log.info(
                "Usuario actualizado correctamente con ID {}",
                actualizado.getId()
        );

        return UsuarioMapper.toResponseDTO(actualizado);
    }

    public void eliminar(Long id) {

        Long usuarioId = Objects.requireNonNull(
                id,
                "El ID del usuario no puede ser null"
        );

        log.info("Eliminando usuario con ID {}", usuarioId);

        Usuario usuario = repository.findById(usuarioId)
                .orElseThrow(
                        () -> new UsuarioNoEncontradoException(
                                "No existe usuario con ID: " + usuarioId
                        )
                );

        repository.delete(
                Objects.requireNonNull(
                        usuario,
                        "El usuario no puede ser null"
                )
        );

        log.info(
                "Usuario eliminado correctamente con ID {}",
                usuarioId
        );
    }

    public UsuarioResponseDTO desactivar(Long id) {

        Long usuarioId = Objects.requireNonNull(
                id,
                "El ID del usuario no puede ser null"
        );

        log.info("Desactivando usuario con ID {}", usuarioId);

        Usuario usuario = repository.findById(usuarioId)
                .orElseThrow(
                        () -> new UsuarioNoEncontradoException(
                                "No existe usuario con ID: " + usuarioId
                        )
                );

        usuario.setActivo(false);

        Usuario actualizado = repository.save(
                Objects.requireNonNull(
                        usuario,
                        "El usuario no puede ser null"
                )
        );

        log.info(
                "Usuario desactivado correctamente con ID {}",
                usuarioId
        );

        return UsuarioMapper.toResponseDTO(actualizado);
    }
}