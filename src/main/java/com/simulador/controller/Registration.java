package com.simulador.controller;

import com.simulador.model.domain.*;
import java.util.List;

/*
■ Carregar dados de disciplinas, turmas e alunos.
■ Gerenciar o processo de planejamento de matrícula para os alunos.
■ Aplicar todas as regras de validação (pré-requisitos, co-requisitos, carga horária, vagas, conflitos).
■ Atualizar o histórico do aluno e gerar os relatórios da simulação.
■ Far-se-á uso intensivo de polimorfismo ao chamar os métodos de validação e ao interagir com os diferentes tipos de disciplinas.
*/

public final class Registration {

    public static void main(String[] args) {
        System.out.println("Hello teste!");
    }

    public void run() {
        Schedule monday = new Schedule(1, 10, 12);
        Schedule wednesday = new Schedule(3, 10, 12);

        Subject calculus = new RequiredSubject("MAT101", "Cálculo I", 4);
        ClassGroup groupA = new ClassGroup("MAT101-A", calculus, 40, List.of(monday, wednesday));

        System.out.println("Subject:   " + calculus);
        System.out.println("Class ID:  " + groupA.getId());
        System.out.println("Capacity:  " + groupA.getCapacity());
        System.out.println("Schedules: ");
        groupA.getSchedules().forEach(s -> System.out.println("  • " + s));
    }
}
