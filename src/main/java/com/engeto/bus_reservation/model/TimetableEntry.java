package com.engeto.bus_reservation.model;

/**
 * One line from the timetable.
 */
public class TimetableEntry {

    public final String time;
    public final String from;
    public final String to;
    public final int capacity;

    public TimetableEntry(String time, String from, String to, int capacity) {
        this.time = time;
        this.from = from;
        this.to = to;
        this.capacity = capacity;
    }

    public String getTime() {
        return time;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getCapacity() {
        return capacity;
    }
}
