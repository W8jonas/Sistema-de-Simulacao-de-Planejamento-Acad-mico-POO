package com.simulador.services;

import com.simulador.model.domain.*;
import com.simulador.model.exceptions.ConflitoDeHorarioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

/**
 * Testes para ConflictResolutionService
 */
@DisplayName("Testes de ConflictResolutionService")
public class ConflictResolutionServiceTest {
    
    private ConflictResolutionService service;
    private ClassGroup turmaObrigatoria, turmaEletiva, turmaOptativa;
    private ClassGroup turmaObrigatoria2, turmaEletiva2, turmaOptativa2;
    
    @BeforeEach
    void setUp() {
        service = new ConflictResolutionService();
        
        // Criar disciplinas
        Subject disciplinaObrigatoria = new RequiredSubject("MAT154", "Cálculo I", 4);
        Subject disciplinaEletiva = new ElectiveSubject("DCC197", "VISÃO COMPUTACIONAL", 4);
        Subject disciplinaOptativa = new OptionalSubject("D133", "introducao a sistemas de informação", 2);
        Subject disciplinaObrigatoria2 = new RequiredSubject("MAT155", "Geometria Analítica", 4);
        Subject disciplinaEletiva2 = new ElectiveSubject("DCC120", "Laboratório de Programação", 4);
        Subject disciplinaOptativa2 = new OptionalSubject("DCC101", "Introdução à Programação", 2);
        
        // Criar turmas com horários conflitantes
        Schedule horario = new Schedule(1, 8, 10); // Segunda-feira, 8h às 10h
        turmaObrigatoria = new ClassGroup("MAT154-01", disciplinaObrigatoria, 30, Arrays.asList(horario));
        turmaEletiva = new ClassGroup("DCC197-01", disciplinaEletiva, 25, Arrays.asList(horario));
        turmaOptativa = new ClassGroup("D133-01", disciplinaOptativa, 20, Arrays.asList(horario));
        turmaObrigatoria2 = new ClassGroup("MAT155-01", disciplinaObrigatoria2, 30, Arrays.asList(horario));
        turmaEletiva2 = new ClassGroup("DCC120-01", disciplinaEletiva2, 25, Arrays.asList(horario));
        turmaOptativa2 = new ClassGroup("DCC101-01", disciplinaOptativa2, 20, Arrays.asList(horario));
    }
    
    @Test
    @DisplayName("Deve resolver conflito com obrigatória prevalecendo sobre eletiva")
    void testObrigatoriaPrevalecendoSobreEletiva() throws ConflitoDeHorarioException {
        // ARRANGE: Conflito entre obrigatória e eletiva
        List<ClassGroup> turmasConflitantes = Arrays.asList(turmaObrigatoria, turmaEletiva);
        
        // ACT: Resolve conflito
        List<ClassGroup> resultado = service.resolveConflicts(turmasConflitantes);
        
        // ASSERT: Obrigatória deve vir primeiro
        assertEquals(1, resultado.size(), "Deve ter 1 turma aceita");
        assertEquals(turmaObrigatoria, resultado.get(0), "Obrigatória deve ser aceita");
    }
    
    @Test
    @DisplayName("Deve resolver conflito com eletiva prevalecendo sobre optativa")
    void testEletivaPrevalecendoSobreOptativa() throws ConflitoDeHorarioException {
        // ARRANGE: Conflito entre eletiva e optativa
        List<ClassGroup> turmasConflitantes = Arrays.asList(turmaEletiva, turmaOptativa);
        
        // ACT: Resolve conflito
        List<ClassGroup> resultado = service.resolveConflicts(turmasConflitantes);
        
        // ASSERT: Eletiva deve ser aceita
        assertEquals(1, resultado.size(), "Deve ter 1 turma aceita");
        assertEquals(turmaEletiva, resultado.get(0), "Eletiva deve ser aceita");
    }
    
