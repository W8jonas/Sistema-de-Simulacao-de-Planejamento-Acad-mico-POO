package com.simulador.model.exceptions;

/**
 * Classe base abstrata para todas as exceções relacionadas à matrícula
 */
public abstract class MatriculaException extends Exception {
    
    public MatriculaException(String message) {
        super(message);
    }
    
    public MatriculaException(String message, Throwable cause) {
        super(message, cause);
    }
} 