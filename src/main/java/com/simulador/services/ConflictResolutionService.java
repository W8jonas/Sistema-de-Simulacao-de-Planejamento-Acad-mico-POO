package com.simulador.services;

import com.simulador.model.domain.*;
import java.util.*;

/**
 * Serviço para resolver conflitos de matrícula baseado na precedência das disciplinas
 */
public class ConflictResolutionService {
    
    /**
     * Resolve conflitos de horário baseado na precedência das disciplinas
     * Obrigatória > Eletiva > Optativa
     */
    public List<Subject> resolveConflicts(Set<Subject> disciplinasConflitantes) {
        List<Subject> disciplinasOrdenadas = new ArrayList<>(disciplinasConflitantes);
        
        // Ordenar por precedência: Obrigatória > Eletiva > Optativa
        disciplinasOrdenadas.sort((d1, d2) -> {
            int precedencia1 = getPrecedence(d1);
            int precedencia2 = getPrecedence(d2);
            return Integer.compare(precedencia2, precedencia1); // Ordem decrescente
        });
        
        return disciplinasOrdenadas;
    }
    
    /**
     * Obtém a precedência de uma disciplina
     * 3 = Obrigatória (maior precedência)
     * 2 = Eletiva
     * 1 = Optativa (menor precedência)
     */
    private int getPrecedence(Subject disciplina) {
        if (disciplina instanceof RequiredSubject) {
            return 3;
        } else if (disciplina instanceof ElectiveSubject) {
            return 2;
        } else if (disciplina instanceof OptionalSubject) {
            return 1;
        }
        return 0;
    }
    
    /**
     * Verifica se há conflito entre disciplinas de mesma precedência
     */
    public boolean hasSamePrecedenceConflict(Set<Subject> disciplinas) {
        if (disciplinas.size() <= 1) {
            return false;
        }
        
        Set<Integer> precedencias = new HashSet<>();
        for (Subject disciplina : disciplinas) {
            precedencias.add(getPrecedence(disciplina));
        }
        
        // Se há apenas uma precedência, não há conflito
        return precedencias.size() == 1;
    }
    
    /**
     * Obtém a disciplina com maior precedência de um conjunto
     */
    public Subject getHighestPrecedenceSubject(Set<Subject> disciplinas) {
        if (disciplinas.isEmpty()) {
            return null;
        }
        
        return disciplinas.stream()
                .max((d1, d2) -> Integer.compare(getPrecedence(d1), getPrecedence(d2)))
                .orElse(null);
    }
    
    /**
     * Filtra disciplinas por tipo
     */
    public List<Subject> filterByType(Set<Subject> disciplinas, Class<? extends Subject> tipo) {
        return disciplinas.stream()
                .filter(tipo::isInstance)
                .toList();
    }
} 