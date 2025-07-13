package com.simulador.model.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe Schedule
 */
@DisplayName("Testes da Classe Schedule")
public class ScheduleTest {
    
    @Test
    @DisplayName("Criar horário válido")
    void testCriarHorarioValido() {
        Schedule horario = new Schedule(1, 8, 10); // Segunda, 8h-10h
        
        assertEquals(1, horario.getDayOfWeek());
        assertEquals(8, horario.getStartMinute());
        assertEquals(10, horario.getEndMinute());
    }
    
    @Test
    @DisplayName("Criar horário com dia inválido - deve lançar exceção")
    void testCriarHorarioComDiaInvalido() {
        // Dia 0 (inválido)
        assertThrows(IllegalArgumentException.class, () -> {
            new Schedule(0, 8, 10);
        });
        
        // Dia 8 (inválido)
        assertThrows(IllegalArgumentException.class, () -> {
            new Schedule(8, 8, 10);
        });
    }
    
    @Test
    @DisplayName("Criar horário com horário inválido - deve lançar exceção")
    void testCriarHorarioComHorarioInvalido() {
        // Horário negativo
        assertThrows(IllegalArgumentException.class, () -> {
            new Schedule(1, -1, 10);
        });
        
        // Horário muito alto
        assertThrows(IllegalArgumentException.class, () -> {
            new Schedule(1, 8, 25);
        });
        
        // Horário de início >= horário de fim
        assertThrows(IllegalArgumentException.class, () -> {
            new Schedule(1, 10, 8);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Schedule(1, 10, 10);
        });
    }
    
    @Test
    @DisplayName("Verificar conflito de horário no mesmo dia")
    void testConflitoMesmoDia() {
        Schedule horario1 = new Schedule(1, 8, 10);   // Segunda, 8h-10h
        Schedule horario2 = new Schedule(1, 9, 11);   // Segunda, 9h-11h
        
        assertTrue(horario1.conflicts(horario2), "Horários devem conflitar");
        assertTrue(horario2.conflicts(horario1), "Conflito deve ser recíproco");
    }
    
    @Test
    @DisplayName("Verificar conflito de horário sobreposto")
    void testConflitoSobreposto() {
        Schedule horario1 = new Schedule(1, 8, 12);   // Segunda, 8h-12h
        Schedule horario2 = new Schedule(1, 10, 14);  // Segunda, 10h-14h
        
        assertTrue(horario1.conflicts(horario2), "Horários sobrepostos devem conflitar");
        assertTrue(horario2.conflicts(horario1), "Conflito deve ser recíproco");
    }
    
    @Test
    @DisplayName("Verificar conflito de horário adjacente")
    void testConflitoAdjacente() {
        Schedule horario1 = new Schedule(1, 8, 10);   // Segunda, 8h-10h
        Schedule horario2 = new Schedule(1, 10, 12);  // Segunda, 10h-12h
        
        assertFalse(horario1.conflicts(horario2), "Horários adjacentes não devem conflitar");
        assertFalse(horario2.conflicts(horario1), "Conflito deve ser recíproco");
    }
    
    @Test
    @DisplayName("Verificar sem conflito em dias diferentes")
    void testSemConflitoDiasDiferentes() {
        Schedule horario1 = new Schedule(1, 8, 10);   // Segunda, 8h-10h
        Schedule horario2 = new Schedule(2, 8, 10);   // Terça, 8h-10h
        
        assertFalse(horario1.conflicts(horario2), "Horários em dias diferentes não devem conflitar");
        assertFalse(horario2.conflicts(horario1), "Conflito deve ser recíproco");
    }
    
    @Test
    @DisplayName("Verificar sem conflito em horários diferentes")
    void testSemConflitoHorariosDiferentes() {
        Schedule horario1 = new Schedule(1, 8, 10);   // Segunda, 8h-10h
        Schedule horario2 = new Schedule(1, 14, 16);  // Segunda, 14h-16h
        
        assertFalse(horario1.conflicts(horario2), "Horários em horários diferentes não devem conflitar");
        assertFalse(horario2.conflicts(horario1), "Conflito deve ser recíproco");
    }
    
    @Test
    @DisplayName("Testar representação em string")
    void testToString() {
        Schedule horario = new Schedule(1, 8, 10);
        String resultado = horario.toString();
        
        assertTrue(resultado.contains("D 1"), "Deve conter o dia da semana");
        assertTrue(resultado.contains("[8–10]"), "Deve conter o horário");
    }
} 