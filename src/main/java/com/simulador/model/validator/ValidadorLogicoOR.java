package com.simulador.model.validator;

import com.simulador.model.domain.Student;
import com.simulador.model.domain.Subject;
import java.util.List;

// Valida se PELO MENOS UM dos pré-requisitos foi atendido
public class ValidadorLogicoOR implements ValidadorPreRequisito {
    
    private List<ValidadorPreRequisito> validadores;
    
    public ValidadorLogicoOR(List<ValidadorPreRequisito> validadores) {
        this.validadores = validadores;
    }
    
    @Override
    public boolean validar(Student student, Subject subject) {
        // Pelo menos um validador deve retornar true
        for (ValidadorPreRequisito validador : validadores) {
            if (validador.validar(student, subject)) {
                return true;
            }
        }
        return false;
    }
} 