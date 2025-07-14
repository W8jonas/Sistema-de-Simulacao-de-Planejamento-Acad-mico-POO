package com.simulador.controller;

import com.simulador.model.domain.*;
import com.simulador.model.validator.ValidadorPreRequisito;
import com.simulador.model.exceptions.CargaHorariaExcedidaException;
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
        Set<ClassGroup> turmasComConflito = new HashSet<>();
        if (!conflitosHorario.isEmpty()) {
            for (String conflito : conflitosHorario) {
                relatorio.adicionarErro("Conflito de horário: " + conflito);
                // Marcar as turmas envolvidas no conflito
                String[] partes = conflito.split(" ");
                if (partes.length >= 4) {
                    String id1 = partes[0].substring(partes[0].indexOf("(") + 1, partes[0].indexOf(")"));
                    String id2 = partes[3].substring(partes[3].indexOf("(") + 1, partes[3].indexOf(")"));
                    for (ClassGroup turma : turmasDesejadas) {
                        if (turma.getId().equals(id1) || turma.getId().equals(id2)) {
                            turmasComConflito.add(turma);
                        }
                    }
                }
            }
        }
        
        int cargaHorariaAcumulada = aluno.getFuturePlanningWeeklyHours();
        for (ClassGroup turma : turmasDesejadas) {
            Subject disciplina = turma.getSubject();
            boolean aceita = true;
            String motivoRejeicao = "";
            
            // Se está em conflito, rejeita
            if (turmasComConflito.contains(turma)) {
                aceita = false;
                motivoRejeicao = "Conflito de horário";
            }
            // Se já cursou, apenas avisa
            if (aluno.hasCompletedSubject(disciplina)) {
                relatorio.adicionarAviso("Aluno já cursou esta disciplina: " + disciplina.getCode() + " - " + disciplina.getName() + " (Nota: " + aluno.getGrade(disciplina) + ")");
            }
            // Pré-requisitos
            List<String> errosPre = verificarPreRequisitos(aluno, disciplina);
            if (!errosPre.isEmpty()) {
                aceita = false;
                motivoRejeicao = String.join("; ", errosPre);
            }
            // Capacidade da turma
            if (!turma.hasAvailableSlots()) {
                aceita = false;
                motivoRejeicao = "Turma cheia";
            }
            // Carga horária
            if (aceita && (cargaHorariaAcumulada + disciplina.getWeeklyHours() > aluno.getMaxWeeklyHours())) {
                aceita = false;
                motivoRejeicao = "Carga horária excedida";
            }
            // Adiciona ao relatório
            if (aceita) {
                try {
                    aluno.addToFuturePlanning(disciplina);
                    relatorio.adicionarTurmaPlanejada(turma);
                    cargaHorariaAcumulada += disciplina.getWeeklyHours();
                } catch (CargaHorariaExcedidaException e) {
                    relatorio.adicionarErro("Carga horária excedida: " + e.getMessage());
                }
            } else {
                relatorio.adicionarErro("Turma " + turma.getId() + " rejeitada: " + motivoRejeicao);
            }
        }
        // Status final
        if (!relatorio.getTurmasPlanejadas().isEmpty()) {
            relatorio.setSucesso(true);
        } else {
            relatorio.setSucesso(false);
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

    /**
     * Finaliza a simulação, atualizando o histórico acadêmico do aluno
     * com as disciplinas que foram aceitas na simulação
     */
    public void finalizarSimulacao(RelatorioSimulacao relatorio, double nota) {
        if (relatorio == null) {
            throw new IllegalArgumentException("Relatório não pode ser nulo");
        }
        
        if (nota < 0.0 || nota > 10.0) {
            throw new IllegalArgumentException("Nota deve estar entre 0.0 e 10.0");
        }
        
        Student aluno = relatorio.getAluno();
        
        // Adicionar disciplinas aceitas ao histórico do aluno
        for (ClassGroup turma : relatorio.getTurmasPlanejadas()) {
            Subject disciplina = turma.getSubject();
            
            // Verificar se o aluno já cursou esta disciplina
            if (!aluno.hasCompletedSubject(disciplina)) {
                aluno.addCompletedSubject(disciplina, nota);
                System.out.println("✓ Disciplina " + disciplina.getCode() + " adicionada ao histórico com nota " + nota);
            } else {
                System.out.println("⚠ Disciplina " + disciplina.getCode() + " já estava no histórico (nota: " + aluno.getGrade(disciplina) + ")");
            }
        }
        
        // Limpar o planejamento futuro, já que as disciplinas foram cursadas
        for (ClassGroup turma : relatorio.getTurmasPlanejadas()) {
            aluno.removeFromFuturePlanning(turma.getSubject());
        }
        
        System.out.println("✅ Simulação finalizada! Histórico acadêmico atualizado.");
        System.out.println("Total de disciplinas adicionadas ao histórico: " + relatorio.getTurmasPlanejadas().size());
    }
    
    /**
     * Finaliza a simulação com nota padrão 7.0
     */
    public void finalizarSimulacao(RelatorioSimulacao relatorio) {
        finalizarSimulacao(relatorio, 7.0);
    }
} 