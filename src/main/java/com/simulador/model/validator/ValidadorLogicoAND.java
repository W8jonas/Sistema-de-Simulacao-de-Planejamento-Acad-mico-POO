package com.simulador.model.validator;

import com.simulador.model.domain.Student;
import com.simulador.model.domain.Subject;
import java.util.List;

// Valida se TODOS os pré-requisitos foram atendidos
public class ValidadorLogicoAND implements ValidadorPreRequisito {
    
    private List<ValidadorPreRequisito> validadores;
    
    public ValidadorLogicoAND(List<ValidadorPreRequisito> validadores) {
        this.validadores = validadores;
    }
    
    @Override
    public boolean validar(Student student, Subject subject) {
        // Todos os validadores devem retornar true
        for (ValidadorPreRequisito validador : validadores) {
            if (!validador.validar(student, subject)) {
                return false;
            }
        }
        return true;
    }
} 