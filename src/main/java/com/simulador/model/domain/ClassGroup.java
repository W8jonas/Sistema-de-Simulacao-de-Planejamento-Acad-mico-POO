package com.simulador.model.domain;

import java.util.*;

public class ClassGroup {
    private final String id;
    private final Subject subject;
    private final int capacity;
    private final List<Schedule> schedules;

    public ClassGroup(String id, Subject subject, int capacity, List<Schedule> schedules) {
        this.id = id;
        this.subject = subject;
        if (capacity <= 0) throw new IllegalArgumentException("capacity must be positive");
        if (schedules == null || schedules.isEmpty())
            throw new IllegalArgumentException("at least one schedule is required");
        this.capacity = capacity;
        this.schedules = List.copyOf(schedules);
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
        return schedules;
    }

    public boolean conflictsWith(ClassGroup other) {
        for (Schedule s1 : schedules)
            for (Schedule s2 : other.schedules)
                if (s1.conflicts(s2)) return true;
        return false;
    }
}
