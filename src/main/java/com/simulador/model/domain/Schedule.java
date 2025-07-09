package com.simulador.model.domain;

public class Schedule {
    private final int dayOfWeek;
    private final int startMinute;
    private final int endMinute;

    public Schedule(int dayOfWeek, int startMinute, int endMinute) {
        if (dayOfWeek < 1 || dayOfWeek > 7)
            throw new IllegalArgumentException("dayOfWeek must be 1–7. 1 to monday, 7 to sunday");
        if (startMinute < 0 || endMinute > 1440 || startMinute >= endMinute)
            throw new IllegalArgumentException("invalid start/end minutes");
        this.dayOfWeek = dayOfWeek;
        this.startMinute = startMinute;
        this.endMinute = endMinute;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }
    public int getStartMinute() {
        return startMinute;
    }
    public int getEndMinute() {
        return endMinute;
    }

    public boolean conflicts(Schedule other) {
        if (dayOfWeek != other.dayOfWeek) return false;
        return startMinute < other.endMinute && other.startMinute < endMinute;
    }

    @Override
    public String toString() {
        return "D " + dayOfWeek + " [" + startMinute + "–" + endMinute + "]";
    }
}
