package com.simulador;

import com.simulador.controller.Registration;
import com.simulador.controller.RelatorioSimulacao;
import com.simulador.model.domain.ClassGroup;
import com.simulador.model.domain.Student;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Classe principal que demonstra o uso do sistema de simulação de planejamento acadêmico.
 * Interface interativa para testar diferentes cenários de matrícula.
 */
public class Main {
    
    private static Registration registration;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE SIMULAÇÃO DE PLANEJAMENTO ACADÊMICO ===\n");
        
        // Inicializar o sistema
        registration = new Registration();
        registration.inicializarSistema();
        scanner = new Scanner(System.in);
        
        System.out.println("Sistema inicializado com sucesso!\n");
        
        // Mostrar menu principal
        mostrarMenuPrincipal();
    }
    
    private static void mostrarMenuPrincipal() {
        while (true) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Ver relatório do aluno");
            System.out.println("2. Listar disciplinas disponíveis");
            System.out.println("3. Listar turmas disponíveis");
            System.out.println("4. Verificar elegibilidade para disciplina");
            System.out.println("5. Tentar matrícula em turma(s)");
            System.out.println("6. Ver histórico do aluno");
            System.out.println("7. Executar testes automáticos");
            System.out.println("0. Sair");
            System.out.print("\nEscolha uma opção: ");
            
            String opcao = scanner.nextLine().trim();
            
            switch (opcao) {
                case "1":
                    verRelatorioAluno();
                    break;
                case "2":
                    listarDisciplinas();
                    break;
                case "3":
                    listarTurmas();
                    break;
                case "4":
                    verificarElegibilidade();
                    break;
                case "5":
                    tentarMatricula();
                    break;
                case "6":
                    verHistoricoAluno();
                    break;
                case "7":
                    executarTestesAutomaticos();
                    break;
                case "0":
                    System.out.println("Saindo do sistema...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }
    
    private static void verRelatorioAluno() {
        System.out.println("\n=== RELATÓRIO DO ALUNO ===");
        RelatorioSimulacao relatorio = registration.gerarRelatorioAluno("202365082A");
        System.out.println(relatorio);
    }
    
    private static void listarDisciplinas() {
        System.out.println("\n=== DISCIPLINAS DISPONÍVEIS ===");
        registration.listarDisciplinas().forEach(disciplina -> 
            System.out.println("  " + disciplina.getCode() + " - " + disciplina.getName() + " (" + disciplina.getWeeklyHours() + "h)"));
    }
    
    private static void listarTurmas() {
        System.out.println("\n=== TURMAS DISPONÍVEIS ===");
        registration.listarTurmas().forEach(turma -> 
            System.out.println("  " + turma.getId() + " - " + turma.getSubject().getName() + 
                             " (Capacidade: " + turma.getCapacity() + ", Horários: " + turma.getSchedules() + ")"));
    }
    
    private static void verificarElegibilidade() {
        System.out.println("\n=== VERIFICAR ELEGIBILIDADE ===");
        System.out.print("Digite o código da disciplina (ex: MAT154, DCC013): ");
        String codigoDisciplina = scanner.nextLine().trim().toUpperCase();
        
        RelatorioSimulacao relatorio = registration.verificarElegibilidade("202365082A", codigoDisciplina);
        System.out.println(relatorio);
    }
    
    private static void tentarMatricula() {
        System.out.println("\n=== TENTAR MATRÍCULA ===");
        System.out.println("Turmas disponíveis:");
        registration.listarTurmas().forEach(turma -> 
            System.out.println("  " + turma.getId() + " - " + turma.getSubject().getName()));
        
        System.out.print("\nDigite os IDs das turmas separados por vírgula (ex: MAT156-01,DCC025-01): ");
        String input = scanner.nextLine().trim();
        
        if (input.isEmpty()) {
            System.out.println("Nenhuma turma especificada.");
            return;
        }
        
        List<String> turmas = Arrays.asList(input.split(","));
        for (int i = 0; i < turmas.size(); i++) {
            turmas.set(i, turmas.get(i).trim());
        }
        
        RelatorioSimulacao relatorio = registration.processarMatricula("202365082A", turmas);
        System.out.println(relatorio);
    }
    
    private static void verHistoricoAluno() {
        System.out.println("\n=== HISTÓRICO DO ALUNO ===");
        Student aluno = registration.buscarAluno("202365082A");
        
        System.out.println("Aluno: " + aluno.getName() + " (" + aluno.getRegistration() + ")");
        System.out.println("Limite de horas semanais: " + aluno.getMaxWeeklyHours());
        
        System.out.println("\nDisciplinas cursadas:");
        if (aluno.getCompletedSubjects().isEmpty()) {
            System.out.println("  Nenhuma disciplina cursada.");
        } else {
            aluno.getCompletedSubjects().forEach((disciplina, nota) -> 
                System.out.println("  " + disciplina.getCode() + " - " + disciplina.getName() + " (Nota: " + nota + ")"));
        }
        
        System.out.println("\nDisciplinas no planejamento:");
        if (aluno.getFuturePlanning().isEmpty()) {
            System.out.println("  Nenhuma disciplina no planejamento.");
        } else {
            aluno.getFuturePlanning().forEach(disciplina -> 
                System.out.println("  " + disciplina.getCode() + " - " + disciplina.getName()));
        }
        
        System.out.println("\nCarga horária planejada: " + aluno.getFuturePlanningWeeklyHours() + "h");
        System.out.println("Horas restantes disponíveis: " + aluno.getRemainingWeeklyHours() + "h");
    }
    
    private static void executarTestesAutomaticos() {
        System.out.println("\n=== EXECUTANDO TESTES AUTOMÁTICOS ===");
        
        // TESTE 1: Relatório inicial do aluno
        System.out.println("--- TESTE 1: Relatório Inicial do Aluno ---");
        RelatorioSimulacao relatorio1 = registration.gerarRelatorioAluno("202365082A");
        System.out.println(relatorio1);
        
        // TESTE 2: ValidadorSimples - Disciplina sem pré-requisitos
        System.out.println("\n--- TESTE 2: ValidadorSimples - Cálculo I (já cursada) ---");
        RelatorioSimulacao relatorio2 = registration.verificarElegibilidade("202365082A", "MAT154");
        System.out.println(relatorio2);
        
        // TESTE 3: ValidadorLogicoAND - Disciplina com múltiplos pré-requisitos (todos atendidos)
        System.out.println("\n--- TESTE 3: ValidadorLogicoAND - Cálculo II (pré-requisitos atendidos) ---");
        RelatorioSimulacao relatorio3 = registration.verificarElegibilidade("202365082A", "MAT156");
        System.out.println(relatorio3);
        
        // TESTE 4: ValidadorLogicoAND - Disciplina com múltiplos pré-requisitos (alguns não atendidos)
        System.out.println("\n--- TESTE 4: ValidadorLogicoAND - Física II (pré-requisitos não atendidos) ---");
        RelatorioSimulacao relatorio4 = registration.verificarElegibilidade("202365082A", "FIS074");
        System.out.println(relatorio4);
        
        // TESTE 5: Matrícula válida sem conflitos
        System.out.println("\n--- TESTE 5: Matrícula Válida - Cálculo II ---");
        List<String> turmasValidas = Arrays.asList("MAT156-01");
        RelatorioSimulacao relatorio5 = registration.processarMatricula("202365082A", turmasValidas);
        System.out.println(relatorio5);
        
        // TESTE 6: Conflito de horário (duas turmas no mesmo horário)
        System.out.println("\n--- TESTE 6: Conflito de Horário ---");
        List<String> turmasComConflito = Arrays.asList("MAT154-01", "MAT154-03"); // Mesmo horário
        RelatorioSimulacao relatorio6 = registration.processarMatricula("202365082A", turmasComConflito);
        System.out.println(relatorio6);
        
        System.out.println("\n=== FIM DOS TESTES AUTOMÁTICOS ===");
    }
}