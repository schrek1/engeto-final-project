package com.engeto.bus_reservation.service;

import com.engeto.bus_reservation.model.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationsHolder {

    private final List<Reservation> reservations = new ArrayList<>();

    public void addNewReservation(Reservation newReservation) {
        if (newReservation != null) {
            reservations.add(newReservation);
        }
    }

    public List<Reservation> getAllReservations() {
        return reservations;
    }

    public void replaceAllReservationsBy(List<Reservation> reservations) {
        if (reservations != null) {
            this.reservations.clear();
            this.reservations.addAll(reservations);
        }
    }
}
