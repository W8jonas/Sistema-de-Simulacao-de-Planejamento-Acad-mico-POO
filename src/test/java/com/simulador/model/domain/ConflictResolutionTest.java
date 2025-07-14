package com.simulador.model.domain;

import com.simulador.services.ConflictResolutionService;
import com.simulador.model.exceptions.ConflitoDeHorarioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * Testes para resolução de conflitos de horário baseado na precedência
 */
@DisplayName("Testes de Resolução de Conflitos")
public class ConflictResolutionTest {
    
    private ConflictResolutionService conflictService;
    private RequiredSubject disciplinaObrigatoria;
    private ElectiveSubject disciplinaEletiva;
    private OptionalSubject disciplinaOptativa;
    private Schedule horario;
    
    @BeforeEach
    void setUp() {
        conflictService = new ConflictResolutionService();
        
        // Criar disciplinas de diferentes tipos
        disciplinaObrigatoria = new RequiredSubject("MAT001", "Matemática Obrigatória", 4);
        disciplinaEletiva = new ElectiveSubject("DCC001", "Programação Eletiva", 4);
        disciplinaOptativa = new OptionalSubject("ART001", "Arte Optativa", 2);
        
        horario = new Schedule(1, 8, 10); // Segunda 8h-10h
    }
    
    @Test
    @DisplayName("Obrigatória deve prevalecer sobre Eletiva")
    void obrigatoriaDevePrevalecerSobreEletiva() throws ConflitoDeHorarioException {
        // Arrange
        ClassGroup turmaObrigatoria = new ClassGroup("MAT001-01", disciplinaObrigatoria, 30, Arrays.asList(horario));
        ClassGroup turmaEletiva = new ClassGroup("DCC001-01", disciplinaEletiva, 25, Arrays.asList(horario));
        
        List<ClassGroup> turmasConflitantes = Arrays.asList(turmaObrigatoria, turmaEletiva);
        
        // Act
        List<ClassGroup> resultado = conflictService.resolveConflicts(turmasConflitantes);
        
        // Assert
        assertEquals(1, resultado.size());
        assertEquals(turmaObrigatoria, resultado.get(0));
        assertEquals("Obrigatória", resultado.get(0).getSubject().getType());
    }
    
    @Test
    @DisplayName("Eletiva deve prevalecer sobre Optativa")
    void eletivaDevePrevalecerSobreOptativa() throws ConflitoDeHorarioException {
        // Arrange
        ClassGroup turmaEletiva = new ClassGroup("DCC001-01", disciplinaEletiva, 25, Arrays.asList(horario));
        ClassGroup turmaOptativa = new ClassGroup("ART001-01", disciplinaOptativa, 20, Arrays.asList(horario));
        
        List<ClassGroup> turmasConflitantes = Arrays.asList(turmaEletiva, turmaOptativa);
        
        // Act
        List<ClassGroup> resultado = conflictService.resolveConflicts(turmasConflitantes);
        
        // Assert
        assertEquals(1, resultado.size());
        assertEquals(turmaEletiva, resultado.get(0));
        assertEquals("Eletiva", resultado.get(0).getSubject().getType());
    }
    
    @Test
    @DisplayName("Conflito entre disciplinas de mesma precedência deve lançar exceção")
    void conflitoMesmaPrecedenciaDeveLancarExcecao() {
        // Arrange
        RequiredSubject outraObrigatoria = new RequiredSubject("FIS001", "Física Obrigatória", 4);
        ClassGroup turmaObrigatoria1 = new ClassGroup("MAT001-01", disciplinaObrigatoria, 30, Arrays.asList(horario));
        ClassGroup turmaObrigatoria2 = new ClassGroup("FIS001-01", outraObrigatoria, 25, Arrays.asList(horario));
        
        List<ClassGroup> turmasConflitantes = Arrays.asList(turmaObrigatoria1, turmaObrigatoria2);
        
        // Act & Assert
        assertThrows(ConflitoDeHorarioException.class, () -> {
            conflictService.resolveConflicts(turmasConflitantes);
        });
    }
    
    @Test
    @DisplayName("Turmas em horários diferentes não devem ter conflito")
    void turmasHorariosDiferentesNaoDevemTerConflito() throws ConflitoDeHorarioException {
        // Arrange
        Schedule horario2 = new Schedule(2, 8, 10); // Terça 8h-10h
        ClassGroup turma1 = new ClassGroup("MAT001-01", disciplinaObrigatoria, 30, Arrays.asList(horario));
        ClassGroup turma2 = new ClassGroup("DCC001-01", disciplinaEletiva, 25, Arrays.asList(horario2));
        
        List<ClassGroup> turmas = Arrays.asList(turma1, turma2);
        
        // Act
        List<ClassGroup> resultado = conflictService.resolveConflicts(turmas);
        
        // Assert
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(turma1));
        assertTrue(resultado.contains(turma2));
    }
    
    @Test
    @DisplayName("Lista vazia deve retornar lista vazia")
    void listaVaziaDeveRetornarListaVazia() throws ConflitoDeHorarioException {
        // Act
        List<ClassGroup> resultado = conflictService.resolveConflicts(Arrays.asList());
        
        // Assert
        assertTrue(resultado.isEmpty());
    }
} 