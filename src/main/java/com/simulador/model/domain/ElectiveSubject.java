package com.simulador.model.domain;

/**
 * Representa uma disciplina eletiva no sistema acadêmico.
 * Disciplinas eletivas são opcionais mas contam para a formação do aluno.
 */
public class ElectiveSubject extends Subject {
    
    public ElectiveSubject(String code, String name, int weeklyHours) {
        super(code, name, weeklyHours);
    }
    
    @Override
    public String getType() {
        return "Eletiva";
    }
    
    @Override
    public int getPrecedence() {
        return 2; // Eletivas têm precedência 2 (menor que obrigatórias, maior que optativas)
    }
}
