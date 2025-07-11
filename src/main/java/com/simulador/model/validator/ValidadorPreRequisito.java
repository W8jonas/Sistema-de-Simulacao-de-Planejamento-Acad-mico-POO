package com.simulador.model.validator;

import com.simulador.model.domain.Student;
import com.simulador.model.domain.Subject;

// Interface para validar pré-requisitos
public interface ValidadorPreRequisito {
    
    // Verifica se o aluno pode cursar a disciplina
    boolean validar(Student student, Subject subject);
}
