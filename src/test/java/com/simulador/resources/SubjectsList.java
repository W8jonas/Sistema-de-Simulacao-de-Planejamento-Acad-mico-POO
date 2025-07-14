package com.simulador.resources;

import com.simulador.model.domain.*;

import java.util.Arrays;
import java.util.List;

public class SubjectsList {
    public static List<Subject> getSubjects() {
        Subject calculoI = new RequiredSubject("MAT154", "Cálculo I", 4);
        Subject fisicaI = new RequiredSubject("FIS073", "Física I", 4);
        Subject algoritmos = new ElectiveSubject("DCC119", "Algoritmos", 4);
        Subject seminarios = new OptionalSubject("DCC100", "Seminários", 2);
        
        return Arrays.asList(calculoI, fisicaI, algoritmos, seminarios);
    }
}

