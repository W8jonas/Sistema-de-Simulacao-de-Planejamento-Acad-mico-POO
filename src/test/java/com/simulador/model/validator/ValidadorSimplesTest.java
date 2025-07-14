package com.simulador.model.validator;

import com.simulador.model.domain.Student;
import com.simulador.model.domain.RequiredSubject;
import com.simulador.model.domain.ElectiveSubject;
import com.simulador.model.domain.OptionalSubject;
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
        Subject disciplinaSemPreRequisitos = new RequiredSubject("DCC197", "VISÃO COMPUTACIONAL", 4);
        
        // ACT: Validar
        boolean resultado = validador.validar(aluno, disciplinaSemPreRequisitos);
        
        // ASSERT: Deve retornar false (não tem o pré-requisito)
        assertFalse(resultado, "Disciplina sem pré-requisitos não deve ser validada por este validador");
    }
    
    @Test
    @DisplayName("Validar com nota exatamente na média (6.0)")
    void testValidarComNotaExata() {
        // ARRANGE: Aluno cursou com nota exatamente 6.0
        aluno.addCompletedSubject(disciplinaPreRequisito, 6.0);
        
        // ACT: Validar
        boolean resultado = validador.validar(aluno, disciplinaAlvo);
        
        // ASSERT: Deve retornar true (6.0 é aprovado)
        assertTrue(resultado, "Aluno deve ser elegível com nota 6.0");
    }
    
    @Test
    @DisplayName("Validar com nota 5.9 (reprovado)")
    void testValidarComNotaQuaseAprovado() {
        // ARRANGE: Aluno cursou com nota 5.9 (reprovado)
        aluno.addCompletedSubject(disciplinaPreRequisito, 5.9);
        
        // ACT: Validar
        boolean resultado = validador.validar(aluno, disciplinaAlvo);
        
        // ASSERT: Deve retornar false (reprovado)
        assertFalse(resultado, "Aluno não deve ser elegível com nota 5.9");
    }
    
    @Test
    @DisplayName("Validar com nota máxima (10.0)")
    void testValidarComNotaMaxima() {
        // ARRANGE: Aluno cursou com nota máxima
        aluno.addCompletedSubject(disciplinaPreRequisito, 10.0);
        
        // ACT: Validar
        boolean resultado = validador.validar(aluno, disciplinaAlvo);
        
        // ASSERT: Deve retornar true
        assertTrue(resultado, "Aluno deve ser elegível com nota 10.0");
    }
    
    @Test
    @DisplayName("Validar com nota zero (reprovado)")
    void testValidarComNotaZero() {
        // ARRANGE: Aluno cursou com nota zero
        aluno.addCompletedSubject(disciplinaPreRequisito, 0.0);
        
        // ACT: Validar
        boolean resultado = validador.validar(aluno, disciplinaAlvo);
        
        // ASSERT: Deve retornar false (reprovado)
        assertFalse(resultado, "Aluno não deve ser elegível com nota 0.0");
    }
    
    @Test
    @DisplayName("Validar com disciplina eletiva como pré-requisito")
    void testValidarComDisciplinaEletiva() {
        // ARRANGE: Pré-requisito é uma disciplina eletiva
        Subject disciplinaEletiva = new ElectiveSubject("DCC999", "Tópicos Especiais", 4);
        ValidadorSimples validadorEletiva = new ValidadorSimples(disciplinaEletiva);
        aluno.addCompletedSubject(disciplinaEletiva, 8.0);
        
        // ACT: Validar
        boolean resultado = validadorEletiva.validar(aluno, disciplinaAlvo);
        
        // ASSERT: Deve retornar true
        assertTrue(resultado, "Aluno deve ser elegível quando cursou disciplina eletiva como pré-requisito");
    }
    
    @Test
    @DisplayName("Validar com disciplina opcional como pré-requisito")
    void testValidarComDisciplinaOpcional() {
        // ARRANGE: Pré-requisito é uma disciplina opcional
        Subject disciplinaOpcional = new OptionalSubject("D133", "introducao a sistemas de informação", 2);
        ValidadorSimples validadorOpcional = new ValidadorSimples(disciplinaOpcional);
        aluno.addCompletedSubject(disciplinaOpcional, 7.5);
        
        // ACT: Validar
        boolean resultado = validadorOpcional.validar(aluno, disciplinaAlvo);
        
        // ASSERT: Deve retornar true
        assertTrue(resultado, "Aluno deve ser elegível quando cursou disciplina opcional como pré-requisito");
    }
    
    @Test
    @DisplayName("Validar com múltiplas disciplinas no histórico")
    void testValidarComMultiplasDisciplinas() {
        // ARRANGE: Aluno tem várias disciplinas no histórico, incluindo o pré-requisito
        Subject outraDisciplina = new RequiredSubject("DCC197", "VISÃO COMPUTACIONAL", 4);
        aluno.addCompletedSubject(outraDisciplina, 9.0);
        aluno.addCompletedSubject(disciplinaPreRequisito, 7.5);
        
        // ACT: Validar
        boolean resultado = validador.validar(aluno, disciplinaAlvo);
        
        // ASSERT: Deve retornar true
        assertTrue(resultado, "Aluno deve ser elegível mesmo com múltiplas disciplinas no histórico");
    }
    
    @Test
    @DisplayName("Validar com aluno que não cursou nenhuma disciplina")
    void testValidarComAlunoSemHistorico() {
        // ARRANGE: Aluno sem histórico de disciplinas
        Student alunoNovo = new Student("Maria Santos", "202365083B", 20);
        
        // ACT: Validar
        boolean resultado = validador.validar(alunoNovo, disciplinaAlvo);
        
        // ASSERT: Deve retornar false
        assertFalse(resultado, "Aluno sem histórico não deve ser elegível");
    }
    
    @Test
    @DisplayName("Validar com nota negativa (caso extremo)")
    void testValidarComNotaNegativa() {
        // ARRANGE: Aluno cursou com nota negativa (caso extremo)
        aluno.addCompletedSubject(disciplinaPreRequisito, -1.0);
        
        // ACT: Validar
        boolean resultado = validador.validar(aluno, disciplinaAlvo);
        
        // ASSERT: Deve retornar false (nota negativa é menor que 6.0)
        assertFalse(resultado, "Aluno não deve ser elegível com nota negativa");
    }
    
    @Test
    @DisplayName("Validar com nota muito alta (caso extremo)")
    void testValidarComNotaMuitoAlta() {
        // ARRANGE: Aluno cursou com nota muito alta
        aluno.addCompletedSubject(disciplinaPreRequisito, 15.0);
        
        // ACT: Validar
        boolean resultado = validador.validar(aluno, disciplinaAlvo);
        
        // ASSERT: Deve retornar true (nota alta é maior que 6.0)
        assertTrue(resultado, "Aluno deve ser elegível com nota muito alta");
    }
    
    @Test
    @DisplayName("Validar com diferentes tipos de disciplinas alvo")
    void testValidarComDiferentesTiposDisciplinaAlvo() {
        // ARRANGE: Aluno cursou o pré-requisito
        aluno.addCompletedSubject(disciplinaPreRequisito, 8.0);
        
        // ACT & ASSERT: Validar com diferentes tipos de disciplinas alvo
        Subject disciplinaObrigatoria = new RequiredSubject("DCC001", "Programação", 4);
        Subject disciplinaEletiva = new ElectiveSubject("DCC002", "Tópicos Avançados", 4);
        Subject disciplinaOpcional = new OptionalSubject("DCC003", "Seminário", 2);
        
        assertTrue(validador.validar(aluno, disciplinaObrigatoria), 
                  "Deve ser elegível para disciplina obrigatória");
        assertTrue(validador.validar(aluno, disciplinaEletiva), 
                  "Deve ser elegível para disciplina eletiva");
        assertTrue(validador.validar(aluno, disciplinaOpcional), 
                  "Deve ser elegível para disciplina opcional");
    }
    
    @Test
    @DisplayName("Validar com nota decimal precisa")
    void testValidarComNotaDecimal() {
        // ARRANGE: Aluno cursou com nota decimal
        aluno.addCompletedSubject(disciplinaPreRequisito, 6.75);
        
        // ACT: Validar
        boolean resultado = validador.validar(aluno, disciplinaAlvo);
        
        // ASSERT: Deve retornar true
        assertTrue(resultado, "Aluno deve ser elegível com nota decimal 6.75");
    }
    
    @Test
    @DisplayName("Validar com nota decimal reprovada")
    void testValidarComNotaDecimalReprovada() {
        // ARRANGE: Aluno cursou com nota decimal reprovada
        aluno.addCompletedSubject(disciplinaPreRequisito, 5.99);
        
        // ACT: Validar
        boolean resultado = validador.validar(aluno, disciplinaAlvo);
        
        // ASSERT: Deve retornar false
        assertFalse(resultado, "Aluno não deve ser elegível com nota decimal 5.99");
    }
    
    @Test
    @DisplayName("Validar com nota decimal aprovada")
    void testValidarComNotaDecimalAprovada() {
        // ARRANGE: Aluno cursou com nota decimal aprovada
        aluno.addCompletedSubject(disciplinaPreRequisito, 6.01);
        
        // ACT: Validar
        boolean resultado = validador.validar(aluno, disciplinaAlvo);
        
        // ASSERT: Deve retornar true
        assertTrue(resultado, "Aluno deve ser elegível com nota decimal 6.01");
    }
} 