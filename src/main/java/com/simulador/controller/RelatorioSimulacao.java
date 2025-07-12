package com.simulador.controller;

import com.simulador.model.domain.ClassGroup;
import com.simulador.model.domain.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa o relatório de uma simulação de planejamento acadêmico.
 * Armazena informações sobre sucesso, erros, avisos e turmas planejadas.
 */
public class RelatorioSimulacao {
    
    private final Student aluno;
    private boolean sucesso;
    private final List<String> erros;
    private final List<String> avisos;
    private final List<String> informacoes;
    private final List<ClassGroup> turmasPlanejadas;
    
    public RelatorioSimulacao(Student aluno) {
        this.aluno = aluno;
        this.sucesso = false;
        this.erros = new ArrayList<>();
        this.avisos = new ArrayList<>();
        this.informacoes = new ArrayList<>();
        this.turmasPlanejadas = new ArrayList<>();
    }
    
    /**
     * Adiciona um erro ao relatório
     */
    public void adicionarErro(String erro) {
        erros.add(erro);
    }
    
    /**
     * Adiciona um aviso ao relatório
     */
    public void adicionarAviso(String aviso) {
        avisos.add(aviso);
    }
    
    /**
     * Adiciona uma informação ao relatório
     */
    public void adicionarInfo(String info) {
        informacoes.add(info);
    }
    
    /**
     * Adiciona uma turma ao planejamento
     */
    public void adicionarTurmaPlanejada(ClassGroup turma) {
        turmasPlanejadas.add(turma);
    }
    
    /**
     * Define se a simulação foi bem-sucedida
     */
    public void setSucesso(boolean sucesso) {
        this.sucesso = sucesso;
    }
    
    // Getters
    public Student getAluno() {
        return aluno;
    }
    
    public boolean isSucesso() {
        return sucesso;
    }
    
    public List<String> getErros() {
        return new ArrayList<>(erros);
    }
    
    public List<String> getAvisos() {
        return new ArrayList<>(avisos);
    }
    
    public List<String> getInformacoes() {
        return new ArrayList<>(informacoes);
    }
    
    public List<ClassGroup> getTurmasPlanejadas() {
        return new ArrayList<>(turmasPlanejadas);
    }
    
    /**
     * Verifica se há erros no relatório
     */
    public boolean temErros() {
        return !erros.isEmpty();
    }
    
    /**
     * Verifica se há avisos no relatório
     */
    public boolean temAvisos() {
        return !avisos.isEmpty();
    }
    
    /**
     * Gera uma representação em string do relatório
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RELATÓRIO DE SIMULAÇÃO ===\n");
        sb.append("Aluno: ").append(aluno.getName()).append(" (").append(aluno.getRegistration()).append(")\n");
        sb.append("Status: ").append(sucesso ? "SUCESSO" : "FALHA").append("\n\n");
        
        if (!informacoes.isEmpty()) {
            sb.append("INFORMAÇÕES:\n");
            for (String info : informacoes) {
                sb.append("  - ").append(info).append("\n");
            }
            sb.append("\n");
        }
        
        if (!avisos.isEmpty()) {
            sb.append("AVISOS:\n");
            for (String aviso : avisos) {
                sb.append("  - ").append(aviso).append("\n");
            }
            sb.append("\n");
        }
        
        if (!erros.isEmpty()) {
            sb.append("ERROS:\n");
            for (String erro : erros) {
                sb.append("  - ").append(erro).append("\n");
            }
            sb.append("\n");
        }
        
        if (!turmasPlanejadas.isEmpty()) {
            sb.append("TURMAS PLANEJADAS:\n");
            for (ClassGroup turma : turmasPlanejadas) {
                sb.append("  - ").append(turma.getSubject().getCode())
                   .append(" (").append(turma.getId()).append(")\n");
            }
        }
        
        return sb.toString();
    }
} 