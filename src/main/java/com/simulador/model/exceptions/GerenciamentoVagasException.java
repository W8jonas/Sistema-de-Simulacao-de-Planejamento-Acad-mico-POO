package com.simulador.model.exceptions;

/**
 * Classe base para exceções de gerenciamento de vagas
 */
public abstract class GerenciamentoVagasException extends MatriculaException {
    
    public GerenciamentoVagasException(String message) {
        super(message);
    }
    
    public GerenciamentoVagasException(String message, Throwable cause) {
        super(message, cause);
    }
} 