package com.simulador.model.exceptions;

/**
 * Exceção lançada quando um co-requisito não é atendido
 */
public class CoRequisitoNaoAtendidoException extends ValidacaoMatriculaException {
    
    public CoRequisitoNaoAtendidoException(String message) {
        super(message);
    }
    
    public CoRequisitoNaoAtendidoException(String message, Throwable cause) {
        super(message, cause);
    }
} 