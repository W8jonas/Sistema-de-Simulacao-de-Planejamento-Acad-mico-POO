package com.simulador.model.domain;

public abstract class Subject {
    private final String code;
    private final String name;
    private final int weeklyHours;

    protected Subject(String code, String name, int weeklyHours) {
        this.code = code;
        this.name = name;
        this.weeklyHours = weeklyHours;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getWeeklyHours() {
        return weeklyHours;
    }

    @Override
    public String toString() {
        return code + " - " + name;
    }
}
