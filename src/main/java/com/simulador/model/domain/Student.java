package com.simulador.model.domain;

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

    public Student(String name, String registration, int maxWeeklyHours) {
        this.name = name;
        this.registration = registration;
        this.maxWeeklyHours = maxWeeklyHours;
        this.completedSubjects = new HashMap<>();
        this.futurePlanning = new HashSet<>();
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

    // Adiciona uma disciplina cursada com sua nota
    public void addCompletedSubject(Subject subject, double grade) {
        completedSubjects.put(subject, grade);
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
    public void addToFuturePlanning(Subject subject) {
        futurePlanning.add(subject);
    }

    // Remove uma disciplina do planejamento futuro
    public void removeFromFuturePlanning(Subject subject) {
        futurePlanning.remove(subject);
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
