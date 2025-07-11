package com.simulador.model.validator;

import com.simulador.model.domain.Student;
import com.simulador.model.domain.Subject;
import java.util.Map;

// Valida se o aluno tem créditos mínimos
public class ValidadorCreditosMinimos implements ValidadorPreRequisito {
    
    private int creditosMinimos;
    
    public ValidadorCreditosMinimos(int creditosMinimos) {
        this.creditosMinimos = creditosMinimos;
    }
    
    @Override
    public boolean validar(Student student, Subject subject) {
        int creditosAprovados = 0;
        
        // Soma os créditos das disciplinas aprovadas (nota >= 6.0)
        for (Map.Entry<Subject, Double> entry : student.getCompletedSubjects().entrySet()) {
            Subject disciplina = entry.getKey();
            Double nota = entry.getValue();
            
            if (nota != null && nota >= 6.0) {
                creditosAprovados += disciplina.getWeeklyHours();
            }
        }
        
        return creditosAprovados >= creditosMinimos;
    }
} 