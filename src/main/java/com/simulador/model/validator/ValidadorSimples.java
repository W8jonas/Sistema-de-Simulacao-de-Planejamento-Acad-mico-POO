package com.simulador.model.validator;

import com.simulador.model.domain.Student;
import com.simulador.model.domain.Subject;
import com.simulador.model.exceptions.PreRequisitoNaoCumpridoException;

// Valida se uma disciplina pré-requisito foi cursada
public class ValidadorSimples implements ValidadorPreRequisito {
    
    private Subject preRequisito;
    
    public ValidadorSimples(Subject preRequisito) {
        this.preRequisito = preRequisito;
    }
    
    @Override
    public boolean validar(Student student, Subject subject) {
        // Verifica se o aluno cursou a disciplina pré-requisito
        if (!student.hasCompletedSubject(preRequisito)) {
            return false;
        }
        
        // Verifica se a nota é >= 6.0 (aprovado)
        Double nota = student.getGrade(preRequisito);
        return nota != null && nota >= 6.0;
    }
    
    /**
     * Valida e lança exceção se pré-requisito não for atendido
     */
    public void validarComExcecao(Student student, Subject subject) throws PreRequisitoNaoCumpridoException {
        if (!student.hasCompletedSubject(preRequisito)) {
            throw new PreRequisitoNaoCumpridoException(
                "Pré-requisito não cursado: " + preRequisito.getCode() + " - " + preRequisito.getName()
            );
        }
        
        Double nota = student.getGrade(preRequisito);
        if (nota == null || nota < 6.0) {
            throw new PreRequisitoNaoCumpridoException(
                "Pré-requisito não aprovado: " + preRequisito.getCode() + 
                " - Nota: " + nota + " (mínimo: 6.0)"
            );
        }
    }
} 