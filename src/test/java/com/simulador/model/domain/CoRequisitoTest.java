package com.simulador.model.domain;

import com.simulador.model.exceptions.CoRequisitoNaoAtendidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Testes para o sistema de co-requisitos
 */
@DisplayName("Testes de Co-requisitos")
public class CoRequisitoTest {
    
    private RequiredSubject disciplinaPrincipal;
    private RequiredSubject coRequisito1;
    private RequiredSubject coRequisito2;
    private Student aluno;
    
    @BeforeEach
    void setUp() {
        // Criar disciplinas
        disciplinaPrincipal = new RequiredSubject("DCC001", "Disciplina Principal", 4);
        coRequisito1 = new RequiredSubject("DCC002", "Co-requisito 1", 2);
        coRequisito2 = new RequiredSubject("DCC003", "Co-requisito 2", 2);
        
        // Criar aluno
        aluno = new Student("João Silva", "202365082A", 20);
    }
    
    @Test
    @DisplayName("Disciplina sem co-requisitos deve ser válida")
    void disciplinaSemCoRequisitosDeveSerValida() {
        // Arrange
        Set<Subject> planejamento = new HashSet<>();
        planejamento.add(disciplinaPrincipal);
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            validarCoRequisitos(planejamento);
        });
    }
    
    @Test
    @DisplayName("Disciplina com co-requisitos atendidos deve ser válida")
    void disciplinaComCoRequisitosAtendidosDeveSerValida() {
        // Arrange
        disciplinaPrincipal.addCoRequisito(coRequisito1);
        disciplinaPrincipal.addCoRequisito(coRequisito2);
        
        Set<Subject> planejamento = new HashSet<>();
        planejamento.add(disciplinaPrincipal);
        planejamento.add(coRequisito1);
        planejamento.add(coRequisito2);
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            validarCoRequisitos(planejamento);
        });
    }
    
    @Test
    @DisplayName("Disciplina com co-requisito não atendido deve lançar exceção")
    void disciplinaComCoRequisitoNaoAtendidoDeveLancarExcecao() {
        // Arrange
        disciplinaPrincipal.addCoRequisito(coRequisito1);
        
        Set<Subject> planejamento = new HashSet<>();
        planejamento.add(disciplinaPrincipal);
        // Não adicionou coRequisito1
        
        // Act & Assert
        assertThrows(CoRequisitoNaoAtendidoException.class, () -> {
            validarCoRequisitos(planejamento);
        });
    }
    
    @Test
    @DisplayName("Disciplina com co-requisito parcialmente atendido deve lançar exceção")
    void disciplinaComCoRequisitoParcialmenteAtendidoDeveLancarExcecao() {
        // Arrange
        disciplinaPrincipal.addCoRequisito(coRequisito1);
        disciplinaPrincipal.addCoRequisito(coRequisito2);
        
        Set<Subject> planejamento = new HashSet<>();
        planejamento.add(disciplinaPrincipal);
        planejamento.add(coRequisito1);
        // Não adicionou coRequisito2
        
        // Act & Assert
        assertThrows(CoRequisitoNaoAtendidoException.class, () -> {
            validarCoRequisitos(planejamento);
        });
    }
    
    @Test
    @DisplayName("Co-requisito deve ser válido mesmo sem a disciplina principal")
    void coRequisitoDeveSerValidoSemDisciplinaPrincipal() {
        // Arrange
        disciplinaPrincipal.addCoRequisito(coRequisito1);
        
        Set<Subject> planejamento = new HashSet<>();
        planejamento.add(coRequisito1);
        // Não adicionou disciplinaPrincipal
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            validarCoRequisitos(planejamento);
        });
    }
    
    /**
     * Método auxiliar para validar co-requisitos
     */
    private void validarCoRequisitos(Set<Subject> planejamento) throws CoRequisitoNaoAtendidoException {
        for (Subject disciplina : planejamento) {
            if (disciplina.hasCoRequisitos()) {
                Set<Subject> coRequisitos = disciplina.getCoRequisitos();
                for (Subject coRequisito : coRequisitos) {
                    if (!planejamento.contains(coRequisito)) {
                        throw new CoRequisitoNaoAtendidoException(
                            "Co-requisito não atendido: " + disciplina.getCode() + 
                            " requer " + coRequisito.getCode() + " no mesmo período"
                        );
                    }
                }
            }
        }
    }
} 