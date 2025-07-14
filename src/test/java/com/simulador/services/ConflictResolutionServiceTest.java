package com.simulador.services;

import com.simulador.model.domain.*;
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
    private Subject disciplinaObrigatoria, disciplinaEletiva, disciplinaOptativa;
    private Subject disciplinaObrigatoria2, disciplinaEletiva2, disciplinaOptativa2;
    
    @BeforeEach
    void setUp() {
        service = new ConflictResolutionService();
        disciplinaObrigatoria = new RequiredSubject("MAT154", "Cálculo I", 4);
        disciplinaEletiva = new ElectiveSubject("DCC119", "Algoritmos", 4);
        disciplinaOptativa = new OptionalSubject("DCC100", "Seminários", 2);
        disciplinaObrigatoria2 = new RequiredSubject("MAT155", "Geometria Analítica", 4);
        disciplinaEletiva2 = new ElectiveSubject("DCC120", "Laboratório de Programação", 4);
        disciplinaOptativa2 = new OptionalSubject("DCC101", "Introdução à Programação", 2);
    }
    
    @Test
    @DisplayName("Deve resolver conflito com obrigatória prevalecendo sobre eletiva")
    void testObrigatoriaPrevalecendoSobreEletiva() {
        // ARRANGE: Conflito entre obrigatória e eletiva
        Set<Subject> disciplinasConflitantes = new HashSet<>();
        disciplinasConflitantes.add(disciplinaObrigatoria);
        disciplinasConflitantes.add(disciplinaEletiva);
        
        // ACT: Resolve conflito
        List<Subject> resultado = service.resolveConflicts(disciplinasConflitantes);
        
        // ASSERT: Obrigatória deve vir primeiro
        assertEquals(2, resultado.size(), "Deve ter 2 disciplinas");
        assertEquals(disciplinaObrigatoria, resultado.get(0), "Obrigatória deve vir primeiro");
        assertEquals(disciplinaEletiva, resultado.get(1), "Eletiva deve vir segundo");
    }
    
    @Test
    @DisplayName("Deve resolver conflito com eletiva prevalecendo sobre optativa")
    void testEletivaPrevalecendoSobreOptativa() {
        // ARRANGE: Conflito entre eletiva e optativa
        Set<Subject> disciplinasConflitantes = new HashSet<>();
        disciplinasConflitantes.add(disciplinaEletiva);
        disciplinasConflitantes.add(disciplinaOptativa);
        
        // ACT: Resolve conflito
        List<Subject> resultado = service.resolveConflicts(disciplinasConflitantes);
        
        // ASSERT: Eletiva deve vir primeiro
        assertEquals(2, resultado.size(), "Deve ter 2 disciplinas");
        assertEquals(disciplinaEletiva, resultado.get(0), "Eletiva deve vir primeiro");
        assertEquals(disciplinaOptativa, resultado.get(1), "Optativa deve vir segundo");
    }
    
    @Test
    @DisplayName("Deve resolver conflito com obrigatória prevalecendo sobre optativa")
    void testObrigatoriaPrevalecendoSobreOptativa() {
        // ARRANGE: Conflito entre obrigatória e optativa
        Set<Subject> disciplinasConflitantes = new HashSet<>();
        disciplinasConflitantes.add(disciplinaObrigatoria);
        disciplinasConflitantes.add(disciplinaOptativa);
        
        // ACT: Resolve conflito
        List<Subject> resultado = service.resolveConflicts(disciplinasConflitantes);
        
        // ASSERT: Obrigatória deve vir primeiro
        assertEquals(2, resultado.size(), "Deve ter 2 disciplinas");
        assertEquals(disciplinaObrigatoria, resultado.get(0), "Obrigatória deve vir primeiro");
        assertEquals(disciplinaOptativa, resultado.get(1), "Optativa deve vir segundo");
    }
    
    @Test
    @DisplayName("Deve resolver conflito com múltiplas disciplinas")
    void testConflitoMultiplasDisciplinas() {
        // ARRANGE: Conflito entre múltiplas disciplinas
        Set<Subject> disciplinasConflitantes = new HashSet<>();
        disciplinasConflitantes.add(disciplinaObrigatoria);
        disciplinasConflitantes.add(disciplinaEletiva);
        disciplinasConflitantes.add(disciplinaOptativa);
        
        // ACT: Resolve conflito
        List<Subject> resultado = service.resolveConflicts(disciplinasConflitantes);
        
        // ASSERT: Deve estar ordenado por precedência
        assertEquals(3, resultado.size(), "Deve ter 3 disciplinas");
        assertEquals(disciplinaObrigatoria, resultado.get(0), "Obrigatória deve vir primeiro");
        assertEquals(disciplinaEletiva, resultado.get(1), "Eletiva deve vir segundo");
        assertEquals(disciplinaOptativa, resultado.get(2), "Optativa deve vir terceiro");
    }
    
    @Test
    @DisplayName("Deve detectar conflito entre disciplinas de mesma precedência")
    void testConflitoMesmaPrecedencia() {
        // ARRANGE: Conflito entre duas obrigatórias
        Set<Subject> disciplinasConflitantes = new HashSet<>();
        disciplinasConflitantes.add(disciplinaObrigatoria);
        disciplinasConflitantes.add(disciplinaObrigatoria2);
        
        // ACT & ASSERT: Deve detectar conflito
        assertTrue(service.hasSamePrecedenceConflict(disciplinasConflitantes), 
                  "Deve detectar conflito entre disciplinas de mesma precedência");
    }
    
    @Test
    @DisplayName("Não deve detectar conflito entre disciplinas de precedências diferentes")
    void testNaoConflitoPrecedenciasDiferentes() {
        // ARRANGE: Disciplinas de precedências diferentes
        Set<Subject> disciplinasConflitantes = new HashSet<>();
        disciplinasConflitantes.add(disciplinaObrigatoria);
        disciplinasConflitantes.add(disciplinaEletiva);
        
        // ACT & ASSERT: Não deve detectar conflito
        assertFalse(service.hasSamePrecedenceConflict(disciplinasConflitantes), 
                   "Não deve detectar conflito entre disciplinas de precedências diferentes");
    }
    
    @Test
    @DisplayName("Deve obter disciplina com maior precedência")
    void testGetHighestPrecedenceSubject() {
        // ARRANGE: Disciplinas com diferentes precedências
        Set<Subject> disciplinas = new HashSet<>();
        disciplinas.add(disciplinaEletiva);
        disciplinas.add(disciplinaObrigatoria);
        disciplinas.add(disciplinaOptativa);
        
        // ACT: Obtém disciplina com maior precedência
        Subject resultado = service.getHighestPrecedenceSubject(disciplinas);
        
        // ASSERT: Deve ser a obrigatória
        assertEquals(disciplinaObrigatoria, resultado, "Deve ser a disciplina obrigatória");
    }
    
    @Test
    @DisplayName("Deve retornar null para conjunto vazio")
    void testGetHighestPrecedenceSubjectEmpty() {
        // ARRANGE: Conjunto vazio
        Set<Subject> disciplinas = new HashSet<>();
        
        // ACT: Obtém disciplina com maior precedência
        Subject resultado = service.getHighestPrecedenceSubject(disciplinas);
        
        // ASSERT: Deve ser null
        assertNull(resultado, "Deve retornar null para conjunto vazio");
    }
    
    @Test
    @DisplayName("Deve filtrar disciplinas por tipo")
    void testFilterByType() {
        // ARRANGE: Disciplinas de diferentes tipos
        Set<Subject> disciplinas = new HashSet<>();
        disciplinas.add(disciplinaObrigatoria);
        disciplinas.add(disciplinaEletiva);
        disciplinas.add(disciplinaOptativa);
        
        // ACT: Filtra por tipo
        List<Subject> obrigatorias = service.filterByType(disciplinas, RequiredSubject.class);
        List<Subject> eletivas = service.filterByType(disciplinas, ElectiveSubject.class);
        List<Subject> optativas = service.filterByType(disciplinas, OptionalSubject.class);
        
        // ASSERT: Deve filtrar corretamente
        assertEquals(1, obrigatorias.size(), "Deve ter 1 obrigatória");
        assertEquals(1, eletivas.size(), "Deve ter 1 eletiva");
        assertEquals(1, optativas.size(), "Deve ter 1 optativa");
        assertEquals(disciplinaObrigatoria, obrigatorias.get(0), "Deve ser a disciplina obrigatória");
        assertEquals(disciplinaEletiva, eletivas.get(0), "Deve ser a disciplina eletiva");
        assertEquals(disciplinaOptativa, optativas.get(0), "Deve ser a disciplina optativa");
    }
    
    @Test
    @DisplayName("Deve resolver conflito com múltiplas eletivas")
    void testConflitoMultiplasEletivas() {
        // ARRANGE: Conflito entre duas eletivas
        Set<Subject> disciplinasConflitantes = new HashSet<>();
        disciplinasConflitantes.add(disciplinaEletiva);
        disciplinasConflitantes.add(disciplinaEletiva2);
        
        // ACT: Resolve conflito
        List<Subject> resultado = service.resolveConflicts(disciplinasConflitantes);
        
        // ASSERT: Deve ter 2 disciplinas (ordem não importa para mesma precedência)
        assertEquals(2, resultado.size(), "Deve ter 2 disciplinas");
        assertTrue(resultado.contains(disciplinaEletiva), "Deve conter primeira eletiva");
        assertTrue(resultado.contains(disciplinaEletiva2), "Deve conter segunda eletiva");
    }
    
    @Test
    @DisplayName("Deve resolver conflito com múltiplas optativas")
    void testConflitoMultiplasOptativas() {
        // ARRANGE: Conflito entre duas optativas
        Set<Subject> disciplinasConflitantes = new HashSet<>();
        disciplinasConflitantes.add(disciplinaOptativa);
        disciplinasConflitantes.add(disciplinaOptativa2);
        
        // ACT: Resolve conflito
        List<Subject> resultado = service.resolveConflicts(disciplinasConflitantes);
        
        // ASSERT: Deve ter 2 disciplinas (ordem não importa para mesma precedência)
        assertEquals(2, resultado.size(), "Deve ter 2 disciplinas");
        assertTrue(resultado.contains(disciplinaOptativa), "Deve conter primeira optativa");
        assertTrue(resultado.contains(disciplinaOptativa2), "Deve conter segunda optativa");
    }
} 