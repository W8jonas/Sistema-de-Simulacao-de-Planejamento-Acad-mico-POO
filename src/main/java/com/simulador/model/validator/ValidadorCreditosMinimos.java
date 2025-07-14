package com.simulador.model.validator;

import com.simulador.model.domain.Student;
import com.simulador.model.domain.Subject;

// Valida se o aluno tem créditos mínimos
public class ValidadorCreditosMinimos implements ValidadorPreRequisito {
    
    private int creditosMinimos;
    
    public ValidadorCreditosMinimos(int creditosMinimos) {
        this.creditosMinimos = creditosMinimos;
    }
    
    @Override
    public boolean validar(Student student, Subject subject) {
        // Usa os créditos acumulados pelo estudante
        return student.getCreditosAcumulados() >= creditosMinimos;
    }
} 