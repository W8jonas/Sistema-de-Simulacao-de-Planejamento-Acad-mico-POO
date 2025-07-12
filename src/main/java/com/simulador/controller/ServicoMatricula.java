package com.simulador.controller;

import com.simulador.model.domain.*;
import com.simulador.model.validator.ValidadorPreRequisito;
import com.simulador.services.VerifyDependencies;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller principal que gerencia o processo de matrícula e planejamento acadêmico.
 * Implementa a lógica de negócio para validação de pré-requisitos, conflitos de horário
 * e geração de relatórios de planejamento.
 */
public class ServicoMatricula {
    
    private final Map<String, Subject> disciplinasRepo;
    private final Map<String, ClassGroup> turmasRepo;
    private final Map<String, Student> alunosRepo;
    private final VerifyDependencies verifyDependencies;
    
    public ServicoMatricula() {
        this.disciplinasRepo = new HashMap<>();
        this.turmasRepo = new HashMap<>();
        this.alunosRepo = new HashMap<>();
        this.verifyDependencies = new VerifyDependencies();
    }
    
    /**
     * Registra uma disciplina no sistema
     */
    public void registrarDisciplina(Subject disciplina) {
        disciplinasRepo.put(disciplina.getCode(), disciplina);
    }
    
    /**
     * Registra uma turma no sistema
     */
    public void registrarTurma(ClassGroup turma) {
        turmasRepo.put(turma.getId(), turma);
    }
    
    /**
     * Registra um aluno no sistema
     */
    public void registrarAluno(Student aluno) {
        alunosRepo.put(aluno.getRegistration(), aluno);
    }
    
    /**
     * Planeja a matrícula de um aluno em um conjunto de turmas
     */
    public RelatorioSimulacao planejar(Student aluno, Set<ClassGroup> turmasDesejadas) {
        RelatorioSimulacao relatorio = new RelatorioSimulacao(aluno);
        
        // Validações básicas
        if (aluno == null) {
            relatorio.adicionarErro("Aluno não pode ser nulo");
            return relatorio;
        }
        
        if (turmasDesejadas == null || turmasDesejadas.isEmpty()) {
            relatorio.adicionarErro("Deve ser fornecido pelo menos uma turma para planejamento");
            return relatorio;
        }
        
        // Verificar conflitos de horário entre as turmas
        List<String> conflitosHorario = verificarConflitosHorario(turmasDesejadas);
        if (!conflitosHorario.isEmpty()) {
            for (String conflito : conflitosHorario) {
                relatorio.adicionarErro("Conflito de horário: " + conflito);
            }
        }
        
        // Verificar pré-requisitos para cada disciplina
        for (ClassGroup turma : turmasDesejadas) {
            Subject disciplina = turma.getSubject();
            
            // Verificar se o aluno já cursou esta disciplina
            if (aluno.hasCompletedSubject(disciplina)) {
                relatorio.adicionarAviso("Aluno já cursou esta disciplina: " + disciplina.getCode() + " - " + disciplina.getName() + 
                                       " (Nota: " + aluno.getGrade(disciplina) + ")");
            }
            
            List<String> errosPreRequisitos = verificarPreRequisitos(aluno, disciplina);
            
            for (String erro : errosPreRequisitos) {
                relatorio.adicionarErro("Pré-requisito não atendido para " + disciplina.getCode() + ": " + erro);
            }
        }
        
        // Verificar carga horária semanal
        int cargaHorariaTotal = calcularCargaHorariaTotal(turmasDesejadas);
        if (cargaHorariaTotal > aluno.getMaxWeeklyHours()) {
            relatorio.adicionarErro("Carga horária semanal excede o limite: " + 
                cargaHorariaTotal + "h > " + aluno.getMaxWeeklyHours() + "h");
        }
        
        // Se não há erros, adicionar ao planejamento
        if (relatorio.getErros().isEmpty()) {
            for (ClassGroup turma : turmasDesejadas) {
                aluno.addToFuturePlanning(turma.getSubject());
                relatorio.adicionarTurmaPlanejada(turma);
            }
            relatorio.setSucesso(true);
        }
        
        return relatorio;
    }
    
