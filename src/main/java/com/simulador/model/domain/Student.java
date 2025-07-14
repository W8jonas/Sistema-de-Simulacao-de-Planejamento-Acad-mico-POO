package com.simulador.model.domain;

import com.simulador.model.exceptions.CargaHorariaExcedidaException;
import com.simulador.model.exceptions.CoRequisitoNaoAtendidoException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

// Classe que representa um estudante no sistema
public class Student {
    private String name;
    private String registration;
    private Map<Subject, Double> completedSubjects; // disciplinas cursadas com notas
    private int maxWeeklyHours;
    private Set<Subject> futurePlanning; // planejamento futuro
    private Map<Subject, Subject> coRequisitos; // mapeamento de co-requisitos
    private int creditosAcumulados; // créditos acumulados pelo estudante

    public Student(String name, String registration, int maxWeeklyHours) {
        this.name = name;
        this.registration = registration;
        this.maxWeeklyHours = maxWeeklyHours;
        this.completedSubjects = new HashMap<>();
        this.futurePlanning = new HashSet<>();
        this.coRequisitos = new HashMap<>();
        this.creditosAcumulados = 0;
    }

    // Getters básicos
    public String getName() {
        return name;
    }

    public String getRegistration() {
        return registration;
    }

    public int getMaxWeeklyHours() {
        return maxWeeklyHours;
    }

    public Map<Subject, Double> getCompletedSubjects() {
        return completedSubjects;
    }

    public Set<Subject> getFuturePlanning() {
        return futurePlanning;
    }

    /**
     * Obtém os créditos acumulados pelo estudante
     */
    public int getCreditosAcumulados() {
        return creditosAcumulados;
    }

    /**
     * Calcula os créditos baseado nas horas semanais
     * Relação: 4 horas = 4 créditos, 2 horas = 2 créditos, etc.
     */
    private int calcularCreditos(int horasSemanais) {
        return horasSemanais; // A relação é 1:1 (1 hora = 1 crédito)
    }

    // Adiciona uma disciplina cursada com sua nota
    public void addCompletedSubject(Subject subject, double grade) {
        completedSubjects.put(subject, grade);
        
        // Se a nota for >= 6.0 (aprovado), adiciona os créditos
        if (grade >= 6.0) {
            creditosAcumulados += calcularCreditos(subject.getWeeklyHours());
        }
    }

    // Verifica se o estudante cursou uma disciplina específica
    public boolean hasCompletedSubject(Subject subject) {
        return completedSubjects.containsKey(subject);
    }

    // Obtém a nota de uma disciplina cursada
    public Double getGrade(Subject subject) {
        return completedSubjects.get(subject);
    }

    // Adiciona uma disciplina ao planejamento futuro
    public void addToFuturePlanning(Subject subject) throws CargaHorariaExcedidaException {
        int novaCargaHoraria = getFuturePlanningWeeklyHours() + subject.getWeeklyHours();
        if (novaCargaHoraria > maxWeeklyHours) {
            throw new CargaHorariaExcedidaException(
                "Carga horária excedida: " + novaCargaHoraria + "h > " + maxWeeklyHours + "h"
            );
        }
        futurePlanning.add(subject);
    }

    // Remove uma disciplina do planejamento futuro
    public void removeFromFuturePlanning(Subject subject) {
        futurePlanning.remove(subject);
    }

    /**
     * Define um co-requisito para uma disciplina
     */
    public void setCoRequisito(Subject disciplina, Subject coRequisito) {
        coRequisitos.put(disciplina, coRequisito);
    }

    /**
     * Verifica se um co-requisito é atendido
     */
    public boolean isCoRequisitoAtendido(Subject disciplina) {
        Subject coRequisito = coRequisitos.get(disciplina);
        if (coRequisito == null) {
            return true; // Não há co-requisito
        }
        return futurePlanning.contains(coRequisito) || completedSubjects.containsKey(coRequisito);
    }

    /**
     * Adiciona uma disciplina ao planejamento verificando co-requisitos
     */
    public void addToFuturePlanningWithCoRequisito(Subject disciplina) 
            throws CargaHorariaExcedidaException, CoRequisitoNaoAtendidoException {
        
        // Verificar co-requisito
        if (!isCoRequisitoAtendido(disciplina)) {
            Subject coRequisito = coRequisitos.get(disciplina);
            throw new CoRequisitoNaoAtendidoException(
                "Co-requisito não atendido: " + disciplina.getCode() + 
                " requer " + coRequisito.getCode() + " no mesmo período"
            );
        }
        
        addToFuturePlanning(disciplina);
    }

    // Calcula o total de horas semanais do planejamento futuro
    public int getFuturePlanningWeeklyHours() {
        int total = 0;
        for (Subject subject : futurePlanning) {
            total += subject.getWeeklyHours();
        }
        return total;
    }

    // Verifica se o planejamento futuro está dentro do limite de horas
    public boolean isFuturePlanningWithinLimit() {
        return getFuturePlanningWeeklyHours() <= maxWeeklyHours;
    }

    // Obtém as horas restantes disponíveis para o planejamento
    public int getRemainingWeeklyHours() {
        return maxWeeklyHours - getFuturePlanningWeeklyHours();
    }
}
