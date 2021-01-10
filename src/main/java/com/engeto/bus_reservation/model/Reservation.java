package com.engeto.bus_reservation.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Reservation made by the customer.
 */
public class Reservation {

    public final LocalDate day;
    public final String time;
    public final String from;
    public final String to;
    public final String customerName;
    public final List<Integer> seats;

    public Reservation(LocalDate day, String time, String from, String to, String customerName, List<Integer> seats) {
        this.day = day;
        this.time = time;
        this.from = from;
        this.to = to;
        this.customerName = customerName;
        this.seats = seats;
    }

    public LocalDate getDay() {
        return day;
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

    public String getCustomerName() {
        return customerName;
    }

    public List<Integer> getSeats() {
        return seats;
    }
}
