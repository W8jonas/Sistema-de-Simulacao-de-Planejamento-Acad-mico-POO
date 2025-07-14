package com.simulador.services;

import com.simulador.model.domain.ClassGroup;
import com.simulador.model.domain.Subject;
import com.simulador.model.exceptions.ConflitoDeHorarioException;
import java.util.*;

/**
 * Serviço responsável por resolver conflitos de horário entre disciplinas
 * baseado na precedência: Obrigatória > Eletiva > Optativa
 */
public class ConflictResolutionService {
    
    /**
     * Resolve conflitos de horário entre turmas baseado na precedência
     * @param turmas Lista de turmas candidatas
     * @return Lista de turmas aceitas após resolução de conflitos
     * @throws ConflitoDeHorarioException se há conflito entre disciplinas de mesma precedência
     */
    public List<ClassGroup> resolveConflicts(List<ClassGroup> turmas) throws ConflitoDeHorarioException {
        if (turmas == null || turmas.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Agrupar turmas por horário
        Map<String, List<ClassGroup>> turmasPorHorario = groupBySchedule(turmas);
        
        List<ClassGroup> turmasAceitas = new ArrayList<>();
        
        // Para cada horário, resolver conflitos
        for (List<ClassGroup> turmasNoHorario : turmasPorHorario.values()) {
            if (turmasNoHorario.size() == 1) {
                // Sem conflito
                turmasAceitas.add(turmasNoHorario.get(0));
            } else {
                // Há conflito - aplicar precedência
                ClassGroup turmaVencedora = resolveConflictByPrecedence(turmasNoHorario);
                if (turmaVencedora != null) {
                    turmasAceitas.add(turmaVencedora);
                }
            }
        }
        
        return turmasAceitas;
    }
    
    /**
     * Agrupa turmas por horário
     */
    private Map<String, List<ClassGroup>> groupBySchedule(List<ClassGroup> turmas) {
        Map<String, List<ClassGroup>> grupos = new HashMap<>();
        
        for (ClassGroup turma : turmas) {
            String horario = turma.getSchedules().toString(); // Simplificado
            grupos.computeIfAbsent(horario, k -> new ArrayList<>()).add(turma);
        }
        
        return grupos;
    }
    
    /**
     * Resolve conflito baseado na precedência das disciplinas
     * @param turmasConflitantes Lista de turmas no mesmo horário
     * @return Turma com maior precedência, ou null se há conflito entre mesma precedência
     * @throws ConflitoDeHorarioException se há conflito entre disciplinas de mesma precedência
     */
    private ClassGroup resolveConflictByPrecedence(List<ClassGroup> turmasConflitantes) 
            throws ConflitoDeHorarioException {
        
        if (turmasConflitantes.size() == 1) {
            return turmasConflitantes.get(0);
        }
        
        // Encontrar a disciplina com maior precedência
        ClassGroup turmaVencedora = null;
        int maiorPrecedencia = Integer.MAX_VALUE;
        
        for (ClassGroup turma : turmasConflitantes) {
            int precedencia = turma.getSubject().getPrecedence();
            if (precedencia < maiorPrecedencia) {
                maiorPrecedencia = precedencia;
                turmaVencedora = turma;
            }
        }
        
        // Verificar se há múltiplas disciplinas com a mesma precedência
        List<ClassGroup> turmasMesmaPrecedencia = new ArrayList<>();
        for (ClassGroup turma : turmasConflitantes) {
            if (turma.getSubject().getPrecedence() == maiorPrecedencia) {
                turmasMesmaPrecedencia.add(turma);
            }
        }
        
        // Se há mais de uma disciplina com a mesma precedência, há conflito
        if (turmasMesmaPrecedencia.size() > 1) {
            throw new ConflitoDeHorarioException(
                "Conflito entre disciplinas de mesma precedência: " + 
                turmasMesmaPrecedencia.stream()
                    .map(t -> t.getSubject().getCode() + " (" + t.getSubject().getType() + ")")
                    .reduce("", (a, b) -> a + ", " + b)
            );
        }
        
        return turmaVencedora;
    }
} 