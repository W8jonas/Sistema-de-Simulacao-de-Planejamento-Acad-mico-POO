package com.simulador.model.domain;

/**
 * Representa uma disciplina obrigatória no sistema acadêmico.
 * Disciplinas obrigatórias são essenciais para a formação do aluno.
 */
public class RequiredSubject extends Subject {
    
    public RequiredSubject(String code, String name, int weeklyHours) {
        super(code, name, weeklyHours);
    }
    
    @Override
    public String getType() {
        return "Obrigatória";
    }
    
    @Override
    public int getPrecedence() {
        return 1; // Obrigatórias têm maior precedência
    }
}
