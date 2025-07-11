package com.simulador.model.domain;

public class Schedule {
    private final int dayOfWeek;
    private final int startTime;
    private final int endTime;

    public Schedule(int dayOfWeek, int startTime, int endTime) {
        if (dayOfWeek < 1 || dayOfWeek > 7)
            throw new IllegalArgumentException("dayOfWeek must be 1–7. 1 to monday, 7 to sunday");
        if (startTime < 0 || endTime > 22 || startTime >= endTime)
            throw new IllegalArgumentException("invalid start/end minutes");
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }
    public int getStartMinute() {
        return startTime;
    }
    public int getEndMinute() {
        return endTime;
    }

    public boolean conflicts(Schedule other) {
        if (dayOfWeek != other.dayOfWeek) return false;
        return startTime < other.endTime && other.startTime < endTime;
    }

    @Override
    public String toString() {
        return "D " + dayOfWeek + " [" + startTime + "–" + endTime + "]";
    }
}
