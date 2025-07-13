package com.simulador.model.exceptions;

/**
 * Exceção lançada quando há um conflito de horário não resolvível pela precedência
 */
public class ConflitoDeHorarioException extends ValidacaoMatriculaException {
    
    public ConflitoDeHorarioException(String message) {
        super(message);
    }
    
    public ConflitoDeHorarioException(String message, Throwable cause) {
        super(message, cause);
    }
} 