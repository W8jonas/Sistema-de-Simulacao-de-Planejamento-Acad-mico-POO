package com.simulador.model.domain;

import com.simulador.model.validator.ValidadorPreRequisito;
import java.util.HashSet;
import java.util.Set;

public abstract class Subject {
    private final String code;
    private final String name;
    private final int weeklyHours;
    private Set<ValidadorPreRequisito> validadores;
    private Set<Subject> coRequisitos; // Lista de co-requisitos

    protected Subject(String code, String name, int weeklyHours) {
        this.code = code;
        this.name = name;
        this.weeklyHours = weeklyHours;
        this.validadores = new HashSet<>();
        this.coRequisitos = new HashSet<>();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getWeeklyHours() {
        return weeklyHours;
    }

    public Set<ValidadorPreRequisito> getValidadores() {
        return validadores;
    }
    
    public Set<Subject> getCoRequisitos() {
        return coRequisitos;
    }
    
    public void addCoRequisito(Subject coRequisito) {
        this.coRequisitos.add(coRequisito);
    }
    
    public void removeCoRequisito(Subject coRequisito) {
        this.coRequisitos.remove(coRequisito);
    }
    
    public boolean hasCoRequisitos() {
        return !coRequisitos.isEmpty();
    }

    public void setValidadores(ValidadorPreRequisito validador) {
        this.validadores.clear();
        this.validadores.add(validador);
    }

    public void setValidadores(Set<ValidadorPreRequisito> validadores) {
        this.validadores = validadores;
    }

    /**
     * Verifica se o aluno atende aos pré-requisitos desta disciplina
     */
    public boolean requisitosAtendidos(Student student) {
        if (validadores.isEmpty()) {
            return true; // Sem pré-requisitos
        }
        
        for (ValidadorPreRequisito validador : validadores) {
            if (!validador.validar(student, this)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return code + " - " + name;
    }
    
    /**
     * Retorna o tipo da disciplina (Obrigatória, Eletiva, Optativa)
     */
    public abstract String getType();
    
    /**
     * Retorna a precedência da disciplina para resolução de conflitos
     * 1 = Obrigatória (maior precedência)
     * 2 = Eletiva (precedência média)
     * 3 = Optativa (menor precedência)
     */
    public abstract int getPrecedence();
}
