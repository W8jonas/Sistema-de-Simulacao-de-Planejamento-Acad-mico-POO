package com.simulador.model.exceptions;

/**
 * Exceção lançada quando uma turma está cheia
 */
public class TurmaCheiaException extends GerenciamentoVagasException {
    
    public TurmaCheiaException(String message) {
        super(message);
    }
    
    public TurmaCheiaException(String message, Throwable cause) {
        super(message, cause);
    }
} 