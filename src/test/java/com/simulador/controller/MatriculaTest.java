package com.simulador.controller;

import com.simulador.model.domain.*;
import com.simulador.model.validator.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * Teste obrigatório: Matrícula bem-sucedida em uma turma quando todos os requisitos são atendidos
 */
@DisplayName("Teste de Matrícula Bem-Sucedida")
public class MatriculaTest {
    
    private Registration registration;
    private Student aluno;
    private Subject calculoI;
    private Subject calculoII;
    private ClassGroup turmaCalculoII;
    
    @BeforeEach
    void setUp() {
        // Inicializar o sistema
        registration = new Registration();
        registration.inicializarSistema();
        
        // Buscar o aluno de teste
        aluno = registration.buscarAluno("202365082A");
        
        // Buscar disciplinas e turmas do sistema através do serviço
        ServicoMatricula servico = registration.getServicoMatricula();
        calculoI = servico.getDisciplina("MAT154");
        calculoII = servico.getDisciplina("MAT156");
        turmaCalculoII = servico.getTurma("MAT156-01");
    }
    
    @Test
    @DisplayName("Matrícula bem-sucedida quando todos os requisitos são atendidos")
    void testMatriculaBemSucedida() {
        // ARRANGE: Preparar o cenário
        // O aluno já cursou Cálculo I (pré-requisito para Cálculo II)
        assertTrue(aluno.hasCompletedSubject(calculoI), "Aluno deve ter cursado Cálculo I");
        
        // Verificar que a turma existe e tem capacidade
        assertNotNull(turmaCalculoII, "Turma deve existir");
        assertTrue(turmaCalculoII.getCapacity() > 0, "Turma deve ter capacidade");
        
        // Verificar que o aluno atende aos pré-requisitos
        assertTrue(calculoII.requisitosAtendidos(aluno), "Aluno deve atender aos pré-requisitos");
        
        // ACT: Executar a matrícula
        List<String> turmasParaMatricular = Arrays.asList("MAT156-01");
        RelatorioSimulacao relatorio = registration.processarMatricula("202365082A", turmasParaMatricular);
        
        // ASSERT: Verificar que a matrícula foi bem-sucedida
        assertTrue(relatorio.isSucesso(), "Matrícula deve ser bem-sucedida");
        assertFalse(relatorio.temErros(), "Não deve haver erros na matrícula");
        
        // Verificar que não há mensagens de erro específicas
        List<String> erros = relatorio.getErros();
        boolean temErroConflito = erros.stream().anyMatch(msg -> 
            msg.toLowerCase().contains("conflito") || msg.toLowerCase().contains("horário"));
        boolean temErroPreRequisito = erros.stream().anyMatch(msg -> 
            msg.toLowerCase().contains("pré-requisito"));
        
        assertFalse(temErroConflito, "Não deve haver conflitos de horário");
        assertFalse(temErroPreRequisito, "Não deve haver problemas de pré-requisito");
        
        // Verificar que a disciplina foi adicionada ao planejamento do aluno
        assertTrue(aluno.getFuturePlanning().contains(calculoII), "Disciplina deve estar no planejamento do aluno");
        
        // Verificar que a carga horária foi atualizada corretamente
        int cargaHorariaEsperada = calculoII.getWeeklyHours();
        assertTrue(aluno.getFuturePlanningWeeklyHours() >= cargaHorariaEsperada, 
                  "Carga horária do aluno deve incluir a nova disciplina");
        
        System.out.println("✓ Teste de matrícula bem-sucedida passou!");
        System.out.println("Relatório da matrícula:");
        System.out.println(relatorio);
    }
    
    @Test
    @DisplayName("Verificar elegibilidade para disciplina com pré-requisitos atendidos")
    void testElegibilidadeComPreRequisitosAtendidos() {
        // ARRANGE: Preparar o cenário
        assertTrue(aluno.hasCompletedSubject(calculoI), "Aluno deve ter cursado Cálculo I");
        
        // ACT: Verificar elegibilidade
        RelatorioSimulacao relatorio = registration.verificarElegibilidade("202365082A", "MAT156");
        
        // ASSERT: Verificar que o aluno é elegível
        assertTrue(relatorio.isSucesso(), "Aluno deve ser elegível para a disciplina");
        
        // Verificar que não há erros (indicando que é elegível)
        assertFalse(relatorio.temErros(), "Aluno elegível não deve ter erros no relatório");
        
        // Verificar que há informações sobre a disciplina
        List<String> informacoes = relatorio.getInformacoes();
        boolean temInfoDisciplina = informacoes.stream().anyMatch(msg -> 
            msg.contains("MAT156") || msg.contains("Cálculo II"));
        
        assertTrue(temInfoDisciplina, "Relatório deve conter informações sobre a disciplina");
        
        System.out.println("✓ Teste de elegibilidade passou!");
    }
    
    @Test
    @DisplayName("Verificar que não há conflitos de horário na matrícula válida")
    void testSemConflitosDeHorario() {
        // ARRANGE: Preparar o cenário
        assertTrue(aluno.hasCompletedSubject(calculoI), "Aluno deve ter cursado Cálculo I");
        
        // ACT: Tentar matrícula
        List<String> turmasParaMatricular = Arrays.asList("MAT156-01");
        RelatorioSimulacao relatorio = registration.processarMatricula("202365082A", turmasParaMatricular);
        
        // ASSERT: Verificar que não há conflitos
        assertTrue(relatorio.isSucesso(), "Matrícula deve ser bem-sucedida");
        
        // Verificar que não há mensagens sobre conflitos
        List<String> erros = relatorio.getErros();
        List<String> avisos = relatorio.getAvisos();
        boolean temConflitos = erros.stream().anyMatch(msg -> 
            msg.toLowerCase().contains("conflito") || msg.toLowerCase().contains("horário") ||
            msg.toLowerCase().contains("schedule")) ||
            avisos.stream().anyMatch(msg -> 
            msg.toLowerCase().contains("conflito") || msg.toLowerCase().contains("horário") ||
            msg.toLowerCase().contains("schedule"));
        
        assertFalse(temConflitos, "Não deve haver conflitos de horário");
        
        System.out.println("✓ Teste de ausência de conflitos passou!");
    }
} 