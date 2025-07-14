package com.simulador.model.exceptions;

import com.simulador.model.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para CargaHorariaExcedidaException
 */
@DisplayName("Testes de CargaHorariaExcedidaException")
public class CargaHorariaExcedidaExceptionTest {
    
    private Student aluno;
    private Subject disciplina1, disciplina2, disciplina3, disciplina4;
    
    @BeforeEach
    void setUp() {
        aluno = new Student("João Silva", "202365082A", 8); // Limite de 8 horas
        disciplina1 = new RequiredSubject("MAT154", "Cálculo I", 4);
        disciplina2 = new RequiredSubject("MAT155", "Geometria Analítica", 4);
        disciplina3 = new ElectiveSubject("DCC119", "Algoritmos", 4);
        disciplina4 = new OptionalSubject("DCC100", "Seminários", 2);
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando carga horária é excedida")
    void testExcecaoCargaHorariaExcedida() throws CargaHorariaExcedidaException {
        // ARRANGE: Adiciona disciplinas até o limite
        aluno.addToFuturePlanning(disciplina1); // 4h
        aluno.addToFuturePlanning(disciplina2); // 4h (total: 8h)
        
        // ACT & ASSERT: Deve lançar exceção ao tentar adicionar mais uma disciplina
        assertThrows(CargaHorariaExcedidaException.class, () -> {
            aluno.addToFuturePlanning(disciplina3);
        }, "Deve lançar exceção quando carga horária é excedida");
    }
    
    @Test
    @DisplayName("Não deve lançar exceção quando carga horária está dentro do limite")
    void testNaoExcecaoCargaHorariaDentroLimite() throws CargaHorariaExcedidaException {
        // ARRANGE: Adiciona disciplinas dentro do limite
        aluno.addToFuturePlanning(disciplina1); // 4h
        aluno.addToFuturePlanning(disciplina4); // 2h (total: 6h)
        
        // ACT & ASSERT: Não deve lançar exceção
        assertDoesNotThrow(() -> {
            aluno.addToFuturePlanning(disciplina4); // +2h (total: 8h)
        }, "Não deve lançar exceção quando carga horária está dentro do limite");
    }
    
    @Test
    @DisplayName("Não deve lançar exceção quando carga horária é exatamente o limite")
    void testNaoExcecaoCargaHorariaExata() throws CargaHorariaExcedidaException {
        // ARRANGE: Adiciona disciplinas até exatamente o limite
        aluno.addToFuturePlanning(disciplina1); // 4h
        aluno.addToFuturePlanning(disciplina2); // 4h (total: 8h)
        
        // ACT & ASSERT: Não deve lançar exceção
        assertDoesNotThrow(() -> {
            // Não adiciona mais nada, apenas verifica que não lança exceção
        }, "Não deve lançar exceção quando carga horária é exatamente o limite");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando uma disciplina excede o limite sozinha")
    void testExcecaoDisciplinaExcedeSozinha() {
        // ARRANGE: Cria disciplina que excede o limite
        Subject disciplinaGrande = new RequiredSubject("DCC999", "Disciplina Grande", 10);
        
        // ACT & ASSERT: Deve lançar exceção
        assertThrows(CargaHorariaExcedidaException.class, () -> {
            aluno.addToFuturePlanning(disciplinaGrande);
        }, "Deve lançar exceção quando uma disciplina excede o limite sozinha");
    }
    
    @Test
    @DisplayName("Deve permitir adicionar disciplina após remover outra")
    void testAdicionarAposRemover() throws CargaHorariaExcedidaException {
        // ARRANGE: Adiciona disciplinas até o limite
        aluno.addToFuturePlanning(disciplina1); // 4h
        aluno.addToFuturePlanning(disciplina2); // 4h (total: 8h)
        
        // Remove uma disciplina
        aluno.removeFromFuturePlanning(disciplina1); // -4h (total: 4h)
        
        // ACT & ASSERT: Deve permitir adicionar nova disciplina
        assertDoesNotThrow(() -> {
            aluno.addToFuturePlanning(disciplina3); // +4h (total: 8h)
        }, "Deve permitir adicionar disciplina após remover outra");
    }
    
    @Test
    @DisplayName("Deve calcular corretamente horas restantes")
    void testHorasRestantes() throws CargaHorariaExcedidaException {
        // ARRANGE: Aluno sem disciplinas
        assertEquals(8, aluno.getRemainingWeeklyHours(), "Deve ter 8 horas restantes");
        
        // Adiciona uma disciplina
        aluno.addToFuturePlanning(disciplina1); // 4h
        assertEquals(4, aluno.getRemainingWeeklyHours(), "Deve ter 4 horas restantes");
        
        // Adiciona outra disciplina
        aluno.addToFuturePlanning(disciplina4); // 2h
        assertEquals(2, aluno.getRemainingWeeklyHours(), "Deve ter 2 horas restantes");
    }
    
    @Test
    @DisplayName("Deve verificar se planejamento está dentro do limite")
    void testPlanejamentoDentroLimite() throws CargaHorariaExcedidaException {
        // ARRANGE: Aluno sem disciplinas
        assertTrue(aluno.isFuturePlanningWithinLimit(), "Planejamento deve estar dentro do limite");
        
        // Adiciona disciplinas dentro do limite
        aluno.addToFuturePlanning(disciplina1); // 4h
        assertTrue(aluno.isFuturePlanningWithinLimit(), "Planejamento deve estar dentro do limite");
        
        aluno.addToFuturePlanning(disciplina4); // 2h
        assertTrue(aluno.isFuturePlanningWithinLimit(), "Planejamento deve estar dentro do limite");
    }
    
    @Test
    @DisplayName("Deve calcular corretamente carga horária planejada")
    void testCargaHorariaPlanejada() throws CargaHorariaExcedidaException {
        // ARRANGE: Aluno sem disciplinas
        assertEquals(0, aluno.getFuturePlanningWeeklyHours(), "Carga horária deve ser 0");
        
        // Adiciona disciplinas
        aluno.addToFuturePlanning(disciplina1); // 4h
        assertEquals(4, aluno.getFuturePlanningWeeklyHours(), "Carga horária deve ser 4h");
        
        aluno.addToFuturePlanning(disciplina4); // 2h
        assertEquals(6, aluno.getFuturePlanningWeeklyHours(), "Carga horária deve ser 6h");
    }
    
    @Test
    @DisplayName("Deve lançar exceção com múltiplas disciplinas pequenas")
    void testExcecaoMultiplasDisciplinasPequenas() throws CargaHorariaExcedidaException {
        // ARRANGE: Adiciona disciplinas pequenas até exceder
        Subject disciplinaPequena1 = new OptionalSubject("DCC100", "Seminários", 2);
        Subject disciplinaPequena2 = new OptionalSubject("DCC100", "Seminários", 2);
        Subject disciplinaPequena3 = new OptionalSubject("DCC101", "Introdução à Programação", 2);
        Subject disciplinaPequena4 = new OptionalSubject("DCC102", "Metodologia", 2);
        
        aluno.addToFuturePlanning(disciplinaPequena1); // 2h
        aluno.addToFuturePlanning(disciplinaPequena2); // 2h
        aluno.addToFuturePlanning(disciplinaPequena3); // 2h
        aluno.addToFuturePlanning(disciplinaPequena4); // 2h (total: 8h)
        
        // ACT & ASSERT: Deve lançar exceção ao tentar adicionar mais uma
        assertThrows(CargaHorariaExcedidaException.class, () -> {
            Subject disciplinaPequena5 = new OptionalSubject("DCC103", "Tópicos Especiais", 2);
            aluno.addToFuturePlanning(disciplinaPequena5); // +2h (total: 10h)
        }, "Deve lançar exceção com múltiplas disciplinas pequenas");
    }
} 