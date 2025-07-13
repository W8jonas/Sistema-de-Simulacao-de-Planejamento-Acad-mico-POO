package com.simulador.model.exceptions;

import com.simulador.model.domain.Student;
import com.simulador.model.domain.RequiredSubject;
import com.simulador.model.domain.Subject;
import com.simulador.model.exceptions.CargaHorariaExcedidaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para CoRequisitoNaoAtendidoException
 */
@DisplayName("Testes de CoRequisitoNaoAtendidoException")
public class CoRequisitoNaoAtendidoExceptionTest {
    
    private Student aluno;
    private Subject disciplinaPrincipal;
    private Subject coRequisito;
    
    @BeforeEach
    void setUp() {
        aluno = new Student("João Silva", "202365082A", 20);
        disciplinaPrincipal = new RequiredSubject("MAT156", "Cálculo II", 4);
        coRequisito = new RequiredSubject("MAT155", "Geometria Analítica", 4);
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando co-requisito não está no planejamento")
    void testExcecaoCoRequisitoNaoNoPlanejamento() {
        // ARRANGE: Define co-requisito mas não adiciona ao planejamento
        aluno.setCoRequisito(disciplinaPrincipal, coRequisito);
        
        // ACT & ASSERT: Deve lançar exceção
        assertThrows(CoRequisitoNaoAtendidoException.class, () -> {
            aluno.addToFuturePlanningWithCoRequisito(disciplinaPrincipal);
        }, "Deve lançar exceção quando co-requisito não está no planejamento");
    }
    
    @Test
    @DisplayName("Não deve lançar exceção quando co-requisito está no planejamento")
    void testNaoExcecaoCoRequisitoNoPlanejamento() throws CargaHorariaExcedidaException {
        // ARRANGE: Define co-requisito e adiciona ao planejamento
        aluno.setCoRequisito(disciplinaPrincipal, coRequisito);
        aluno.addToFuturePlanning(coRequisito);
        
        // ACT & ASSERT: Não deve lançar exceção
        assertDoesNotThrow(() -> {
            try {
                aluno.addToFuturePlanningWithCoRequisito(disciplinaPrincipal);
            } catch (CargaHorariaExcedidaException e) {
                // Ignora exceção de carga horária para este teste
            }
        }, "Não deve lançar exceção quando co-requisito está no planejamento");
    }
    
    @Test
    @DisplayName("Não deve lançar exceção quando co-requisito foi cursado")
    void testNaoExcecaoCoRequisitoCursado() throws CargaHorariaExcedidaException {
        // ARRANGE: Define co-requisito e adiciona ao histórico
        aluno.setCoRequisito(disciplinaPrincipal, coRequisito);
        aluno.addCompletedSubject(coRequisito, 8.0);
        
        // ACT & ASSERT: Não deve lançar exceção
        assertDoesNotThrow(() -> {
            try {
                aluno.addToFuturePlanningWithCoRequisito(disciplinaPrincipal);
            } catch (CargaHorariaExcedidaException e) {
                // Ignora exceção de carga horária para este teste
            }
        }, "Não deve lançar exceção quando co-requisito foi cursado");
    }
    
    @Test
    @DisplayName("Não deve lançar exceção quando não há co-requisito")
    void testNaoExcecaoSemCoRequisito() throws CargaHorariaExcedidaException {
        // ARRANGE: Não define co-requisito
        
        // ACT & ASSERT: Não deve lançar exceção
        assertDoesNotThrow(() -> {
            try {
                aluno.addToFuturePlanningWithCoRequisito(disciplinaPrincipal);
            } catch (CargaHorariaExcedidaException e) {
                // Ignora exceção de carga horária para este teste
            }
        }, "Não deve lançar exceção quando não há co-requisito");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando co-requisito é removido do planejamento")
    void testExcecaoCoRequisitoRemovido() throws CargaHorariaExcedidaException {
        // ARRANGE: Define co-requisito, adiciona e depois remove
        aluno.setCoRequisito(disciplinaPrincipal, coRequisito);
        aluno.addToFuturePlanning(coRequisito);
        aluno.removeFromFuturePlanning(coRequisito);
        
        // ACT & ASSERT: Deve lançar exceção
        assertThrows(CoRequisitoNaoAtendidoException.class, () -> {
            try {
                aluno.addToFuturePlanningWithCoRequisito(disciplinaPrincipal);
            } catch (CargaHorariaExcedidaException e) {
                // Ignora exceção de carga horária para este teste
            }
        }, "Deve lançar exceção quando co-requisito é removido do planejamento");
    }
    
    @Test
    @DisplayName("Deve lançar exceção com múltiplos co-requisitos não atendidos")
    void testExcecaoMultiplosCoRequisitos() {
        // ARRANGE: Define múltiplos co-requisitos
        Subject disciplina2 = new RequiredSubject("FIS073", "Física I", 4);
        Subject coRequisito2 = new RequiredSubject("FIS074", "Física II", 4);
        
        aluno.setCoRequisito(disciplinaPrincipal, coRequisito);
        aluno.setCoRequisito(disciplina2, coRequisito2);
        
        // ACT & ASSERT: Deve lançar exceção para ambas
        assertThrows(CoRequisitoNaoAtendidoException.class, () -> {
            aluno.addToFuturePlanningWithCoRequisito(disciplinaPrincipal);
        }, "Deve lançar exceção para primeira disciplina");
        
        assertThrows(CoRequisitoNaoAtendidoException.class, () -> {
            aluno.addToFuturePlanningWithCoRequisito(disciplina2);
        }, "Deve lançar exceção para segunda disciplina");
    }
} 