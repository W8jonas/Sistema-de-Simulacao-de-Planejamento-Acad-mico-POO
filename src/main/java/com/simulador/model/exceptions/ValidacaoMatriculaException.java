package com.simulador.model.exceptions;

/**
 * Classe base para exceções de validação de matrícula
 */
public abstract class ValidacaoMatriculaException extends MatriculaException {
    
    public ValidacaoMatriculaException(String message) {
        super(message);
    }
    
    public ValidacaoMatriculaException(String message, Throwable cause) {
        super(message, cause);
    }
} 