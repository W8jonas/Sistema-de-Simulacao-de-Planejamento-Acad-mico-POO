package com.simulador.model.validator;

import com.simulador.model.domain.Student;
import com.simulador.model.domain.RequiredSubject;
import com.simulador.model.domain.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o ValidadorSimples
 */
@DisplayName("Testes do ValidadorSimples")
public class ValidadorSimplesTest {
    
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
    @DisplayName("Validar quando pré-requisito foi cursado")
    void testValidarComPreRequisitoCursado() {
        // ARRANGE: Aluno cursou a disciplina pré-requisito
        aluno.addCompletedSubject(disciplinaPreRequisito, 8.5);
        
        // ACT: Validar se pode cursar a disciplina alvo
        boolean resultado = validador.validar(aluno, disciplinaAlvo);
        
        // ASSERT: Deve retornar true
        assertTrue(resultado, "Aluno deve ser elegível quando cursou o pré-requisito");
    }
    
    @Test
    @DisplayName("Validar quando pré-requisito não foi cursado")
    void testValidarSemPreRequisitoCursado() {
        // ARRANGE: Aluno não cursou a disciplina pré-requisito
        // (não adiciona nada ao histórico)
        
        // ACT: Validar se pode cursar a disciplina alvo
        boolean resultado = validador.validar(aluno, disciplinaAlvo);
        
        // ASSERT: Deve retornar false
        assertFalse(resultado, "Aluno não deve ser elegível quando não cursou o pré-requisito");
    }
    
    @Test
    @DisplayName("Validar com nota mínima (aprovado)")
    void testValidarComNotaMinima() {
        // ARRANGE: Aluno cursou com nota suficiente
        aluno.addCompletedSubject(disciplinaPreRequisito, 7.0);
        
        // ACT: Validar
        boolean resultado = validador.validar(aluno, disciplinaAlvo);
        
        // ASSERT: Deve retornar true
        assertTrue(resultado, "Aluno deve ser elegível com nota 7.0");
    }
    
    @Test
    @DisplayName("Validar com nota baixa (reprovado)")
    void testValidarComNotaBaixa() {
        // ARRANGE: Aluno cursou mas foi reprovado
        aluno.addCompletedSubject(disciplinaPreRequisito, 3.0);
        
        // ACT: Validar
        boolean resultado = validador.validar(aluno, disciplinaAlvo);
        
        // ASSERT: Deve retornar false (reprovado)
        assertFalse(resultado, "Aluno não deve ser elegível quando foi reprovado");
    }
    
    @Test
    @DisplayName("Validar disciplina sem pré-requisitos")
    void testValidarDisciplinaSemPreRequisitos() {
        // ARRANGE: Disciplina sem pré-requisitos
        Subject disciplinaSemPreRequisitos = new RequiredSubject("DCC119", "Algoritmos", 4);
        
        // ACT: Validar
        boolean resultado = validador.validar(aluno, disciplinaSemPreRequisitos);
        
        // ASSERT: Deve retornar false (não tem o pré-requisito)
        assertFalse(resultado, "Disciplina sem pré-requisitos não deve ser validada por este validador");
    }
} 