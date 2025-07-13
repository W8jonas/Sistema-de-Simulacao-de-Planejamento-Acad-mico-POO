package com.simulador.model.exceptions;

import com.simulador.model.domain.Student;
import com.simulador.model.domain.RequiredSubject;
import com.simulador.model.domain.Subject;
import com.simulador.model.validator.ValidadorSimples;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para PreRequisitoNaoCumpridoException
 */
@DisplayName("Testes de PreRequisitoNaoCumpridoException")
public class PreRequisitoNaoCumpridoExceptionTest {
    
    private Student aluno;
    private Subject disciplinaPreRequisito;
    private Subject disciplinaAlvo;
    private ValidadorSimples validador;
    
    @BeforeEach
    void setUp() {
        aluno = new Student("João Silva", "202365082A", 20);
        disciplinaPreRequisito = new RequiredSubject("MAT154", "Cálculo I", 4);
        disciplinaAlvo = new RequiredSubject("MAT156", "Cálculo II", 4);
        validador = new ValidadorSimples(disciplinaPreRequisito);
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando pré-requisito não foi cursado")
    void testExcecaoPreRequisitoNaoCursado() {
        // ARRANGE: Aluno não cursou a disciplina pré-requisito
        
        // ACT & ASSERT: Deve lançar exceção
        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            validador.validarComExcecao(aluno, disciplinaAlvo);
        }, "Deve lançar exceção quando pré-requisito não foi cursado");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando pré-requisito foi cursado mas reprovado")
    void testExcecaoPreRequisitoReprovado() {
        // ARRANGE: Aluno cursou mas foi reprovado
        aluno.addCompletedSubject(disciplinaPreRequisito, 5.0);
        
        // ACT & ASSERT: Deve lançar exceção
        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            validador.validarComExcecao(aluno, disciplinaAlvo);
        }, "Deve lançar exceção quando pré-requisito foi reprovado");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando pré-requisito foi cursado com nota 5.9")
    void testExcecaoPreRequisitoNotaQuaseAprovado() {
        // ARRANGE: Aluno cursou com nota 5.9
        aluno.addCompletedSubject(disciplinaPreRequisito, 5.9);
        
        // ACT & ASSERT: Deve lançar exceção
        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            validador.validarComExcecao(aluno, disciplinaAlvo);
        }, "Deve lançar exceção quando pré-requisito foi cursado com nota 5.9");
    }
    
    @Test
    @DisplayName("Não deve lançar exceção quando pré-requisito foi aprovado")
    void testNaoExcecaoPreRequisitoAprovado() {
        // ARRANGE: Aluno cursou e foi aprovado
        aluno.addCompletedSubject(disciplinaPreRequisito, 7.0);
        
        // ACT & ASSERT: Não deve lançar exceção
        assertDoesNotThrow(() -> {
            validador.validarComExcecao(aluno, disciplinaAlvo);
        }, "Não deve lançar exceção quando pré-requisito foi aprovado");
    }
    
    @Test
    @DisplayName("Não deve lançar exceção quando pré-requisito foi aprovado com nota 6.0")
    void testNaoExcecaoPreRequisitoNotaExata() {
        // ARRANGE: Aluno cursou com nota exatamente 6.0
        aluno.addCompletedSubject(disciplinaPreRequisito, 6.0);
        
        // ACT & ASSERT: Não deve lançar exceção
        assertDoesNotThrow(() -> {
            validador.validarComExcecao(aluno, disciplinaAlvo);
        }, "Não deve lançar exceção quando pré-requisito foi aprovado com nota 6.0");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando pré-requisito foi cursado com nota zero")
    void testExcecaoPreRequisitoNotaZero() {
        // ARRANGE: Aluno cursou com nota zero
        aluno.addCompletedSubject(disciplinaPreRequisito, 0.0);
        
        // ACT & ASSERT: Deve lançar exceção
        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            validador.validarComExcecao(aluno, disciplinaAlvo);
        }, "Deve lançar exceção quando pré-requisito foi cursado com nota zero");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando pré-requisito foi cursado com nota negativa")
    void testExcecaoPreRequisitoNotaNegativa() {
        // ARRANGE: Aluno cursou com nota negativa
        aluno.addCompletedSubject(disciplinaPreRequisito, -1.0);
        
        // ACT & ASSERT: Deve lançar exceção
        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            validador.validarComExcecao(aluno, disciplinaAlvo);
        }, "Deve lançar exceção quando pré-requisito foi cursado com nota negativa");
    }
} 