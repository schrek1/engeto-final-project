package com.engeto.bus_reservation;

import com.engeto.bus_reservation.model.Reservation;
import com.engeto.bus_reservation.ui.forms.ReservationsListFrame;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;

/**
 * Entry point to the application.
 */
public class Main {
    public static void main(String[] args) {
        List<Reservation> sampleData = new ArrayList<>();


//        for (int i = 0; i < 200; i++) {
//            sampleData.add(new Reservation(
//                    LocalDate.now(),
//                    "7:00",
//                    "Ostrava",
//                    "Brno",
//                    "John Doe",
//                    Arrays.asList(i, 1,2,8)
//            ));
//        }

        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            ReservationsListFrame mainWindow = new ReservationsListFrame(sampleData);
            mainWindow.pack();
            mainWindow.setVisible(true);
        });
    }
}
