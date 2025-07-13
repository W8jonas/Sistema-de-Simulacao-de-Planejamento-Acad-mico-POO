package com.simulador.model.domain;

import com.simulador.model.exceptions.TurmaCheiaException;
import java.util.*;

public class ClassGroup {
    private final String id;
    private final Subject subject;
    private final int capacity;
    private final List<Schedule> schedules;
    private final Set<Student> enrolledStudents;

    public ClassGroup(String id, Subject subject, int capacity, List<Schedule> schedules) {
        this.id = id;
        this.subject = subject;
        if (capacity <= 0) throw new IllegalArgumentException("capacity must be positive");
        if (schedules == null || schedules.isEmpty())
            throw new IllegalArgumentException("at least one schedule is required");
        this.capacity = capacity;
        this.schedules = List.copyOf(schedules);
        this.enrolledStudents = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public Subject getSubject() {
        return subject;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<Schedule> getSchedules() {
        return new ArrayList<>(schedules);
    }

    public boolean conflictsWith(ClassGroup other) {
        for (Schedule s1 : schedules)
            for (Schedule s2 : other.schedules)
                if (s1.conflicts(s2)) return true;
        return false;
    }

    /**
     * Verifica se há vagas disponíveis na turma
     */
    public boolean hasAvailableSlots() {
        return enrolledStudents.size() < capacity;
    }

    /**
     * Obtém o número de vagas disponíveis
     */
    public int getAvailableSlots() {
        return capacity - enrolledStudents.size();
    }

    /**
     * Obtém o número de alunos matriculados
     */
    public int getEnrolledStudentsCount() {
        return enrolledStudents.size();
    }

    /**
     * Matricula um aluno na turma
     * @throws TurmaCheiaException se a turma estiver cheia
     */
    public void enrollStudent(Student student) throws TurmaCheiaException {
        if (!hasAvailableSlots()) {
            throw new TurmaCheiaException("Turma " + id + " está cheia. Capacidade: " + capacity);
        }
        enrolledStudents.add(student);
    }

    /**
     * Remove um aluno da turma
     */
    public void removeStudent(Student student) {
        enrolledStudents.remove(student);
    }

    /**
     * Verifica se um aluno está matriculado na turma
     */
    public boolean isStudentEnrolled(Student student) {
        return enrolledStudents.contains(student);
    }

    /**
     * Obtém a lista de alunos matriculados
     */
    public Set<Student> getEnrolledStudents() {
        return new HashSet<>(enrolledStudents);
    }
}
