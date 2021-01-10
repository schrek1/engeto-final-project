package com.engeto.bus_reservation.ui.model;

import com.engeto.bus_reservation.model.Reservation;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ReservationsTableModel extends AbstractTableModel {
    private final List<Reservation> reservations;
    private final String[] columnNames = {"Day", "Time", "From", "To", "Customer", "Seats"};

    public ReservationsTableModel() {
        this.reservations = new ArrayList<>();
    }

    public void replaceAllReservations(List<Reservation> reservations) {
        if (reservations != null) {
            this.reservations.clear();
            this.reservations.addAll(reservations);
            fireTableDataChanged();
        }
    }

    public void addNewReservation(Reservation newReservation) {
        if (newReservation != null) {
            reservations.add(newReservation);
            fireTableRowsInserted(reservations.size() - 1, reservations.size() - 1);
        }
    }

    @Override
    public int getRowCount() {
        return reservations.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Reservation reservation = reservations.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return reservation.day;
            case 1:
                return reservation.time;
            case 2:
                return reservation.from;
            case 3:
                return reservation.to;
            case 4:
                return reservation.customerName;
            case 5:
                return reservation.seats;
            default:
                throw new IllegalArgumentException("Cannot read column " + columnIndex);
        }
    }
}
