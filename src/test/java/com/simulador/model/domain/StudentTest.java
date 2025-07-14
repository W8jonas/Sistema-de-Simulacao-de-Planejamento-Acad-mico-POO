package com.simulador.model.domain;

import com.simulador.model.exceptions.CargaHorariaExcedidaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe Student
 */
@DisplayName("Testes da Classe Student")
public class StudentTest {
    
    private Student aluno;
    private Subject disciplina1;
    private Subject disciplina2;
    
    @BeforeEach
    void setUp() {
        aluno = new Student("João Silva", "202365082A", 20);
        disciplina1 = new RequiredSubject("MAT154", "Cálculo I", 4);
        disciplina2 = new RequiredSubject("MAT156", "Cálculo II", 4);
    }
    
    @Test
    @DisplayName("Criar aluno com dados válidos")
    void testCriarAluno() {
        assertEquals("João Silva", aluno.getName());
        assertEquals("202365082A", aluno.getRegistration());
        assertEquals(20, aluno.getMaxWeeklyHours());
        assertTrue(aluno.getCompletedSubjects().isEmpty());
        assertTrue(aluno.getFuturePlanning().isEmpty());
    }
    
    @Test
    @DisplayName("Adicionar disciplina cursada")
    void testAdicionarDisciplinaCursada() {
        aluno.addCompletedSubject(disciplina1, 8.5);
        
        assertTrue(aluno.hasCompletedSubject(disciplina1));
        assertEquals(8.5, aluno.getGrade(disciplina1));
        assertEquals(1, aluno.getCompletedSubjects().size());
    }
    
    @Test
    @DisplayName("Adicionar disciplina ao planejamento futuro")
    void testAdicionarAoPlanejamentoFuturo() throws CargaHorariaExcedidaException {
        aluno.addToFuturePlanning(disciplina1);
        
        assertTrue(aluno.getFuturePlanning().contains(disciplina1));
        assertEquals(4, aluno.getFuturePlanningWeeklyHours());
        assertEquals(16, aluno.getRemainingWeeklyHours());
    }
    
    @Test
    @DisplayName("Remover disciplina do planejamento futuro")
    void testRemoverDoPlanejamentoFuturo() throws CargaHorariaExcedidaException {
        aluno.addToFuturePlanning(disciplina1);
        aluno.addToFuturePlanning(disciplina2);
        
        assertEquals(8, aluno.getFuturePlanningWeeklyHours());
        
        aluno.removeFromFuturePlanning(disciplina1);
        
        assertFalse(aluno.getFuturePlanning().contains(disciplina1));
        assertTrue(aluno.getFuturePlanning().contains(disciplina2));
        assertEquals(4, aluno.getFuturePlanningWeeklyHours());
    }
    
    @Test
    @DisplayName("Verificar limite de carga horária")
    void testLimiteCargaHoraria() throws CargaHorariaExcedidaException {
        // Adicionar disciplinas até o limite
        aluno.addToFuturePlanning(disciplina1); // 4h
        aluno.addToFuturePlanning(disciplina2); // 4h
        
        assertTrue(aluno.isFuturePlanningWithinLimit()); // 8h <= 20h
        
        // Adicionar mais disciplinas até o limite
        Subject disciplina3 = new RequiredSubject("FIS073", "Física I", 4);
        Subject disciplina4 = new RequiredSubject("DCC197", "VISÃO COMPUTACIONAL", 4);
        Subject disciplina5 = new RequiredSubject("DCC120", "Lab Programação", 4);
        
        aluno.addToFuturePlanning(disciplina3); // 12h
        aluno.addToFuturePlanning(disciplina4); // 16h
        aluno.addToFuturePlanning(disciplina5); // 20h
        
        assertTrue(aluno.isFuturePlanningWithinLimit()); // 20h = 20h
        assertEquals(0, aluno.getRemainingWeeklyHours());
        
        // Tentar adicionar mais uma disciplina deve lançar exceção
        Subject disciplina6 = new RequiredSubject("DCC025", "Orientação a Objetos", 4);
        assertThrows(CargaHorariaExcedidaException.class, () -> {
            aluno.addToFuturePlanning(disciplina6); // 24h > 20h
        }, "Deve lançar exceção quando carga horária é excedida");
    }
    
    @Test
    @DisplayName("Verificar disciplina não cursada")
    void testDisciplinaNaoCursada() {
        assertFalse(aluno.hasCompletedSubject(disciplina1));
        assertNull(aluno.getGrade(disciplina1));
    }
} 