    /**
     * Verifica conflitos de horário entre as turmas
     */
    private List<String> verificarConflitosHorario(Set<ClassGroup> turmas) {
        List<String> conflitos = new ArrayList<>();
        List<ClassGroup> turmasList = new ArrayList<>(turmas);
        
        for (int i = 0; i < turmasList.size(); i++) {
            for (int j = i + 1; j < turmasList.size(); j++) {
                ClassGroup turma1 = turmasList.get(i);
                ClassGroup turma2 = turmasList.get(j);
                
                if (turma1.conflictsWith(turma2)) {
                    conflitos.add(turma1.getSubject().getCode() + " (" + turma1.getId() + 
                                ") conflita com " + turma2.getSubject().getCode() + " (" + turma2.getId() + ")");
                }
            }
        }
        
        return conflitos;
    }
    
    /**
     * Verifica se o aluno atende aos pré-requisitos de uma disciplina
     */
    private List<String> verificarPreRequisitos(Student aluno, Subject disciplina) {
        List<String> erros = new ArrayList<>();
        
        if (!disciplina.requisitosAtendidos(aluno)) {
            erros.add("Pré-requisitos não atendidos para " + disciplina.getCode() + " - " + disciplina.getName());
        }
        
        return erros;
    }
    
    /**
     * Calcula a carga horária total das turmas
     */
    private int calcularCargaHorariaTotal(Set<ClassGroup> turmas) {
        return turmas.stream()
                .mapToInt(turma -> turma.getSubject().getWeeklyHours())
                .sum();
    }
    
    /**
     * Gera um relatório completo de simulação para um aluno
     */
    public RelatorioSimulacao gerarRelatorio(Student aluno) {
        RelatorioSimulacao relatorio = new RelatorioSimulacao(aluno);
        
        // Informações básicas do aluno
        relatorio.adicionarInfo("Aluno: " + aluno.getName());
        relatorio.adicionarInfo("Matrícula: " + aluno.getRegistration());
        relatorio.adicionarInfo("Limite de horas semanais: " + aluno.getMaxWeeklyHours());
        
        // Disciplinas cursadas
        relatorio.adicionarInfo("Disciplinas cursadas: " + aluno.getCompletedSubjects().size());
        for (Map.Entry<Subject, Double> entry : aluno.getCompletedSubjects().entrySet()) {
            relatorio.adicionarInfo("  " + entry.getKey().getCode() + " - Nota: " + entry.getValue());
        }
        
        // Planejamento futuro
        relatorio.adicionarInfo("Disciplinas no planejamento: " + aluno.getFuturePlanning().size());
        for (Subject disciplina : aluno.getFuturePlanning()) {
            relatorio.adicionarInfo("  " + disciplina.getCode() + " - " + disciplina.getName());
        }
        
        // Carga horária
        relatorio.adicionarInfo("Carga horária planejada: " + aluno.getFuturePlanningWeeklyHours() + "h");
        relatorio.adicionarInfo("Horas restantes disponíveis: " + aluno.getRemainingWeeklyHours() + "h");
        
        return relatorio;
    }
    
    /**
     * Obtém todas as disciplinas disponíveis
     */
    public List<Subject> getDisciplinasDisponiveis() {
        return new ArrayList<>(disciplinasRepo.values());
    }
    
    /**
     * Obtém todas as turmas disponíveis
     */
    public List<ClassGroup> getTurmasDisponiveis() {
        return new ArrayList<>(turmasRepo.values());
    }
    
    /**
     * Obtém um aluno por matrícula
     */
    public Student getAluno(String matricula) {
        return alunosRepo.get(matricula);
    }
    
    /**
     * Obtém uma disciplina por código
     */
    public Subject getDisciplina(String codigo) {
        return disciplinasRepo.get(codigo);
    }
    
    /**
     * Obtém uma turma por ID
     */
    public ClassGroup getTurma(String id) {
        return turmasRepo.get(id);
    }
} 