    @Test
    @DisplayName("Deve resolver conflito com obrigatória prevalecendo sobre optativa")
    void testObrigatoriaPrevalecendoSobreOptativa() throws ConflitoDeHorarioException {
        // ARRANGE: Conflito entre obrigatória e optativa
        List<ClassGroup> turmasConflitantes = Arrays.asList(turmaObrigatoria, turmaOptativa);
        
        // ACT: Resolve conflito
        List<ClassGroup> resultado = service.resolveConflicts(turmasConflitantes);
        
        // ASSERT: Obrigatória deve ser aceita
        assertEquals(1, resultado.size(), "Deve ter 1 turma aceita");
        assertEquals(turmaObrigatoria, resultado.get(0), "Obrigatória deve ser aceita");
    }
    
    @Test
    @DisplayName("Deve resolver conflito com múltiplas disciplinas")
    void testConflitoMultiplasDisciplinas() throws ConflitoDeHorarioException {
        // ARRANGE: Conflito entre múltiplas disciplinas
        List<ClassGroup> turmasConflitantes = Arrays.asList(turmaObrigatoria, turmaEletiva, turmaOptativa);
        
        // ACT: Resolve conflito
        List<ClassGroup> resultado = service.resolveConflicts(turmasConflitantes);
        
        // ASSERT: Deve aceitar apenas a obrigatória
        assertEquals(1, resultado.size(), "Deve ter 1 turma aceita");
        assertEquals(turmaObrigatoria, resultado.get(0), "Obrigatória deve ser aceita");
    }
    
    @Test
    @DisplayName("Deve lançar exceção para conflito entre disciplinas de mesma precedência")
    void testConflitoMesmaPrecedencia() {
        // ARRANGE: Conflito entre duas obrigatórias
        List<ClassGroup> turmasConflitantes = Arrays.asList(turmaObrigatoria, turmaObrigatoria2);
        
        // ACT & ASSERT: Deve lançar exceção
        assertThrows(com.simulador.model.exceptions.ConflitoDeHorarioException.class, 
                    () -> service.resolveConflicts(turmasConflitantes),
                    "Deve lançar exceção para conflito entre disciplinas de mesma precedência");
    }
    
    @Test
    @DisplayName("Deve aceitar turmas sem conflito")
    void testTurmasSemConflito() throws ConflitoDeHorarioException {
        // ARRANGE: Turmas sem conflito (horários diferentes)
        Schedule horario1 = new Schedule(1, 8, 10);   // Segunda 8-10h
        Schedule horario2 = new Schedule(2, 8, 10);   // Terça 8-10h
        
        Subject disciplina1 = new RequiredSubject("MAT154", "Cálculo I", 4);
        Subject disciplina2 = new RequiredSubject("MAT155", "Geometria Analítica", 4);
        
        ClassGroup turma1 = new ClassGroup("MAT154-01", disciplina1, 30, Arrays.asList(horario1));
        ClassGroup turma2 = new ClassGroup("MAT155-01", disciplina2, 30, Arrays.asList(horario2));
        
        List<ClassGroup> turmas = Arrays.asList(turma1, turma2);
        
        // ACT: Resolve conflito
        List<ClassGroup> resultado = service.resolveConflicts(turmas);
        
        // ASSERT: Deve aceitar ambas as turmas
        assertEquals(2, resultado.size(), "Deve aceitar ambas as turmas");
        assertTrue(resultado.contains(turma1), "Deve conter primeira turma");
        assertTrue(resultado.contains(turma2), "Deve conter segunda turma");
    }
    
    @Test
    @DisplayName("Deve retornar lista vazia para lista vazia")
    void testListaVazia() throws ConflitoDeHorarioException {
        // ARRANGE: Lista vazia
        List<ClassGroup> turmas = new ArrayList<>();
        
        // ACT: Resolve conflito
        List<ClassGroup> resultado = service.resolveConflicts(turmas);
        
        // ASSERT: Deve retornar lista vazia
        assertTrue(resultado.isEmpty(), "Deve retornar lista vazia");
    }
    
    @Test
    @DisplayName("Deve retornar lista vazia para null")
    void testListaNull() throws ConflitoDeHorarioException {
        // ACT: Resolve conflito
        List<ClassGroup> resultado = service.resolveConflicts(null);
        
        // ASSERT: Deve retornar lista vazia
        assertTrue(resultado.isEmpty(), "Deve retornar lista vazia");
    }
} 