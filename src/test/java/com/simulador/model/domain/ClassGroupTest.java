package com.simulador.model.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * Testes unitários para a classe ClassGroup
 */
@DisplayName("Testes da Classe ClassGroup")
public class ClassGroupTest {
    
    private Subject disciplina;
    private Schedule horario1;
    private Schedule horario2;
    private ClassGroup turma;
    
    @BeforeEach
    void setUp() {
        disciplina = new RequiredSubject("MAT154", "Cálculo I", 4);
        horario1 = new Schedule(1, 8, 10); // Segunda, 8h-10h
        horario2 = new Schedule(3, 14, 16); // Quarta, 14h-16h
        List<Schedule> horarios = Arrays.asList(horario1, horario2);
        turma = new ClassGroup("MAT154-01", disciplina, 30, horarios);
    }
    
    @Test
    @DisplayName("Criar turma válida")
    void testCriarTurmaValida() {
        assertEquals("MAT154-01", turma.getId());
        assertEquals(disciplina, turma.getSubject());
        assertEquals(30, turma.getCapacity());
        assertEquals(2, turma.getSchedules().size());
        assertTrue(turma.getSchedules().contains(horario1));
        assertTrue(turma.getSchedules().contains(horario2));
    }
    
    @Test
    @DisplayName("Criar turma com capacidade inválida - deve lançar exceção")
    void testCriarTurmaComCapacidadeInvalida() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ClassGroup("MAT154-01", disciplina, 0, Arrays.asList(horario1));
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new ClassGroup("MAT154-01", disciplina, -5, Arrays.asList(horario1));
        });
    }
    
    @Test
    @DisplayName("Criar turma sem horários - deve lançar exceção")
    void testCriarTurmaSemHorarios() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ClassGroup("MAT154-01", disciplina, 30, null);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new ClassGroup("MAT154-01", disciplina, 30, Arrays.asList());
        });
    }
    
    @Test
    @DisplayName("Verificar conflito entre turmas com horários sobrepostos")
    void testConflitoComHorariosSobrepostos() {
        // Turma 1: Segunda 8h-10h, Quarta 14h-16h
        ClassGroup turma1 = new ClassGroup("MAT154-01", disciplina, 30, 
            Arrays.asList(horario1, horario2));
        
        // Turma 2: Segunda 9h-11h (conflita com Segunda 8h-10h)
        Schedule horarioConflitante = new Schedule(1, 9, 11);
        ClassGroup turma2 = new ClassGroup("MAT154-02", disciplina, 30, 
            Arrays.asList(horarioConflitante));
        
        assertTrue(turma1.conflictsWith(turma2), "Turmas devem conflitar");
        assertTrue(turma2.conflictsWith(turma1), "Conflito deve ser recíproco");
    }
    
    @Test
    @DisplayName("Verificar sem conflito entre turmas com horários diferentes")
    void testSemConflitoComHorariosDiferentes() {
        // Turma 1: Segunda 8h-10h, Quarta 14h-16h
        ClassGroup turma1 = new ClassGroup("MAT154-01", disciplina, 30, 
            Arrays.asList(horario1, horario2));
        
        // Turma 2: Terça 8h-10h, Quinta 14h-16h (dias diferentes)
        Schedule horarioTerça = new Schedule(2, 8, 10);
        Schedule horarioQuinta = new Schedule(4, 14, 16);
        ClassGroup turma2 = new ClassGroup("MAT154-02", disciplina, 30, 
            Arrays.asList(horarioTerça, horarioQuinta));
        
        assertFalse(turma1.conflictsWith(turma2), "Turmas não devem conflitar");
        assertFalse(turma2.conflictsWith(turma1), "Conflito deve ser recíproco");
    }
    
    @Test
    @DisplayName("Verificar conflito parcial entre turmas")
    void testConflitoParcial() {
        // Turma 1: Segunda 8h-10h, Quarta 14h-16h
        ClassGroup turma1 = new ClassGroup("MAT154-01", disciplina, 30, 
            Arrays.asList(horario1, horario2));
        
        // Turma 2: Segunda 9h-11h (conflita), Quarta 16h-18h (não conflita)
        Schedule horarioConflitante = new Schedule(1, 9, 11);
        Schedule horarioNaoConflitante = new Schedule(3, 16, 18);
        ClassGroup turma2 = new ClassGroup("MAT154-02", disciplina, 30, 
            Arrays.asList(horarioConflitante, horarioNaoConflitante));
        
        assertTrue(turma1.conflictsWith(turma2), "Turmas devem conflitar mesmo com apenas um horário conflitante");
        assertTrue(turma2.conflictsWith(turma1), "Conflito deve ser recíproco");
    }
    
    @Test
    @DisplayName("Verificar turma com múltiplos horários")
    void testTurmaComMultiplosHorarios() {
        Schedule horario3 = new Schedule(5, 10, 12); // Sexta, 10h-12h
        List<Schedule> horarios = Arrays.asList(horario1, horario2, horario3);
        ClassGroup turmaMultipla = new ClassGroup("MAT154-03", disciplina, 25, horarios);
        
        assertEquals(3, turmaMultipla.getSchedules().size());
        assertTrue(turmaMultipla.getSchedules().contains(horario1));
        assertTrue(turmaMultipla.getSchedules().contains(horario2));
        assertTrue(turmaMultipla.getSchedules().contains(horario3));
    }
    
    @Test
    @DisplayName("Verificar que horários são imutáveis")
    void testHorariosImutaveis() {
        List<Schedule> horariosOriginais = turma.getSchedules();
        
        // Verificar que a lista retornada é uma cópia (não a lista original)
        assertNotSame(turma.getSchedules(), turma.getSchedules(), "Deve retornar uma nova lista a cada chamada");
        
        // A turma deve manter seus horários originais
        assertEquals(2, turma.getSchedules().size());
        assertTrue(turma.getSchedules().contains(horario1));
        assertTrue(turma.getSchedules().contains(horario2));
    }
} 