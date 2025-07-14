package com.simulador.model.domain;

/**
 * Representa uma disciplina optativa no sistema acadêmico.
 * Disciplinas optativas são completamente opcionais para o aluno.
 */
public class OptionalSubject extends Subject {
    
    public OptionalSubject(String code, String name, int weeklyHours) {
        super(code, name, weeklyHours);
    }
    
    @Override
    public String getType() {
        return "Optativa";
    }
    
    @Override
    public int getPrecedence() {
        return 3; // Optativas têm menor precedência
    }
}
