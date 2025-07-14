package com.simulador.resources;

import com.simulador.model.domain.*;

import java.util.Arrays;
import java.util.List;

public class SubjectsList {
    public static List<Subject> getSubjects() {
        Subject calculoI = new RequiredSubject("MAT154", "Cálculo I", 4);
        Subject fisicaI = new RequiredSubject("FIS073", "Física I", 4);
        Subject visaoComputacional = new ElectiveSubject("DCC197", "VISÃO COMPUTACIONAL", 4);
        Subject introducaoSistemas = new OptionalSubject("D133", "introducao a sistemas de informação", 2);
        
        return Arrays.asList(calculoI, fisicaI, visaoComputacional, introducaoSistemas);
    }
}

