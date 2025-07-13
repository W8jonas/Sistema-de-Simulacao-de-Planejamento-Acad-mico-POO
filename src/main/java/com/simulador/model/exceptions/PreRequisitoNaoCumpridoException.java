package com.simulador.model.exceptions;

/**
 * Exceção lançada quando um pré-requisito não é atendido
 */
public class PreRequisitoNaoCumpridoException extends Exception {
    
    public PreRequisitoNaoCumpridoException(String message) {
        super(message);
    }
    
    public PreRequisitoNaoCumpridoException(String message, Throwable cause) {
        super(message, cause);
    }
} 