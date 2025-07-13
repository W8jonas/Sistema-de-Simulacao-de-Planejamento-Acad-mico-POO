package com.simulador.model.validator;

import com.simulador.model.domain.Student;
import com.simulador.model.domain.RequiredSubject;
import com.simulador.model.domain.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * Testes unitários para o ValidadorLogicoAND
 */
@DisplayName("Testes do ValidadorLogicoAND")
public class ValidadorLogicoANDTest {
    
    private Student aluno;
    private Subject disciplina1;
    private Subject disciplina2;
    private Subject disciplina3;
    private Subject disciplinaAlvo;
    private ValidadorLogicoAND validador;
    
    @BeforeEach
    void setUp() {
        aluno = new Student("João Silva", "202365082A", 20);
        disciplina1 = new RequiredSubject("MAT154", "Cálculo I", 4);
        disciplina2 = new RequiredSubject("MAT155", "Geometria Analítica", 4);
        disciplina3 = new RequiredSubject("FIS073", "Física I", 4);
        disciplinaAlvo = new RequiredSubject("MAT156", "Cálculo II", 4);
        
        // Criar validador que requer disciplina1 E disciplina2
        List<ValidadorPreRequisito> validadores = Arrays.asList(
            new ValidadorSimples(disciplina1),
            new ValidadorSimples(disciplina2)
        );
        validador = new ValidadorLogicoAND(validadores);
    }
    
    @Test
    @DisplayName("Validar quando TODOS os pré-requisitos foram cursados")
    void testValidarComTodosPreRequisitosCursados() {
        // ARRANGE: Aluno cursou ambas as disciplinas pré-requisito
        aluno.addCompletedSubject(disciplina1, 8.5);
        aluno.addCompletedSubject(disciplina2, 7.5);
        
        // ACT: Validar se pode cursar a disciplina alvo
        boolean resultado = validador.validar(aluno, disciplinaAlvo);
        
        // ASSERT: Deve retornar true
        assertTrue(resultado, "Aluno deve ser elegível quando cursou todos os pré-requisitos");
    }
    
    @Test
    @DisplayName("Validar quando apenas UM pré-requisito foi cursado")
    void testValidarComApenasUmPreRequisitoCursado() {
        // ARRANGE: Aluno cursou apenas uma das disciplinas pré-requisito
        aluno.addCompletedSubject(disciplina1, 8.5);
        // Não cursou disciplina2
        
        // ACT: Validar se pode cursar a disciplina alvo
        boolean resultado = validador.validar(aluno, disciplinaAlvo);
        
        // ASSERT: Deve retornar false
        assertFalse(resultado, "Aluno não deve ser elegível quando cursou apenas um dos pré-requisitos");
    }
    
    @Test
    @DisplayName("Validar quando NENHUM pré-requisito foi cursado")
    void testValidarSemNenhumPreRequisitoCursado() {
        // ARRANGE: Aluno não cursou nenhuma das disciplinas pré-requisito
        // (não adiciona nada ao histórico)
        
        // ACT: Validar se pode cursar a disciplina alvo
        boolean resultado = validador.validar(aluno, disciplinaAlvo);
        
        // ASSERT: Deve retornar false
        assertFalse(resultado, "Aluno não deve ser elegível quando não cursou nenhum pré-requisito");
    }
    
    @Test
    @DisplayName("Validar quando um pré-requisito foi reprovado")
    void testValidarComUmPreRequisitoReprovado() {
        // ARRANGE: Aluno cursou uma disciplina mas foi reprovado na outra
        aluno.addCompletedSubject(disciplina1, 8.5);
        aluno.addCompletedSubject(disciplina2, 3.0); // Reprovado
        
        // ACT: Validar se pode cursar a disciplina alvo
        boolean resultado = validador.validar(aluno, disciplinaAlvo);
        
        // ASSERT: Deve retornar false
        assertFalse(resultado, "Aluno não deve ser elegível quando foi reprovado em um pré-requisito");
    }
    
    @Test
    @DisplayName("Validar com três pré-requisitos - todos cursados")
    void testValidarComTresPreRequisitosTodosCursados() {
        // ARRANGE: Criar validador com três pré-requisitos
        List<ValidadorPreRequisito> validadores = Arrays.asList(
            new ValidadorSimples(disciplina1),
            new ValidadorSimples(disciplina2),
            new ValidadorSimples(disciplina3)
        );
        ValidadorLogicoAND validadorTres = new ValidadorLogicoAND(validadores);
        
        // Aluno cursou todas as disciplinas
        aluno.addCompletedSubject(disciplina1, 8.5);
        aluno.addCompletedSubject(disciplina2, 7.5);
        aluno.addCompletedSubject(disciplina3, 9.0);
        
        // ACT: Validar
        boolean resultado = validadorTres.validar(aluno, disciplinaAlvo);
        
        // ASSERT: Deve retornar true
        assertTrue(resultado, "Aluno deve ser elegível quando cursou todos os três pré-requisitos");
    }
    
    @Test
    @DisplayName("Validar com três pré-requisitos - apenas dois cursados")
    void testValidarComTresPreRequisitosApenasDoisCursados() {
        // ARRANGE: Criar validador com três pré-requisitos
        List<ValidadorPreRequisito> validadores = Arrays.asList(
            new ValidadorSimples(disciplina1),
            new ValidadorSimples(disciplina2),
            new ValidadorSimples(disciplina3)
        );
        ValidadorLogicoAND validadorTres = new ValidadorLogicoAND(validadores);
        
        // Aluno cursou apenas duas disciplinas
        aluno.addCompletedSubject(disciplina1, 8.5);
        aluno.addCompletedSubject(disciplina2, 7.5);
        // Não cursou disciplina3
        
        // ACT: Validar
        boolean resultado = validadorTres.validar(aluno, disciplinaAlvo);
        
        // ASSERT: Deve retornar false
        assertFalse(resultado, "Aluno não deve ser elegível quando cursou apenas dois dos três pré-requisitos");
    }
} 