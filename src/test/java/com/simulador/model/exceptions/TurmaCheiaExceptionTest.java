package com.simulador.model.exceptions;

import com.simulador.model.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

/**
 * Testes para TurmaCheiaException
 */
@DisplayName("Testes de TurmaCheiaException")
public class TurmaCheiaExceptionTest {
    
    private ClassGroup turma;
    private Student aluno1, aluno2, aluno3;
    private Subject disciplina;
    
    @BeforeEach
    void setUp() {
        disciplina = new RequiredSubject("MAT154", "Cálculo I", 4);
        turma = new ClassGroup("MAT154-01", disciplina, 2, 
                              Arrays.asList(new Schedule(1, 8, 10)));
        aluno1 = new Student("João Silva", "202365082A", 20);
        aluno2 = new Student("Maria Santos", "202365083B", 20);
        aluno3 = new Student("Pedro Costa", "202365084C", 20);
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando turma está cheia")
    void testExcecaoTurmaCheia() throws TurmaCheiaException {
        // ARRANGE: Preenche a turma até a capacidade
        turma.enrollStudent(aluno1);
        turma.enrollStudent(aluno2);
        
        // ACT & ASSERT: Deve lançar exceção ao tentar matricular mais um aluno
        assertThrows(TurmaCheiaException.class, () -> {
            turma.enrollStudent(aluno3);
        }, "Deve lançar exceção quando turma está cheia");
    }
    
    @Test
    @DisplayName("Não deve lançar exceção quando há vagas disponíveis")
    void testNaoExcecaoComVagas() throws TurmaCheiaException {
        // ARRANGE: Turma vazia
        
        // ACT & ASSERT: Não deve lançar exceção
        assertDoesNotThrow(() -> {
            turma.enrollStudent(aluno1);
        }, "Não deve lançar exceção quando há vagas disponíveis");
    }
    
    @Test
    @DisplayName("Não deve lançar exceção quando há uma vaga disponível")
    void testNaoExcecaoUmaVaga() throws TurmaCheiaException {
        // ARRANGE: Matricula um aluno
        turma.enrollStudent(aluno1);
        
        // ACT & ASSERT: Não deve lançar exceção
        assertDoesNotThrow(() -> {
            turma.enrollStudent(aluno2);
        }, "Não deve lançar exceção quando há uma vaga disponível");
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao tentar matricular aluno já matriculado")
    void testExcecaoAlunoJaMatriculado() throws TurmaCheiaException {
        // ARRANGE: Matricula o aluno
        turma.enrollStudent(aluno1);
        
        // ACT & ASSERT: Deve lançar exceção ao tentar matricular novamente
        assertThrows(TurmaCheiaException.class, () -> {
            turma.enrollStudent(aluno1);
        }, "Deve lançar exceção ao tentar matricular aluno já matriculado");
    }
    
    @Test
    @DisplayName("Deve permitir matrícula após remover aluno")
    void testMatriculaAposRemover() throws TurmaCheiaException {
        // ARRANGE: Preenche a turma
        turma.enrollStudent(aluno1);
        turma.enrollStudent(aluno2);
        
        // Remove um aluno
        turma.removeStudent(aluno1);
        
        // ACT & ASSERT: Deve permitir nova matrícula
        assertDoesNotThrow(() -> {
            turma.enrollStudent(aluno3);
        }, "Deve permitir matrícula após remover aluno");
    }
    
    @Test
    @DisplayName("Deve verificar corretamente o número de vagas disponíveis")
    void testVagasDisponiveis() throws TurmaCheiaException {
        // ARRANGE: Turma vazia
        assertEquals(2, turma.getAvailableSlots(), "Turma deve ter 2 vagas disponíveis");
        
        // Matricula um aluno
        turma.enrollStudent(aluno1);
        assertEquals(1, turma.getAvailableSlots(), "Turma deve ter 1 vaga disponível");
        
        // Matricula segundo aluno
        turma.enrollStudent(aluno2);
        assertEquals(0, turma.getAvailableSlots(), "Turma deve ter 0 vagas disponíveis");
    }
    
    @Test
    @DisplayName("Deve verificar se há vagas disponíveis")
    void testHasAvailableSlots() throws TurmaCheiaException {
        // ARRANGE: Turma vazia
        assertTrue(turma.hasAvailableSlots(), "Turma deve ter vagas disponíveis");
        
        // Matricula um aluno
        turma.enrollStudent(aluno1);
        assertTrue(turma.hasAvailableSlots(), "Turma ainda deve ter vagas disponíveis");
        
        // Matricula segundo aluno
        turma.enrollStudent(aluno2);
        assertFalse(turma.hasAvailableSlots(), "Turma não deve ter vagas disponíveis");
    }
    
    @Test
    @DisplayName("Deve contar corretamente alunos matriculados")
    void testContagemAlunos() throws TurmaCheiaException {
        // ARRANGE: Turma vazia
        assertEquals(0, turma.getEnrolledStudentsCount(), "Turma deve ter 0 alunos");
        
        // Matricula um aluno
        turma.enrollStudent(aluno1);
        assertEquals(1, turma.getEnrolledStudentsCount(), "Turma deve ter 1 aluno");
        
        // Matricula segundo aluno
        turma.enrollStudent(aluno2);
        assertEquals(2, turma.getEnrolledStudentsCount(), "Turma deve ter 2 alunos");
    }
    
    @Test
    @DisplayName("Deve verificar se aluno está matriculado")
    void testIsStudentEnrolled() throws TurmaCheiaException {
        // ARRANGE: Turma vazia
        assertFalse(turma.isStudentEnrolled(aluno1), "Aluno não deve estar matriculado");
        
        // Matricula o aluno
        turma.enrollStudent(aluno1);
        assertTrue(turma.isStudentEnrolled(aluno1), "Aluno deve estar matriculado");
        
        // Remove o aluno
        turma.removeStudent(aluno1);
        assertFalse(turma.isStudentEnrolled(aluno1), "Aluno não deve estar matriculado após remoção");
    }
} 