package com.canchapro.ms_usuarios.exception;



public class UsuarioYaExisteException extends RuntimeException {

    public UsuarioYaExisteException(String message) {
        super(message);
    }
}