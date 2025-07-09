package com.simulador.model.validator;

/*
○ O ValidadorPreRequisito (Interface) define o contrato para
qualquer regra de validação de pré-requisito. Terá um método,
por exemplo, boolean validar(Aluno aluno, Disciplina
disciplina). Isso permite que diferentes lógicas de
pré-requisito sejam tratadas de forma polimórfica.
■ ValidadorSimples (Implementa ValidadorPreRequisito):
valida se uma única disciplina pré-requisito foi
concluída (com nota >= 60).
■ ValidadorLogicoAND (Implementa ValidadorPreRequisito):
valida se um conjunto de pré-requisitos foi atendido.
■ ValidadorLogicoOR (Implementa ValidadorPreRequisito):
valida se pelo menos um dos pré-requisitos foi atendido.
■ ValidadorCreditosMinimos (Implementa
ValidadorPreRequisito): valida se o aluno atingiu uma
quantidade mínima de créditos.
*/

public class validator {
}
