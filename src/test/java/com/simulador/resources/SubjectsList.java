package com.simulador.resources;
import com.simulador.model.domain.*;
import java.util.List;

public class SubjectsList {
    private SubjectsList() { }

    public static List<Subject> list() {

        Subject calculusI   = new RequiredSubject("MAT101", "Calculus I", 4);
        Subject physicsI    = new RequiredSubject("PHY101", "Physics I", 4);
        Subject programming = new ElectiveSubject("CS101", "Intro to Programming", 4);
        Subject artApp      = new OptionalSubject("ART100", "Art Appreciation", 2);

        return List.of(calculusI, physicsI, programming, artApp);
    }
}

