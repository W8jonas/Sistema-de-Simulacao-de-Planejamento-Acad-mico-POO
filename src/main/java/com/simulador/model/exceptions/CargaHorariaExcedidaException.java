package com.simulador.model.exceptions;

/**
 * Exceção lançada quando a carga horária máxima é excedida
 */
public class CargaHorariaExcedidaException extends Exception {
    
    public CargaHorariaExcedidaException(String message) {
        super(message);
    }
    
    public CargaHorariaExcedidaException(String message, Throwable cause) {
        super(message, cause);
    }
} 