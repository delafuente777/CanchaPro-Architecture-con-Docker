package com.canchapro.ms_disponibilidad.exception;

public class HorarioYaExisteException extends RuntimeException {

    public HorarioYaExisteException(String message) {
        super(message);
    }
}